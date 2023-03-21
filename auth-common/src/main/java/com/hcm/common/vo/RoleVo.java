package com.hcm.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author pc
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    @ExcelProperty("角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @ExcelProperty("角色权限")
    private String roleKey;

    /**
     * 角色排序
     */
    @ExcelProperty("角色排序")
    private Integer roleSort;

    /**
     * 角色状态（0正常 1停用）
     */
    @ExcelProperty("角色状态（0正常 1停用")
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createTime;

    /**
     * 当前页
     */
    @ExcelIgnore
    private Integer pageNum;

    /**
     * 每页显示数量
     */
    @ExcelIgnore
    private Integer pageSize;
}
