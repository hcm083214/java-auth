package com.hcm.common.core.entity;

import com.hcm.common.core.domain.BaseEntity;
import com.hcm.common.vo.FunctionVo;
import com.hcm.common.vo.RoleVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色表 sys_role
 *
 * @author ruoyi
 */
@Data
public class SysRole extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleNameCn;

    /**
     * 角色中文描述
     */
    private String roleDescriptionCn;

    /**
     * 角色英文名称
     */
    private String roleNameEn;

    /**
     * 角色英文描述
     */
    private String roleDescriptionEn;

    /**
     * 权限字符列表字符串
     */
    private String functionJson;

    /**
     * 功能权限列表
     */
    private List<SysFunction> functionList;

    /**
     * 角色排序
     */
    private Integer roleSort;

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private boolean deptCheckStrictly;

    /**
     * 角色状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    private boolean flag = false;

    /**
     * 菜单组
     */
    private Long[] menuIds;

    /**
     * 部门组（数据权限）
     */
    private Long[] deptIds;

    /**
     * 角色菜单权限
     */
    private List<Long> permissions;


    /**
     * po2vo
     *
     * @param sysRole 角色
     */
    public static RoleVo po2vo(SysRole sysRole){
        RoleVo roleVo = new RoleVo();
        BeanUtils.copyProperties(sysRole,roleVo);
        if(sysRole.getFunctionList() !=null && sysRole.getFunctionList().size()>0){
            List<FunctionVo> functionVoList = new ArrayList<>(sysRole.getFunctionList().size());
            sysRole.getFunctionList().forEach(sysFunction -> {
                FunctionVo functionVo = new FunctionVo();
                BeanUtils.copyProperties(sysFunction,functionVo);
                functionVoList.add(functionVo);
            });
            roleVo.setFunctionList(functionVoList);
        }
        return roleVo;
    }

    /**
     * pos2vos
     *
     * @param sysRoleList 角色列表
     * @return {@link List}<{@link RoleVo}>
     */
    public static List<RoleVo> pos2vos(List<SysRole> sysRoleList){
        List<RoleVo> roleVoList = new ArrayList<>(sysRoleList.size());
        sysRoleList.forEach(sysRole -> {
            RoleVo roleVo = po2vo(sysRole);
            roleVoList.add(roleVo);
        });
        return roleVoList;
    }
}
