package com.hcm.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.hcm.common.annotations.EnumValue;
import com.hcm.common.enums.RoleSearchTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
     * 角色中文名称
     */
    @Length(max = 20,message = "角色中文名称长度必须位于2到20以内")
    @ExcelProperty("角色中文名称")
    private String roleNameCn;

    /**
     * 角色英文名称
     */
    @Length(max = 20,message = "角色英文名称长度必须位于2到20以内")
    @ExcelProperty("角色英文名称")
    private String roleNameEn;

    /**
     * 角色中文描述
     */
    private String roleDescriptionCn;

    /**

     /**
     * 角色英文描述
     */
    private String roleDescriptionEn;

    /**
     * 角色权限
     */
    @Length(max = 20,message = "角色权限长度必须位于20以内")
    @ExcelProperty("权限字符串")
    private String functionKey;

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
     * 搜索参数
     */
    @ExcelIgnore
    @EnumValue(enumClass = RoleSearchTypeEnum.class,ignoreCase = true,message = "传入的参数不正确")
    private String searchParams;
}
