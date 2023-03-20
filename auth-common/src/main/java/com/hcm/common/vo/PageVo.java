package com.hcm.common.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
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
    private Integer pageNum;

    /**
     * 每页显示的条数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

}
