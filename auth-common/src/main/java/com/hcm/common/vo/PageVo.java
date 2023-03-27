package com.hcm.common.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 页签证官
 * PageVo
 *
 * @author pc
 * @date 2023/03/19
 */
@Data
public class PageVo<T> {
    /**
     * 数据
     */
    private List<T> list;

    /**
     * 总数
     */
    private long total;

    /**
     * 当前页的页码
     */
    @Min(value = 1,message="pageNum至少为1")
    private Integer pageNum;

    /**
     * 每页显示的条数
     */
    @Max(value = 400,message="每页显示最多400条")
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

}
