package com.example.spring.batch.job.delivery;

import org.slf4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

public class StorePackTasklet implements Tasklet {
    private static final Logger logger = getLogger(StorePackTasklet.class);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
        String article = jobParameters.getString("input.article");
        String customerName = jobParameters.getString("input.customer.name");
        logger.error("The package {} for {} has been returned to the main storage on date {}", article, customerName, LocalDate.now());
        return RepeatStatus.FINISHED;
    }
}