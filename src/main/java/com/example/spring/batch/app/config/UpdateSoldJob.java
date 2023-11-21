package com.example.spring.batch.app.config;

import com.example.spring.batch.app.AppConstants;
import com.example.spring.batch.app.listener.FileHandlingJobExecutionListener;
import com.example.spring.batch.app.model.Sales;
import com.example.spring.batch.app.validator.AppJobParametersValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import java.io.File;

@Configuration
@Slf4j
public class UpdateSoldJob {
    public Job job(JobRepository repository, FileHandlingJobExecutionListener listener,
                   Step step01) {
        return new JobBuilder("update-sold", repository)
                .start(step01)
                .listener(listener)
                .validator(new AppJobParametersValidator())
                .build();
    }

    @Bean
    public Step step01(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("first-step", repository)
                .<Sales, Sales>chunk(1, transactionManager)
                .reader(reader(null, null))
                .processor(processor(null))
                .writer(writer(null, null))
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Sales> reader(
            @Value(AppConstants.JOB_PARAM_INPUT_PATH_REF) String inputPath,
            @Value(AppConstants.JOB_PARAM_OUTPUT_FILE_NAME_REF) String fileName
    ) {
        FileSystemResource resource = new FileSystemResource(new File(inputPath, fileName));

        return new JsonItemReaderBuilder<Sales>()
                .name("input-item-reader")
                .resource(resource)
                .jsonObjectReader(new JacksonJsonObjectReader<>(Sales.class))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Sales, Sales> processor(
            @Value(AppConstants.JOB_PARAM_ANONYMIZE_DATA_REF) String anonymize
    ) {
        return input -> {
            log.info("ItemProcessor | CodFid {}", input.getCode());
            Assert.hasText(input.getCode(), "Item Code must not be null or empty");
            Sales output = new Sales();

            if( "-1".equalsIgnoreCase(input.getCode().trim()) ) {
                log.warn("Not fidelity code! This item will be discarded");
                return null;
            }

            if(input.isMobile()) {
                log.info("Found a billing mobile user {}. Adding 200 extra points ", input.getName());
                output.setPoints( input.getPoints()+200 );
            }

            if(StringUtils.isNotBlank(anonymize) &&
                    "true".equalsIgnoreCase(anonymize.trim()) ) {
                log.info("ItemProcessor | Anonymizing | going to anonymize data | CodFid {}", input.getCode());
                output.setName(input.getName().trim().replaceAll("\\w", "X"));
            } else {
                log.info("ItemProcessor | Anonymizing | not required | CodFid {}", input.getCode());
                output.setName(input.getName().trim());
            }

            output.setCode(input.getCode());
            output.setCost(input.getCost());
            output.setDate(input.getDate());
            output.setMobile(input.isMobile());

            return input;
        };
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Sales> writer(
            @Value(AppConstants.JOB_PARAM_OUTPUT_PATH_REF) String outputPath,
            @Value(AppConstants.JOB_PARAM_OUTPUT_FILE_NAME_REF) String fileName
    ) {
        FileSystemResource resource = new FileSystemResource(outputPath);

        return new JsonFileItemWriterBuilder<Sales>()
                .name("output-item-writer")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(resource)
                .build();
    }
}
