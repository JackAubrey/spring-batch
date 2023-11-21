package com.example.spring.batch.app.listener;

import com.example.spring.batch.app.AppConstants;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlingJobExecutionListenerImplTest {
    private FileHandlingJobExecutionListener listener;

    @BeforeEach
    void setUp() {
        listener = new FileHandlingJobExecutionListenerImpl();
    }

    @Test
    void testBeforeJobFail_when_uploaded_file_is_not_set() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Upload path can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_uploaded_file_is_empty() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, "")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Upload path can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_input_file_is_not_set() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, "/uploaded/foo.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Input path can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_input_file_is_empty() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, "/uploaded/foo.json")
                .addString(AppConstants.INPUT_PATH, "")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Input path can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_output_filename_is_not_set() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, "/uploaded/foo.json")
                .addString(AppConstants.INPUT_PATH, "/input")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Output file-name can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_output_filename_is_empty() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, "/uploaded/foo.json")
                .addString(AppConstants.INPUT_PATH, "/input")
                .addString(AppConstants.OUTPUT_FILE_NAME, "")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("The Output file-name can not be null or empty", errMsg);
    }

    @Test
    void testBeforeJobFail_when_uploadPath_not_exists() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        String uploadPath = "/uploaded/foo.json";
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, uploadPath)
                .addString(AppConstants.INPUT_PATH, "/input")
                .addString(AppConstants.OUTPUT_FILE_NAME, "bar.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("the uploaded file ["+uploadPath+"] not exits or is not a file", errMsg);
    }

    @Test
    void testBeforeJobFail_when_uploadPath_is_not_a_file() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        String uploadPath = "/home/dcividin/git/courses/spring-batch/external_resources/upload";
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, uploadPath)
                .addString(AppConstants.INPUT_PATH, "/input")
                .addString(AppConstants.OUTPUT_FILE_NAME, "bar.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("the uploaded file ["+uploadPath+"] not exits or is not a file", errMsg);
    }

    @Test
    void testBeforeJobFail_when_inputPath_not_exists() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        String uploadPath = "/home/dcividin/git/courses/spring-batch/external_resources/upload/upload.json";
        String inputPath = "/foo/dir";
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, uploadPath)
                .addString(AppConstants.INPUT_PATH, inputPath)
                .addString(AppConstants.OUTPUT_FILE_NAME, "bar.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("the input path ["+inputPath+"] not exits or is not a directory", errMsg);
    }

    @Test
    void testBeforeJobFail_when_inputPath_is_not_a_dir() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        String uploadPath = "/home/dcividin/git/courses/spring-batch/external_resources/upload/upload.json";
        String inputPath = "/home/dcividin/git/courses/spring-batch/external_resources/input/data.json";
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, uploadPath)
                .addString(AppConstants.INPUT_PATH, inputPath)
                .addString(AppConstants.OUTPUT_FILE_NAME, "bar.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        // when
        String errMsg = assertThrows( IllegalArgumentException.class, () -> listener.beforeJob(execution)).getLocalizedMessage();

        // then
        assertEquals("the input path ["+inputPath+"] not exits or is not a directory", errMsg);
    }

    @Test
    void testBeforeJob() {
        // given
        JobInstance jobInstance = new JobInstance(1L, "foo-job" );
        String uploadPath = "/home/dcividin/git/courses/spring-batch/external_resources/upload";
        String inputPath = "/home/dcividin/git/courses/spring-batch/external_resources/input";
        JobParameters params = new JobParametersBuilder()
                .addString(AppConstants.UPLOAD_FILE_PATH, uploadPath)
                .addString(AppConstants.INPUT_PATH, inputPath)
                .addString(AppConstants.OUTPUT_FILE_NAME, "bar.json")
                .toJobParameters();
        JobExecution execution = new JobExecution(jobInstance, 1L, params);

        try (MockedStatic<FileUtils> utilities = Mockito.mockStatic(FileUtils.class)) {
            assertDoesNotThrow( () -> utilities.when( () -> FileUtils.moveFile(any(), any())).then(invocationOnMock -> null) );
        }
    }
}