package com.example.spring.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class DeliveryJob {
    private final DeliveryDecider deliveryDecider;

    public DeliveryJob(DeliveryDecider deliveryDecider) {
        this.deliveryDecider = deliveryDecider;
    }

    @Bean
    public Job prepareFlowers(
            JobRepository jobRepository,
            @Qualifier("SelectFlowersStep") Step selectFlowersStep,
            @Qualifier("RemoveThornsStep") Step removeThornsStep,
            @Qualifier("SendFlowersStep") Step sendFlowersStep,
            @Qualifier("DeliveryFlow") Flow deliveryFlow) {
        return new JobBuilder("PrepareFlowersJob", jobRepository)
                .start(selectFlowersStep)
                    .on("TRIM_REQUIRED").to(removeThornsStep).next(sendFlowersStep)
                .from(selectFlowersStep)
                    .on("NO_TRIM_REQUIRED").to(sendFlowersStep)
                .from(sendFlowersStep).on("*").to(deliveryFlow)
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

    @Bean("DeliveryFlow")
    public Flow deliveryFlow(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                             @Qualifier("DriveToAddressStep") Step driveToAddressStep,
                             @Qualifier("GivePackageToCustomerStep") Step givePackageToCustomerStep,
                             @Qualifier("LeaveAtDoorStep") Step leaveAtDoorStep) {
        return new FlowBuilder<SimpleFlow>("DeliveryFlow")
                .start(driveToAddressStep)
                    .on("FAILED").fail()
                .from(driveToAddressStep)
                    .on("COMPLETED").to(deliveryDecider)
                        .on("PRESENT").to(givePackageToCustomerStep)
                    .from(deliveryDecider)
                        .on("NOT_PRESENT").to(leaveAtDoorStep)
                .end();
    }

    @Bean("LeaveAtDoorStep")
    public Step leaveAtDoorStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("leaveAtDoorStep", jobRepository)
                .tasklet( (contribution, context) -> {
                    System.out.println("Cliente Assente. Prodotto lasciato sulla porta di ingresso");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean("GivePackageToCustomerStep")
    public Step givePackageToCustomerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("GivePackageToCustomerStep", jobRepository)
                .tasklet( (contribution, context) -> {
                    System.out.println("Il prodotto è stato consegnato al cliente");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean("DriveToAddressStep")
    public Step driveToAddressStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("DriveToAddressStep", jobRepository)
                .tasklet( (contribution, context) -> {
                    JobParameters parameters = contribution.getStepExecution().getJobParameters();
                    String driveToFails = parameters.getString("input.simulate.drive-to-address.fails");
                    System.out.println("Drive To Address fails ? "+driveToFails);

                    if("true".equalsIgnoreCase(driveToFails)) {
                        throw new RuntimeException("Indirizzo Cliente non Trovato!");
                    } else {
                        System.out.println("Il prodotto è arrivato all'indirizzo del cliente");
                        return RepeatStatus.FINISHED;
                    }
                }, transactionManager)
                .build();
    }
}
