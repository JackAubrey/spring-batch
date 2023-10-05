package com.example.spring.batch.config;

import org.slf4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import static org.slf4j.LoggerFactory.getLogger;

public class GreetingTasklet implements Tasklet {
    private static final Logger logger = getLogger(GreetingTasklet.class);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Hello!!!");
        return RepeatStatus.FINISHED;
    }
}
