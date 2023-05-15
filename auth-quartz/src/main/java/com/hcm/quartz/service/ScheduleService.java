package com.hcm.quartz.service;

import com.hcm.quartz.entity.SysJob;

import java.util.List;

/**
 * 定时任务服务
 *
 * @author pc
 * @date 2023/05/11
 */
public interface ScheduleService {

    /**
     * 得到工作列表
     *
     * @return {@link List}<{@link SysJob}>
     */
    List<SysJob> getJobList();
    /**
     * 添加任务
     *
     * @param sysJob 系统工作
     */
    void addTask(SysJob sysJob);

    /**
     * 运行任务
     *
     * @param sysJob 系统工作
     */
    void run(SysJob sysJob);
}
