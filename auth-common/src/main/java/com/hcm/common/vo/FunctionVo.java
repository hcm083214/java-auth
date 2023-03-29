package com.hcm.common.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hcm.common.annotations.EnumValue;
import com.hcm.common.enums.FunctionSearchTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

/**
 * 功能权限Vo
 *
 * @author pc
 * @date 2023/03/25
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FunctionVo {
    private static final long serialVersionUID = 1L;

    /**
     * 功能权限id
     */
    @ExcelProperty("权限id")
    private Long functionId;

    /**
     * 功能权限中文名称
     */
    @Length(max = 40,message = "功能权限中文名称长度必须位于2到40以内")
    @ExcelProperty("功能权限中文名称")
    private String functionNameCn;

    /**
     * 功能权限英文名称
     */
    @Length(max = 40,message = "功能权限英文名称长度必须位于2到40以内")
    @ExcelProperty("功能权限英文名称")
    private String functionNameEn;

    /**
     * 功能权限字符串
     */
    @Length(max = 20,message = "角色权限长度必须位于20以内")
    @ExcelProperty("功能权限字符串")
    private String functionKey;


    /**
     * 功能权限中文描述
     */
    @ExcelProperty("功能权限中文描述")
    private String functionDescriptionCn;

    /**
     * 功能权限英文描述
     */
    @ExcelProperty("功能权限英文描述")
    private String functionDescriptionEn;

    /**
     * 菜单列表json
     */
    @ExcelIgnore
    private String menuListJson;

    /**
     * 状态(0:无效，1：有效，2：软删除)
     */
    @ExcelProperty("状态(0:无效，1：有效，2：软删除)")
    private String status;

    /**
     * 创建者
     */
    @ExcelProperty("创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty("更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    private Date updateTime;

    /**
     * 功能权限列表（菜单和按钮）
     */
    @ExcelIgnore
    private List<Long> permissionIds;

    /**
     * 搜索参数
     */
    @ExcelIgnore
    @EnumValue(enumClass = FunctionSearchTypeEnum.class,message = "传入的参数不正确",ignoreCase = true)
    private String searchParams;
}
