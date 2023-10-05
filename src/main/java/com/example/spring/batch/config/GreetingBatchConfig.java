package com.example.spring.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class GreetingBatchConfig {
    @Bean
    public Job getGreetingJob(JobRepository jobRepository, Step greetingStep) {
        return new JobBuilder("GreetingJob", jobRepository)
                .start(greetingStep)
                .build();
    }

    @Bean
    public Step getGreetingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("GreetingStep", jobRepository)
                .tasklet(new GreetingTasklet(), platformTransactionManager)
                .build();
    }
}
