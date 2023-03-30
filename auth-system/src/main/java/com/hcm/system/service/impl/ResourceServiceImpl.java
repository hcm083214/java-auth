package com.hcm.system.service.impl;

import com.hcm.common.core.entity.SysResource;
import com.hcm.common.utils.StringUtils;
import com.hcm.system.mapper.ResourceMapper;
import com.hcm.system.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
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
import java.util.ArrayList;
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

    /**
     * 获得菜单列表
     */
    @Override
    public List<SysResource> getMenuList() {
        List<SysResource> sysResourceList = resourceMapper.getMenuList();
        return getMenuTreeList(sysResourceList);
    }

    /**
     * 得到所有菜单列表
     *
     * @return {@link List}<{@link SysResource}>
     */
    @Override
    public List<SysResource> getMenuListAll() {
        List<SysResource> sysResourceList = resourceMapper.geResourceList();
        return getMenuTreeList(sysResourceList);
    }

    /**
     * 将菜单列表转化为菜单树
     *
     * @param sysResourceList 系统菜单列表
     * @return {@link List}<{@link SysResource}>
     */
    private List<SysResource> getMenuTreeList(List<SysResource> sysResourceList){
        List<SysResource> sysResourceResult = new ArrayList<>();
        if (sysResourceList != null) {
            // 获取 parentId = 0的根节点
            sysResourceResult = sysResourceList.stream().filter(sysMenu->sysMenu.getParentId().equals(0L)).collect(Collectors.toList());
            // 根据 parentId 进行分组
            Map<Long, List<SysResource>> map = sysResourceList.stream().collect(Collectors.groupingBy(SysResource::getParentId));
            recursionTree(sysResourceResult,map);
        }
        return sysResourceResult;
    }
    /**
     * 生成递归树
     *
     * @param menuList 树列表
     * @param map      目标
     */
    private void recursionTree(List<SysResource> menuList, Map<Long, List<SysResource>> map){
        menuList.forEach(tree->{
            List<SysResource> childList = map.get(tree.getResourceId());
            tree.setChildren(childList);
            if(tree.getChildren() != null && tree.getChildren().size()>0){
                recursionTree(childList,map);
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
                        sysResource.setRequestMethod(set2String(requestMethodsRequestCondition.getMethods()));
                        // 获取请求地址
                        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
                        sysResource.setPath(set2String(patternsCondition.getPatterns()));
                        // 获取方法名
                        sysResource.setResourceName(method.getName());
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
                            String notes = apiOperation.notes();
                            sysResource.setDescription(StringUtils.isNull(notes) ? value : notes);
                        }
                        sysApiList.add(sysResource);
                    });
                    log.info("sysApiList:{}", sysApiList);
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
        return null;
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
}
