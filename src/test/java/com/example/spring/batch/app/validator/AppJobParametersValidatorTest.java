package com.example.spring.batch.app.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;

import static com.example.spring.batch.app.AppConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppJobParametersValidatorTest {
    private AppJobParametersValidator validator;
    @BeforeEach
    void init() {
        validator = new AppJobParametersValidator();
    }

    @Test
    void testFail_when_missing_input_path() {
        // given
        String sInputPath = "";
        String sInputFileName = "";
        String sOutputPath = "";
        String sUploadFilePath = "";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Input path can not null or empty", errMsg);
    }

    @Test
    void testFail_when_missing_input_file_name() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "";
        String sOutputPath = "";
        String sUploadFilePath = "";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Input file can not null or empty", errMsg);
    }

    @Test
    void testFail_when_input_file_is_not_json() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "file.doc";
        String sOutputPath = "";
        String sUploadFilePath = "";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The input file ["+sInputFileName+"] must be a JSON file", errMsg);
    }

    @Test
    void testFail_when_missing_output_path() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "file.json";
        String sOutputPath = "";
        String sUploadFilePath = "";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Output path can not null or empty", errMsg);
    }

    @Test
    void testFail_when_missing_upload_path() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "file.json";
        String sOutputPath = "/output";
        String sUploadFilePath = "";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Uploaded file path can not null or empty", errMsg);
    }

    @Test
    void testFail_when_missing_error_path() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "file.json";
        String sOutputPath = "/output";
        String sUploadFilePath = "/upload/upd.json";
        String sErrorPath = "";
        String sAnonymize = "";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Error path can not null or empty", errMsg);
    }

    @Test
    void testFail_when_anonymize_is_not_a_boolean() {
        // given
        String sInputPath = "/input";
        String sInputFileName = "file.json";
        String sOutputPath = "/output";
        String sUploadFilePath = "/upload/upd.json";
        String sErrorPath = "/error";
        String sAnonymize = "yeah";
        JobParameters params = new JobParametersBuilder()
                .addString(INPUT_PATH, sInputPath)
                .addString(INPUT_FILE_NAME, sInputFileName)
                .addString(OUTPUT_PATH, sOutputPath)
                .addString(ERROR_PATH, sOutputPath)
                .addString(UPLOAD_FILE_PATH, sUploadFilePath)
                .addString(ERROR_PATH, sErrorPath)
                .addString(ANONYMIZE_DATA, sAnonymize)
                .toJobParameters();

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The anonymize value ["+sAnonymize+"] must be a boolean value", errMsg);
    }
}