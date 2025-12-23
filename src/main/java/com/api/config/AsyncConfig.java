package com.api.config;

import java.util.concurrent.Executor;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	@Bean(name = "emailExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();				
		executor.setCorePoolSize(2);      // min threads
        executor.setMaxPoolSize(5);       // max threads
        executor.setQueueCapacity(100);   // queued tasks
        executor.setThreadNamePrefix("email-");
        executor.initialize();
        return executor;
	}
	
}
