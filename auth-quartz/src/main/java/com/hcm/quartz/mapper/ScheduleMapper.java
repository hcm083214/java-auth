package com.hcm.quartz.mapper;

import com.hcm.quartz.entity.SysJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 定时任务mapper
 *
 * @author pc
 * @date 2023/05/11
 */
@Mapper
public interface ScheduleMapper {
    /**
     * 得到工作列表
     *
     * @return {@link List}<{@link SysJob}>
     */
    List<SysJob> getJobList();
}
