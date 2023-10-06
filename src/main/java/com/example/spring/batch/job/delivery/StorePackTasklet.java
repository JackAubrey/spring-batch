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
        JobParameters parameters = contribution.getStepExecution().getJobParameters();
        String article = parameters.getString("input.article");
        logger.error("The package {} on date {} has been returned to the main storage!!!", article, LocalDate.now());
        return RepeatStatus.FINISHED;
    }
}