package com.gpxparser.config;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * User: dschetinin <a href="mailto:schetinin.d@gmail.com"/>schetinin.d@gmail.com</a>
 * Date: 10/27/2016
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Configuration
@EnableScheduling
public class SchedulerConfig implements SchedulingConfigurer {

    private static final Logger logger = LogManager.getLogger(SchedulerConfig.class);

    @Value("${executor.corePoolSize}")
    private int corePoolSize;     // 5

    @Value("${executor.maxPoolSize}")
    private int maxPoolSize;      // 10

    @Value("${executor.queueCapacity}")
    private int queueCapacity;    // 25

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // check the documentation to see the difference between setScheduler and setTaskScheduler
        // http://docs.spring.io/spring/docs/3.0.x/spring-framework-reference/html/scheduling.html
        taskRegistrar.setScheduler(schedulerService());
//        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    @Bean(destroyMethod="shutdown")
    public ScheduledExecutorService schedulerService() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(corePoolSize);
        taskExecutor.setMaxPoolSize(maxPoolSize);
        taskExecutor.setQueueCapacity(queueCapacity);

        return new ScheduledThreadPoolExecutor(corePoolSize, taskExecutor);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(corePoolSize);
        return taskScheduler;
    }

}
