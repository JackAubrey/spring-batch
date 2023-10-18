package com.example.spring.batch.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DeliveryDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        JobParameters parameters = jobExecution.getJobParameters();
        String customerIsPresent = parameters.getString("input.simulate.customer.is.present");

        System.out.println("The customer is present in home ? "+customerIsPresent);

        String result = "true".equalsIgnoreCase(customerIsPresent) ? "PRESENT" : "NOT_PRESENT";
        return new FlowExecutionStatus(result);
    }
}
