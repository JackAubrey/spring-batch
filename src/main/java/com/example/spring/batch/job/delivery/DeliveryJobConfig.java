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
                                 @Qualifier("ShippingPackageStep") Step shippingPackageStep,
                                 @Qualifier("DeliveryPackToCustomerStep") Step deliveryPackToCustomerStep,
                                 @Qualifier("DeliverySuccessStep") Step deliverySuccessStep,
                                 @Qualifier("StorePackBackStep") Step storePackBackStep,
                                 @Qualifier("PlanAgainDeliveryStep") Step planAgainDeliveryStep) {
        return new JobBuilder("DeliveryJob", jobRepository)
                .start(packItemStep)
                .next(shippingPackageStep)
                .next(deliveryPackToCustomerStep)
                    .on("FAILED").to(storePackBackStep).next(planAgainDeliveryStep)
                .from(deliveryPackToCustomerStep)
                    .on("*").to(deliverySuccessStep)
                .end()
                .build();
    }

    @Bean("PackItemStep")
    public Step createPackItemStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("PackItemStep", jobRepository)
                .tasklet(new PackItemTasklet(), transactionManager)
                .build();
    }

    @Bean("ShippingPackageStep")
    public Step createShippingPackageStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("ShippingPackageStep", jobRepository)
                .tasklet(new ShippingPackageTasklet(), transactionManager)
                .build();
    }

    @Bean("DeliveryPackToCustomerStep")
    public Step createDeliveryPackToCustomerStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("DeliveryPackToCustomerStep", jobRepository)
                .tasklet(new DeliveryPackToCustomerTasklet(), transactionManager)
                .build();
    }

    @Bean("DeliverySuccessStep")
    public Step createDeliverySuccessStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("DeliverySuccessStep", jobRepository)
                .tasklet(new DeliverySuccessStepTasklet(), transactionManager)
                .build();
    }

    @Bean("StorePackBackStep")
    public Step createStorePackBackStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("StorePackBackStep", jobRepository)
                .tasklet(new StorePackTasklet(), transactionManager)
                .build();
    }

    @Bean("PlanAgainDeliveryStep")
    public Step createPlanAgainDeliveryStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("PlanAgainDeliveryStep", jobRepository)
                .tasklet(new PlanAgainDeliveryTasklet(), transactionManager)
                .build();
    }
}
