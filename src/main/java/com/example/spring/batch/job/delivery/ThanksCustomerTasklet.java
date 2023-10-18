package com.example.spring.batch.job.delivery;

import org.slf4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import static org.slf4j.LoggerFactory.getLogger;

public class ThanksCustomerTasklet implements Tasklet {
    private static final Logger logger = getLogger(ThanksCustomerTasklet.class);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
        String article = jobParameters.getString("input.article");
        String date = jobParameters.getString("input.date");
        String customerName = jobParameters.getString("input.customer.name");
        logger.info("Dear {} thanks for your ship of the product {} bought on {}", customerName, article, date);
        return RepeatStatus.FINISHED;
    }
}
