package com.hcm.quartz.utils;

import org.quartz.DisallowConcurrentExecution;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author pc
 * @date 2023/05/10
 */
@DisallowConcurrentExecution
public class UnConcurrentJobExecution extends AbstractQuartzJob{
    /**
     * 执行方法，由子类重载
     */
    @Override
    protected void doExecute() {
        JobInvokeUtil.invokeMethod();
    }
}
