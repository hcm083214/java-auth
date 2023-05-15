package com.hcm.quartz.utils;


import com.hcm.common.exception.BadRequestException;
import com.hcm.quartz.constants.ScheduleConstants;
import com.hcm.quartz.entity.SysJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

/**
 * 定时任务工具类
 *
 * @author pc
 * @date 2023/05/10
 */
public class ScheduleUtils {
    /**
     * 创建定时任务
     */
    public static void createScheduleTask(Scheduler scheduler, SysJob sysJob) throws BadRequestException{
        Class<? extends Job> jobClass = getJobClass(sysJob);
        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类
        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(getKey(sysJob))
                .build();
        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, sysJob);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression());
        setCronScheduleMisfirePolicy(sysJob, cronScheduleBuilder);

        // 按 cronExpression 表达式构建一个新的 trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(sysJob))
                .withSchedule(cronScheduleBuilder)
                .build();
        try {
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BadRequestException("create schedule task fail");
        }
    }

    /**
     * 设置cron调度失败政策
     *
     * @param sysJob              系统工作
     * @param cronScheduleBuilder cron调度建设者
     */
    private static void setCronScheduleMisfirePolicy(SysJob sysJob, CronScheduleBuilder cronScheduleBuilder) {
        switch (sysJob.getMisfirePolicy()){
            case ScheduleConstants.MISFIRE_DEFAULT:
                break;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
                break;
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
                break;
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
                break;
            default:
        }
    }

    private static TriggerKey getTriggerKey(SysJob sysJob) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + sysJob.getJobId(), sysJob.getJobGroup());
    }

    /**
     * 构建任务键对象
     *
     * @param sysJob sysJob
     * @return {@link JobKey}
     */
    private static JobKey getKey(SysJob sysJob) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + sysJob.getJobId(), sysJob.getJobGroup());
    }

    /**
     * 得到工作类
     *
     * @param sysJob sysJob
     * @return {@link Class}<{@link ?} {@link extends} {@link Job}>
     */
    public static Class<? extends Job> getJobClass(SysJob sysJob) {
        if (sysJob.getConcurrent().equals(ScheduleConstants.JOB_DO_CONCURRENT)) {
            return JobExecution.class;
        } else {
            return UnConcurrentJobExecution.class;
        }

    }
}
