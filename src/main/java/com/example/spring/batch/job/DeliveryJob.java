package com.example.spring.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DeliveryJob {
    public Job prepareFlowers(
            JobRepository jobRepository,
            @Qualifier("SelectFlowersStep") Step selectFlowersStep,
            @Qualifier("RemoveThornsStep") Step removeThornsStep,
            @Qualifier("SendFlowersStep") Step sendFlowersStep) {
        return new JobBuilder("PrepareFlowersJob", jobRepository)
                .start(selectFlowersStep)
                    .on("TRIM_REQUIRED").to(removeThornsStep).next(sendFlowersStep)
                .from(selectFlowersStep)
                    .on("NO_TRIM_REQUIRED").to(sendFlowersStep)
                .end()
                .build();
    }

    @Bean("SelectFlowersStep")
    public Step selectFlowersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlowerSelStepListener flowerSelStepListener) {
        return new StepBuilder("SelectFlowersStep", jobRepository)
                .tasklet(new SelectFlowersTasklet(), transactionManager)
                .listener(flowerSelStepListener)
                .build();
    }

    @Bean("RemoveThornsStep")
    public Step removeThornsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("RemoveThornsStep", jobRepository)
                .tasklet(new RemoveThornsTasklet(), transactionManager)
                .build();
    }

    @Bean("SendFlowersStep")
    public Step sendFlowersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("SendFlowersStep", jobRepository)
                .tasklet(new SendFlowersTasklet(), transactionManager)
                .build();
    }
}
