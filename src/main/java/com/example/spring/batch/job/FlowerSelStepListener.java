package com.example.spring.batch.job;

import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class FlowerSelStepListener implements StepExecutionListener {
    private static final Logger logger = getLogger(FlowerSelStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        logger.info(">> DO IN BEFORE-STEP << Flower Sel Step Listener");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info(">> DO IN AFTER-STEP << Flower Sel Step Listener");
        JobParameters parameters = stepExecution.getJobParameters();
        String flowerType = parameters.getString("input.flower.type");
        logger.info(">> DO IN AFTER-STEP << Flower Type {}", flowerType);

        if("rosa".equalsIgnoreCase(flowerType) || "rose".equalsIgnoreCase(flowerType)) {
            return new ExitStatus("TRIM_REQUIRED");
        } else {
            return new ExitStatus("NO_TRIM_REQUIRED");
        }
    }
}
