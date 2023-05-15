package com.hcm.quartz.controller;

import com.hcm.quartz.entity.SysJob;
import com.hcm.quartz.service.ScheduleService;
import com.hcm.quartz.utils.ScheduleUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * 定时任务控制器
 *
 * @author pc
 * @date 2023/05/11
 */
@Api("定时任务服务")
@Slf4j
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private Scheduler scheduler;

    @GetMapping
    @ApiOperation(value = "开启定时任务", notes = "开启定时任务")
    public void startSchedule(){
        List<SysJob> jobList = scheduleService.getJobList();
        jobList.forEach(sysJob -> {
            ScheduleUtils.createScheduleTask(scheduler,sysJob);
        });
    }
}
