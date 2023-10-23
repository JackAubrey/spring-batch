package com.example.spring.batch.job;

import com.example.spring.batch.job.db.customers.CustomerRepository;
import com.example.spring.batch.job.db.customers.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Configuration
public class DbReadJob {
    @Bean("JpaItemReader")
    @StepScope
    public RepositoryItemReader<Customer> jpaItemReader(
            CustomerRepository repository,
            @Value("#{jobParameters['comune']}") String comune
    ) {
        Map<String, Sort.Direction> sorting = Map.of("nominativo", Sort.Direction.DESC);

        return new RepositoryItemReaderBuilder<Customer>()
                .name("ClientItemJpaReader")
                .arguments(List.of(comune))
                .methodName("findByComune")
                .repository(repository)
                .sorts(sorting)
                .build();
    }

    @Bean("SysOutItemWriter")
    public ItemWriter<Customer> sysOutItemWriter () {
        return new ItemWriter<Customer>() {
            @Override
            public void write(Chunk<? extends Customer> chunk) throws Exception {
                List<? extends Customer> customers = chunk.getItems();
                customers.forEach(System.out::println);
            }
        };
    }

    @Bean("DbReadJob")
    public Job dbReadsJob(JobRepository jobRepository,
                          @Qualifier("DbReadChunkBasedStep")Step dbReadChunkBasedStep) {
        return new JobBuilder("db-read-job", jobRepository)
                .start(dbReadChunkBasedStep)
                .build();
    }

    @Bean("DbReadChunkBasedStep")
    public Step dbReadChunkBasedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                     @Qualifier("JpaItemReader") RepositoryItemReader<Customer> itemReader,
                                     @Qualifier("SysOutItemWriter") ItemWriter<Customer> itemWriter) {
        return new StepBuilder("db-read-chunk-based-step", jobRepository)
                .<Customer, Customer>chunk(3, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }
}
