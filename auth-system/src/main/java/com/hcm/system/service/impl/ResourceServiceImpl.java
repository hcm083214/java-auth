package com.hcm.system.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.hcm.common.constants.CacheConstants;
import com.hcm.common.constants.CommonConstants;
import com.hcm.common.core.entity.SysResource;
import com.hcm.common.core.entity.SysRole;
import com.hcm.common.core.entity.SysUser;
import com.hcm.common.core.redis.RedisHPCache;
import com.hcm.common.core.redis.RedisHashCache;
import com.hcm.common.core.redis.RedisStringCache;
import com.hcm.common.utils.ServletUtils;
import com.hcm.common.utils.StringUtils;
import com.hcm.common.utils.ip.IpUtils;
import com.hcm.common.vo.ResourceVo;
import com.hcm.system.mapper.ResourceMapper;
import com.hcm.system.service.ResourceService;
import com.hcm.system.service.ViewCounterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author pc
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceService {

    @Resource
    private ResourceMapper resourceMapper;

    @Autowired
    private RedisHashCache redisHashCache;

    @Autowired
    private ViewCounterService viewCounterService;

    /**
     * 获得菜单列表
     *
     * @param sysUser sysUser
     */
    @Override
    public List<SysResource> getMenuList(SysUser sysUser) {
        List<SysResource> sysResourceList = getUserResource(sysUser);
        return list2Tree(sysResourceList);
    }

    /**
     * 得到所有菜单列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    @Override
    public List<SysResource> getMenuListAll() {
        List<SysResource> sysResourceList = resourceMapper.geResourceList();
        return list2Tree(sysResourceList);
    }

    /**
     * 编辑菜单
     *
     * @param resourceVo resourceVo
     */
    @Override
    public void editMenu(ResourceVo resourceVo) {
        resourceMapper.editMenu(resourceVo);
    }

    /**
     * 添加菜单
     *
     * @param resourceVo resourceVo
     */
    @Override
    public void addMenu(ResourceVo resourceVo) {
        resourceMapper.addMenu(resourceVo);
    }

    /**
     * 编辑资源父id
     *
     * @param resourceVoList resourceVo
     */
    @Override
    public void editResourceParentId(List<ResourceVo> resourceVoList) {
        List<ResourceVo> updateList = new ArrayList<>();
        resourceVoList.forEach(resourceVo -> {
            if (!resourceVo.getParentId().equals(0L)) {
                resourceVo.setParentId(0L);
                updateList.add(resourceVo);
            }
        });
        setParentIdAndFlat(resourceVoList, updateList);
        if (updateList.size() > 0) {
            resourceMapper.updateParentId(updateList);
        }
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     */
    @Override
    public void deleteResource(Long resourceId) {
        resourceMapper.deleteResource(resourceId);
    }

    /**
     * 设置父id和设置更新列表
     *
     * @param resourceVoList 资源vo列表
     * @param updateList     更新列表
     */
    private void setParentIdAndFlat(List<ResourceVo> resourceVoList, List<ResourceVo> updateList) {
        for (ResourceVo resourceVo : resourceVoList) {
            Long parentId = resourceVo.getResourceId();
            List<ResourceVo> children = resourceVo.getChildren();
            children.forEach(resource -> {
                if (!resource.getParentId().equals(parentId)) {
                    resource.setParentId(parentId);
                    updateList.add(resource);
                }
            });
            if (children.size() > 0) {
                setParentIdAndFlat(children, updateList);
            }
        }
    }

    /**
     * 将菜单列表转化为菜单树
     *
     * @param sysResourceList 系统菜单列表
     * @return {@link List}<{@link SysResource}>
     */
    private List<SysResource> list2Tree(List<SysResource> sysResourceList) {
        List<SysResource> sysResourceResult = new ArrayList<>();
        if (sysResourceList != null) {
            // 获取 parentId = 0的根节点
            sysResourceResult = sysResourceList.stream().filter(sysMenu -> sysMenu.getParentId().equals(0L)).collect(Collectors.toList());
            // 根据 parentId 进行分组
            Map<Long, List<SysResource>> map = sysResourceList.stream().collect(Collectors.groupingBy(SysResource::getParentId));
            recursionTree(sysResourceResult, map);
        }
        return sysResourceResult;
    }

    /**
     * 生成递归树
     *
     * @param menuList 树列表
     * @param map      目标
     */
    private void recursionTree(List<SysResource> menuList, Map<Long, List<SysResource>> map) {
        menuList.forEach(tree -> {
            List<SysResource> childList = map.get(tree.getResourceId());
            tree.setChildren(childList);
            if (tree.getChildren() != null && tree.getChildren().size() > 0) {
                recursionTree(childList, map);
            }
        });
    }

    /**
     * 得到所有的api信息
     *
     * @param request 请求
     * @return {@link List}<{@link SysResource}>
     */
    @Override
    public List<SysResource> syncApiInfo(HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        List<SysResource> sysApiList = new ArrayList<>();
        if (servletContext != null) {
            WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            assert appContext != null;
            // 获取所有的 RequestMapping
            Map<String, HandlerMapping> allRequestMappings = BeanFactoryUtils.beansOfTypeIncludingAncestors(appContext, HandlerMapping.class, true, false);
            allRequestMappings.forEach((name, handlerMapping) -> {
                if (handlerMapping instanceof RequestMappingHandlerMapping) {
                    RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) handlerMapping;
                    Map<RequestMappingInfo, HandlerMethod> handlerMethodsMap = requestMappingHandlerMapping.getHandlerMethods();
                    handlerMethodsMap.forEach((requestMappingInfo, handlerMethods) -> {
                        SysResource sysResource = new SysResource();
                        // 获取 controller 的 class 的全类名
                        Class<?> handlerMethodsBeanType = handlerMethods.getBeanType();
                        // 获取 control 内的方法
                        Method method = handlerMethods.getMethod();
                        // 获取请求方式
                        RequestMethodsRequestCondition requestMethodsRequestCondition = requestMappingInfo.getMethodsCondition();
                        sysResource.setRequestType(set2String(requestMethodsRequestCondition.getMethods()));
                        // 获取请求地址
                        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                        sysResource.setPath(set2String(patternsCondition.getPatterns()));
                        // 获取方法名
                        sysResource.setMethodName(method.getName());
                        // 获取类名
                        String className = handlerMethodsBeanType.toString().replace("class", "")
                                .replace(" ", "");
                        sysResource.setControllerClass(className);
                        if (handlerMethodsBeanType.isAnnotationPresent(Api.class)) {
                            Api apiOperation = handlerMethodsBeanType.getAnnotation(Api.class);
                            String[] tags = apiOperation.tags();
                            sysResource.setControllerName(String.join(",", tags));
                        }
                        if (method.isAnnotationPresent(PreAuthorize.class)) {
                            PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                            String preAuthorizeVal = preAuthorize.value();
                            sysResource.setPerms(getPermissionStr(preAuthorizeVal));
                        }
                        if (method.isAnnotationPresent(ApiOperation.class)) {
                            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
                            String value = apiOperation.value();
                            sysResource.setResourceName(value);
                            String notes = apiOperation.notes();
                            sysResource.setDescription(StringUtils.isNull(notes) ? value : notes);
                        }
                        if (StringUtils.isNotNull(sysResource.getResourceName())) {
                            sysApiList.add(sysResource);
                        }
                    });
                }
            });
        }
        return sysApiList;
    }

    /**
     * set转字符串
     *
     * @param set 列表
     * @return {@link String}
     */
    private String set2String(Set<?> set) {
        return set == null ? "" : set.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 得到该接口的权限字符
     *
     * @param exp 经验值
     * @return {@link String}
     */
    private String getPermissionStr(String exp) {
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(exp);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    /**
     * 获得api列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    @Override
    public List<SysResource> getApiList() {
        List<SysResource> apiList = new ArrayList<>();
        Map<String, Object> sysResourceMap = redisHashCache.entries(CacheConstants.CACHE_RESOURCE);
        if (sysResourceMap.isEmpty()) {
            apiList = resourceMapper.getApiList();
            apiList.forEach(sysResource -> {
                String hashKey = getApiName(sysResource);
                redisHashCache.put(CacheConstants.CACHE_RESOURCE, sysResource.getResourceId().toString(), hashKey);
                redisHashCache.put(CacheConstants.CACHE_RESOURCE, hashKey, sysResource);
                viewCounterService.setResourcePVCount(sysResource, 1);
            });
        } else {
            for (Map.Entry<String, Object> entry : sysResourceMap.entrySet()) {
                Object val = entry.getValue();
                if (val instanceof SysResource) {
                    apiList.add((SysResource) val);
                    viewCounterService.setResourcePVCount((SysResource) val, 1);
                }
            }
        }
        return apiList;
    }

    /**
     * 得到api信息
     *
     * @param resourceId 资源id
     * @return {@link SysResource}
     */
    @Override
    public SysResource getApiInfo(Long resourceId) {
        SysResource sysResource = new SysResource();
        String hashKey = (String) redisHashCache.get(CacheConstants.CACHE_RESOURCE, resourceId.toString());
        if (StringUtils.isNull(hashKey)) {
            sysResource = resourceMapper.getResourceById(resourceId);
            String hashKeyNew = getApiName(sysResource);
            redisHashCache.put(CacheConstants.CACHE_RESOURCE, sysResource.getResourceId().toString(), hashKeyNew);
            redisHashCache.put(CacheConstants.CACHE_RESOURCE, hashKeyNew, sysResource);
        } else {
            sysResource = (SysResource) redisHashCache.get(CacheConstants.CACHE_RESOURCE, hashKey);
        }
        return sysResource;
    }

    @Override
    public String getApiName(SysResource sysResource) {
        return sysResource.getControllerClass() + "." + sysResource.getMethodName();
    }

    /**
     * 添加api列表
     *
     * @param sysResourceList 系统资源列表
     */
    @Override
    public void addApiList(List<SysResource> sysResourceList) {
        resourceMapper.addApiList(sysResourceList);
    }

    /**
     * 通过当前用户的functionId列表得到资源列表
     *
     * @param sysUser@return {@link List}<{@link SysResource}>
     */
    @Override
    public List<SysResource> getUserResource(SysUser sysUser) {
        List<SysRole> roleList = sysUser.getRoleList();
        Set<Long> functionIds = new HashSet<>();
        roleList.forEach(sysRole -> {
            List<Long> functionIdList = JSONArray.parseArray(sysRole.getFunctionJson(), Long.class);
            functionIds.addAll(functionIdList);
        });
        List<SysResource> resourceList = new ArrayList<>();
        if (functionIds.size() > 0) {
            resourceList = resourceMapper.getResourceListByFunctions(new ArrayList<>(functionIds));
        }
        return resourceList;
    }
}
