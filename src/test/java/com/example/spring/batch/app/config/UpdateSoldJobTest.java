package com.example.spring.batch.app.config;

import com.example.spring.batch.app.listener.FileHandlingJobExecutionListener;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;

import static com.example.spring.batch.app.AppConstants.*;
import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
class UpdateSoldJobTest {
    private static final String uploadFilePath = "/home/dcividin/git/courses/spring-batch/external_resources/upload/upload.json";
    private static final String inputPath = "/home/dcividin/git/courses/spring-batch/external_resources/input";
    private static final String inputFile = "data.json";
    private static final String outputPath = "/home/dcividin/git/courses/spring-batch/external_resources/output";
    private static final String errorPath = "/home/dcividin/git/courses/spring-batch/external_resources/errors";


    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    JobRepositoryTestUtils jobRepositoryTestUtils;

    @MockBean
    FileHandlingJobExecutionListener listener;

    @BeforeEach
    public void setUp() {
        this.jobRepositoryTestUtils.removeJobExecutions();
        FileUtils.deleteQuietly(new File(inputPath, inputPath));
    }

    @Test
    void testAlterFilter() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString(INPUT_PATH, inputPath)
                .addString(INPUT_FILE_NAME, inputFile)
                .addString(OUTPUT_PATH, outputPath)
                .addString(UPLOAD_FILE_PATH, uploadFilePath)
                .addString(ERROR_PATH, errorPath)
                .addString(ANONYMIZE_DATA, "false")
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        String fileContent = contentOf(new File(outputPath, inputFile));
        assertTrue(fileContent.contains("John Stoltenberg I"));
        assertFalse(fileContent.contains("CLIENTE NON FIDELITY"));

        Mockito.verify(listener).beforeJob(execution);
        Mockito.verify(listener).afterJob(execution);
    }

    @Test
    void testAnonymize() throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addString(INPUT_PATH, inputPath)
                .addString(INPUT_FILE_NAME, inputFile)
                .addString(OUTPUT_PATH, outputPath)
                .addString(UPLOAD_FILE_PATH, uploadFilePath)
                .addString(ERROR_PATH, errorPath)
                .addString(ANONYMIZE_DATA, "true")
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(parameters);

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
        String fileContent = contentOf(new File(outputPath, inputFile));
        assertTrue(fileContent.contains("J.S.I."));
        assertFalse(fileContent.contains("John Stoltenberg I"));
        assertFalse(fileContent.contains("CLIENTE NON FIDELITY"));

        Mockito.verify(listener).beforeJob(execution);
        Mockito.verify(listener).afterJob(execution);
    }
}