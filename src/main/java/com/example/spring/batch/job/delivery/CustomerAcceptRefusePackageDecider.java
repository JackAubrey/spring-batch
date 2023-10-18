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
public class CustomerAcceptRefusePackageDecider implements JobExecutionDecider {
    private static final Logger logger = getLogger(CustomerAcceptRefusePackageDecider.class);
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String accepted = jobParameters.getString("input.simulate.customer.accept.package");
        logger.info("Customer has accepted the package ? {}", accepted);

        if("false".equalsIgnoreCase(accepted)) {
            String article = jobParameters.getString("input.article");
            String date = jobParameters.getString("input.date");
            String customerName = jobParameters.getString("input.customer.name");
            logger.info("the customer {} has refused the product {} bought on {}", customerName, article, date);
            return new FlowExecutionStatus("DELIVERY.ACCEPTED.FALSE");
        } else {
            return new FlowExecutionStatus("DELIVERY.ACCEPTED.TRUE");
        }
    }
}
