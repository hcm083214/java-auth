package com.hcm.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author pc
 * @date 2023/05/01
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * cpu 核心数
     */
    private final Integer cpuSize = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程池大小 : CPU密集型应用 比如加密、解密、压缩、计算等一系列需要大量耗费 CPU 资源的任务，则线程池大小设置为N+1
     */
    private final Integer corePoolSize = cpuSize + 1;

    /**
     * 最大可创建的线程数 比如数据库、文件的读写，网络通信等任务,则线程池大小设置为2N+1
     */
    private final Integer maxPoolSize = 2 * cpuSize + 1;

    /**
     * 队列最大长度
     */
    private final int queueCapacity = 10;

    /**
     * 线程池维护线程所允许的空闲时间:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁
     */
    private final int keepAliveSeconds = 300;

    /**
     * 线程池设置
     *
     * @return {@link ThreadPoolExecutor}
     */
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueCapacity), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
