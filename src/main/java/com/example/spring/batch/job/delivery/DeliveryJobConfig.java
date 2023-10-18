package com.example.spring.batch.job.delivery;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class DeliveryJobConfig {
    private final DeliveryDecider deliveryDecider;
    private final CustomerAcceptRefusePackageDecider acceptRefusePackageDecider;

    public DeliveryJobConfig(DeliveryDecider deliveryDecider, CustomerAcceptRefusePackageDecider acceptRefusePackageDecider) {
        this.deliveryDecider = deliveryDecider;
        this.acceptRefusePackageDecider = acceptRefusePackageDecider;
    }

    @Bean
    public Job createDeliveryJob(JobRepository jobRepository,
                                 @Qualifier("PackItemStep") Step packItemStep,
                                 @Qualifier("ShippingPackageStep") Step shippingPackageStep,
                                 @Qualifier("DeliveryPackToCustomerStep") Step deliveryPackToCustomerStep,
                                 @Qualifier("DeliverySuccessStep") Step deliverySuccessStep,
                                 @Qualifier("StorePackBackStep") Step storePackBackStep,
                                 @Qualifier("PlanAgainDeliveryStep") Step planAgainDeliveryStep,
                                 @Qualifier("RefundStep") Step refundStep,
                                 @Qualifier("ThanksCustomerStep") Step thanksCustomerStep) {
        return new JobBuilder("DeliveryJob", jobRepository)
                .start(packItemStep)// 01 preparing the package
                .next(shippingPackageStep) // 02 sending the package
                // 03a if the package has been lost
                // 03b else
                // 04a if the customer is in home
                // 04b else
                // 05a if the customer reject the package
                // 05b else
                .next(deliveryPackToCustomerStep)
                    // con il fail() posso istruire spring a far fallire l'esecuzione e quindi a riprovare il batch
                    // simile al "fail" c'è lo stop().
                    // lo stop() e' simile al fail, ma in questo caso cambia lo stato finale del job-instance.
                    .on("FAILED").fail()//.to(storePackBackStep).next(planAgainDeliveryStep)
                .from(deliveryPackToCustomerStep)
                    .on("*")
                        .to(deliveryDecider)
                            .on("DELIVERY.FAILURE").to(planAgainDeliveryStep)
                        .from(deliveryDecider)
                            .on("DELIVERY.SUCCESS")
                                .to(deliverySuccessStep)
                                .next(acceptRefusePackageDecider).on("DELIVERY.ACCEPTED.FALSE").to(refundStep)
                                .from(acceptRefusePackageDecider).on("DELIVERY.ACCEPTED.TRUE").to(thanksCustomerStep)
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

    @Bean("RefundStep")
    public Step createRefoundStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("RefundStep", jobRepository)
                .tasklet(new RefundTasklet(), transactionManager)
                .build();
    }

    @Bean("ThanksCustomerStep")
    public Step createThanksCustomerStep(JobRepository jobRepository, JdbcTransactionManager transactionManager) {
        return new StepBuilder("ThanksCustomerStep", jobRepository)
                .tasklet(new ThanksCustomerTasklet(), transactionManager)
                .build();
    }
}
