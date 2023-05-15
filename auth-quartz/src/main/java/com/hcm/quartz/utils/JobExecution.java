package com.hcm.quartz.utils;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author pc
 * @date 2023/05/10
 */
public class JobExecution extends AbstractQuartzJob{
    /**
     * 执行方法，由子类重载
     */
    @Override
    protected void doExecute() {
        JobInvokeUtil.invokeMethod();
    }
}
