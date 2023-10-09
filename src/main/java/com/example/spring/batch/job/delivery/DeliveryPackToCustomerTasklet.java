package com.example.spring.batch.job.delivery;

import org.slf4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDate;

import static org.slf4j.LoggerFactory.getLogger;

public class DeliveryPackToCustomerTasklet implements Tasklet {
    private static final Logger logger = getLogger(DeliveryPackToCustomerTasklet.class);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
        String article = jobParameters.getString("input.article");
        String customerPresent = jobParameters.getString("input.simulate.customer.present");
        String customerName = jobParameters.getString("input.customer.name");

        if("false".equalsIgnoreCase(customerPresent)) {
            logger.error("SIMULATE on date {} the customer {} is not present to take the article {}", LocalDate.now(), customerName, article);
            throw new RuntimeException("Nobody is in home");
        } else {
            logger.info("The article {} has been delivered on date {} to the customer {}", article, LocalDate.now(), customerName);
            return RepeatStatus.FINISHED;
        }
    }
}
