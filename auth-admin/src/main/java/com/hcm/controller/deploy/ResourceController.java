package com.hcm.controller.deploy;

import com.hcm.common.core.domain.ResultVO;
import com.hcm.common.core.entity.SysResource;
import com.hcm.common.vo.ResourceVo;
import com.hcm.system.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
    @ApiOperation(value = "获取菜单列表",notes = "获取菜单列表")
    public ResultVO<List<ResourceVo>> getMenuList() {
        List<SysResource> sysResourceList = resourceService.getMenuList();
        List<ResourceVo> resourceVoList = new ArrayList<>();
        SysResource.pos2vos(sysResourceList, resourceVoList);
        return ResultVO.success(resourceVoList);
    }

    /**
     * 获得功能权限列表,type = "M"， "C" 和 "F"
     *
     * @return {@link ResultVO}<{@link List}<{@link ResourceVo}>>
     */
    @GetMapping("/all")
    @ApiOperation(value = "获得功能权限列表",notes = "获得功能权限列表,包括页面，菜单，按钮权限")
    public ResultVO<List<ResourceVo>> getPermissionList(){
        List<SysResource> sysResourceList = resourceService.getMenuListAll();
        List<ResourceVo> resourceVoList = new ArrayList<>();
        SysResource.pos2vos(sysResourceList, resourceVoList);
        return ResultVO.success(resourceVoList);
    }

    /**
     * 得到api信息
     */
    @GetMapping("/api/sync")
    @PreAuthorize("@ss.hasPermission('resource:api:sync')")
    @ApiOperation(value = "同步api", notes = "得到api信息")
    public ResultVO<String> syncApiInfo(HttpServletRequest request){
       List<SysResource> sysResourceList = resourceService.syncApiInfo(request);
       resourceService.addApiList(sysResourceList);
       return ResultVO.success("同步成功");
    }

}
