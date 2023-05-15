package com.hcm.quartz.constants;

/**
 * 定时任务常量类
 *
 * @author pc
 * @date 2023/05/10
 */
public class ScheduleConstants {
    /**
     * cron计划策略:默认
     */
    public static final String MISFIRE_DEFAULT = "0";

    /**
     * cron计划策略:立即触发执行
     */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /**
     * cron计划策略:触发一次执行
     */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /**
     * cron计划策略:不触发立即执行
     */
    public static final String MISFIRE_DO_NOTHING = "3";

    /**
     * 任务并发执行
     */
    public static final String JOB_DO_CONCURRENT = "1";

    /**
     * 任务非并发执行
     */
    public static final String JOB_NOT_CONCURRENT = "0";

    /**
     * 任务类名
     */
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** 执行目标key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";
}
