package com.example.spring.batch.job;

import com.example.spring.batch.job.model.Cliente;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Paths;

@Configuration
public class JobChunk {
    private static final String [] tokens = new String[]{"codeID", "name", "value"};
    @Bean
    public Job job(JobRepository jobRepository,
                   @Qualifier("ChunkBasedStep") Step chunkBasedStep) {
        return new JobBuilder("job-test-chunk", jobRepository)
                .start(chunkBasedStep)
                .build();
    }

    @Bean("ChunkBasedStep")
    public Step chunkBasedStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                               @Qualifier("SimpleCustomerCSVReader") ItemReader<Cliente> itemReader,
                               @Qualifier("SimpleCustomerSysOutWriter") ItemWriter<Cliente> itemWriter) {
        return new StepBuilder("ChunkBasedStep", jobRepository)
                .<Cliente, Cliente>chunk(3, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .build();
    }

    @Bean("SimpleCustomerCSVReader")
    public ItemReader<Cliente> csvItemReader() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(tokens);

        return new FlatFileItemReaderBuilder<Cliente>()
                .name("CustomerCsvReader")
                .linesToSkip(0)
                .resource(new FileSystemResource(Paths.get("/home/dcividin/git/courses/spring-batch/external_resources/DataSet.csv")))
                .delimited()
                .names(tokens)
                .fieldSetMapper((fieldSet) -> {
                    Cliente cliente = new Cliente();
                    cliente.setCodFid(fieldSet.readString("codeID"));
                    cliente.setName(fieldSet.readString("name"));
                    cliente.setPoints(fieldSet.readString("value"));
                    return cliente;
                }).build();
    }
}
