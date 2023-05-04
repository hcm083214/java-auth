package com.hcm.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
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
     * 核心线程池大小
     */
    private final Integer corePoolSize = 50;

    /**
     * 最大可创建的线程数
     */
    private final Integer maxPoolSize = 200;

    /**
     * 队列最大长度
     */
    private final int queueCapacity = 1000;

    /**
     * 线程池维护线程所允许的空闲时间:当线程池中空闲线程数量超过corePoolSize时，多余的线程会在多长时间内被销毁
     */
    private final int keepAliveSeconds = 300;

    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor threadPoolExecutor(){
        return new ThreadPoolExecutor(corePoolSize,maxPoolSize,keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(10),new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
