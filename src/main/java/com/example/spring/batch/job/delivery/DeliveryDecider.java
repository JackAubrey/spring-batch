package com.example.spring.batch.job.delivery;

import org.slf4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class DeliveryDecider implements JobExecutionDecider {
    private static final Logger logger = getLogger(DeliveryDecider.class);
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String deliveryProblem = jobParameters.getString("input.simulate.delivery.problem");
        logger.info("Delivery Problem ? {}", deliveryProblem);
        FlowExecutionStatus exitStatus;

        if("true".equalsIgnoreCase(deliveryProblem)) {
            String article = jobParameters.getString("input.article");
            String date = jobParameters.getString("input.date");
            String customerName = jobParameters.getString("input.customer.name");
            logger.info("the product {} bought on {} by the customer {} has been lost", article, date, customerName);
            exitStatus = new FlowExecutionStatus("DELIVERY.FAILURE");
        } else {
            exitStatus = new FlowExecutionStatus("DELIVERY.SUCCESS");
        }

        System.out.println("DeliveryDecider Exit Status = "+exitStatus);
        return exitStatus;
    }
}
