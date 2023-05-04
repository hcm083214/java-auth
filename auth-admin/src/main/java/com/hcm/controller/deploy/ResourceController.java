package com.hcm.controller.deploy;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysResource;
import com.hcm.common.core.entity.UserDetail;
import com.hcm.common.vo.ResourceVo;
import com.hcm.framework.security.context.AuthenticationContextHolder;
import com.hcm.system.service.ResourceService;
import com.hcm.validation.ResourceValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pc
 */
@Slf4j
@Api(tags = "资源管理")
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 获取菜单列表,type = "M"和 "C"
     *
     * @return {@link List}<{@link ResourceVo}>
     */
    @GetMapping("/menu")
    @ApiOperation(value = "菜单资源查询",notes = "获取菜单列表")
    public ResultVO<List<ResourceVo>> getMenuList() {
        UserDetail user = AuthenticationContextHolder.getCurrentUser();
        List<SysResource> sysResourceList = resourceService.getMenuList(user.getSysUser());
        List<ResourceVo> resourceVoList = new ArrayList<>();
        SysResource.pos2vos(sysResourceList, resourceVoList);
        return ResultVO.success(resourceVoList);
    }

    /**
     * 编辑菜单信息
     *
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/menu/resource_id/{resourceId}")
    @ApiOperation(value = "菜单资源修改",notes = "菜单资源信息修改")
    @PreAuthorize("@ss.hasPermission('resource:menu:edit')")
    public ResultVO<String> editMenuInfo(@PathVariable("resourceId") Long resourceId,@RequestBody ResourceVo resourceVo){
        ResourceValidation.editMenuValidation(resourceId,resourceVo);
        resourceService.editMenu(resourceVo);
        return ResultVO.success("修改成功");
    }

    /**
     * 添加菜单信息
     *
     * @param resourceVo resourceVo
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/menu")
    @ApiOperation(value = "菜单资源新增",notes = "菜单资源信息新增")
    @PreAuthorize("@ss.hasPermission('resource:menu:add')")
    public ResultVO<String> addMenuInfo(@RequestBody ResourceVo resourceVo){
        ResourceValidation.addMenuValidation(resourceVo);
        resourceService.addMenu(resourceVo);
        return ResultVO.success("修改成功");
    }

    /**
     * 获得功能权限列表,type = "M"， "C" 和 "F"
     *
     * @return {@link ResultVO}<{@link List}<{@link ResourceVo}>>
     */
    @GetMapping("/all")
    @ApiOperation(value = "资源列表查询",notes = "获得功能权限列表,包括页面，菜单，按钮权限")
    @PreAuthorize("@ss.hasPermission('resource:permission:query')")
    public ResultVO<List<ResourceVo>> getPermissionList(){
        List<SysResource> sysResourceList = resourceService.getMenuListAll();
        List<ResourceVo> resourceVoList = new ArrayList<>();
        SysResource.pos2vos(sysResourceList, resourceVoList);
        return ResultVO.success(resourceVoList);
    }

    /**
     * 编辑资源父id
     *
     * @param resourceVoList 资源vo列表
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/all")
    @PreAuthorize("@ss.hasPermission('resource:parent:edit')")
    @ApiOperation(value = "资源父id修改", notes = "资源父id修改")
    public ResultVO<String> editResourceParentId(@RequestBody List<ResourceVo> resourceVoList){
        resourceService.editResourceParentId(resourceVoList);
        return ResultVO.success("修改成功");
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return {@link ResultVO}<{@link String}>
     */
    @PostMapping("/all/resource_id/{resource_id}")
    @ApiOperation(value = "删除资源",notes = "删除资源")
    @PreAuthorize("@ss.hasPermission('resource:permission:delete')")
    public ResultVO<String> deleteResource(@PathVariable("resource_id") Long resourceId){
        resourceService.deleteResource(resourceId);
        return ResultVO.success("删除成功");
    }

    /**
     * 刷新系统 api 信息
     *
     * @param request 请求
     * @return {@link ResultVO}<{@link String}>
     */
    @GetMapping("/api/sync")
    @PreAuthorize("@ss.hasPermission('resource:api:sync')")
    @ApiOperation(value = "Api资源同步", notes = "同步api信息")
    public ResultVO<String> syncApiInfo(HttpServletRequest request){
       List<SysResource> sysResourceList = resourceService.syncApiInfo(request);
       resourceService.addApiList(sysResourceList);
       return ResultVO.success("同步成功");
    }

    /**
     * 同步api信息到redis
     *
     * @return {@link ResultVO}<{@link String}>
     */
    @GetMapping("/api/sync/redis")
    @PreAuthorize("@ss.hasPermission('resource:api:sync')")
    @ApiOperation(value = "Api资源同步到 redis", notes = "同步api信息到redis")
    public ResultVO<String> syncApi2Redis(){
        List<SysResource> sysResourceList = resourceService.getApiList();
        resourceService.syncResource2Redis(sysResourceList);
        return ResultVO.success("同步成功");
    }

    /**
     * 获得api列表
     *
     * @return {@link ResultVO}<{@link List}<{@link ResourceVo}>>
     */
    @GetMapping("/api")
    @PreAuthorize("@ss.hasPermission('resource:api:query')")
    @ApiOperation(value = "Api资源查询",notes = "获得api列表")
    public ResultVO<List<ResourceVo>> getApiList(){
        List<SysResource> sysResourceList = resourceService.getApiList();
        List<ResourceVo> resourceVoList = new ArrayList<>();
        SysResource.pos2vos(sysResourceList, resourceVoList);
        return ResultVO.success(resourceVoList);
    }

}
