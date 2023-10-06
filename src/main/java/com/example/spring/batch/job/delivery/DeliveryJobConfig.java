package com.example.spring.batch.job.delivery;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class DeliveryJobConfig {
    @Bean
    public Job createDeliveryJob(JobRepository jobRepository,
                                 @Qualifier("PackItemStep") Step packItemStep,
                                 @Qualifier("RouteToAddressStep") Step routeToAddressStep,
                                 @Qualifier("DeliveryPackToAddressStep") Step deliveryPackToAddress) {
        return new JobBuilder("DeliveryJob", jobRepository)
                .start(packItemStep)
                .next(routeToAddressStep)
                .next(deliveryPackToAddress)
                .build();
    }

    @Bean("PackItemStep")
    public Step createPackItemStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("PackItemStep", jobRepository)
                .tasklet(new PackItemTasklet(), transactionManager)
                .build();
    }

    @Bean("RouteToAddressStep")
    public Step createRouteToAddressStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("RouteToAddressStep", jobRepository)
                .tasklet(new RouteToAddressTasklet(), transactionManager)
                .build();
    }

    @Bean("DeliveryPackToAddressStep")
    public Step createDeliveryPackToAddress(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("DeliveryPackToAddressStep", jobRepository)
                .tasklet(new DeliveryPackToAddressTasklet(), transactionManager)
                .build();
    }
}
