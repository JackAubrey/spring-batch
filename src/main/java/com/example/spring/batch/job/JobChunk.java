package com.example.spring.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JobChunk {
    @Bean
    public Job job(JobRepository jobRepository,
                   @Qualifier("ChunkBasedStep") Step chunkBasedStep) {
        return new JobBuilder("job-test-chunk", jobRepository)
                .start(chunkBasedStep)
                .build();
    }

    @Bean("ChunkBasedStep")
    public Step chunkBasedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                               @Qualifier("SimpleItemReader") ItemReader<String> itemReader,
                               @Qualifier("SimpleSysOutWriter") ItemWriter<String> itemWriter) {
        return new StepBuilder("ChunkBasedStep", jobRepository)
                .<String, String>chunk(3, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }
}
