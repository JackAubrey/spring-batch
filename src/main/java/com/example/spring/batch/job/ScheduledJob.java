package com.example.spring.batch.job;

import org.slf4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class ScheduledJob {
    @Bean
    public Job job(JobRepository jobRepository, Step scheduledStep) {
        return new JobBuilder("scheduled-job", jobRepository)
                .start(scheduledStep)
                .build();
    }

    @Bean
    public Step scheduledStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("scheduled-step", jobRepository)
                .tasklet( new Tasklet() {
                    private static final Logger logger = getLogger(ScheduledJob.class);

                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(">>> Executing tasklet at {}", LocalDateTime.now());
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
