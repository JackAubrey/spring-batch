package com.example.spring.batch.app.config;

import com.example.spring.batch.app.listener.FileHandlingJobExecutionListener;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.util.UUID;

import static com.example.spring.batch.app.AppConstants.*;
import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringBatchTest
class UpdateSoldJobTest {
    private static final String inputPath = "/home/dcividin/git/courses/spring-batch/external_resources/input";
    private static final String outputPath = "/home/dcividin/git/courses/spring-batch/external_resources/output";
    private static final String destFile = "data.json";


    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    FileHandlingJobExecutionListener listener;

    @Test
    void testAlterFilter() {
        JobParameters parameters = new JobParametersBuilder()
                .addString("id", UUID.randomUUID().toString())
                .addString(JOB_PARAM_INPUT_PATH_REF, inputPath)
                .addString(JOB_PARAM_OUTPUT_PATH_REF, outputPath)
                .addString(JOB_PARAM_OUTPUT_FILE_NAME_REF, destFile)
//                .addString(JOB_PARAM_ANONYMIZE_DATA_REF, UUID.randomUUID().toString())
                .toJobParameters();

        JobExecution execution = assertDoesNotThrow( () -> jobLauncherTestUtils.launchJob(parameters));

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        String fileContent = contentOf(new File(outputPath, destFile));
        assertTrue(fileContent.contains("John Stoltenberg I"));
        assertFalse(fileContent.contains("CLIENTE NON FIDELITY"));

        Mockito.verify(listener).beforeJob(execution);
        Mockito.verify(listener).afterJob(execution);
    }
}