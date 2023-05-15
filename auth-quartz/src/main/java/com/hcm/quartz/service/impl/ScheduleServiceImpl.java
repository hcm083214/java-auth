package com.hcm.quartz.service.impl;

import com.hcm.quartz.entity.SysJob;
import com.hcm.quartz.mapper.ScheduleMapper;
import com.hcm.quartz.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ScheduleServiceImpl
 *
 * @author pc
 * @date 2023/05/11
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 得到工作列表
     *
     * @return {@link List}<{@link SysJob}>
     */
    @Override
    public List<SysJob> getJobList() {
        return scheduleMapper.getJobList();
    }

    /**
     * 添加任务
     *
     * @param sysJob 系统工作
     */
    @Override
    public void addTask(SysJob sysJob) {

    }

    /**
     * 运行任务
     *
     * @param sysJob 系统工作
     */
    @Override
    public void run(SysJob sysJob) {

    }
}
