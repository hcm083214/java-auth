package com.hcm.quartz.utils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class AbstractQuartzJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        doExecute();
    }

    /**
     * 执行方法，由子类重载
     */
    protected abstract void doExecute();
}
