package com.example.spring.batch.app.validator;

import com.example.spring.batch.app.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppJobParametersValidatorTest {
    private AppJobParametersValidator validator;
    @BeforeEach
    void init() {
        validator = new AppJobParametersValidator();
    }

    @Test
    void testFail_when_missing_input_file() {
        // given
        Map<String, JobParameter<?>> map = new HashMap<>();
        map.put(AppConstants.INPUT_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.OUTPUT_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.UPLOAD_FILE_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.ERROR_PATH, new JobParameter<>("", String.class));
        JobParameters params = new JobParameters(map);

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The Input file can not null or empty", errMsg);
    }

    @Test
    void testFail_when_input_file_is_not_json() {
        // given
        String sInputPath = "/foo/bar/file.doc";
        Map<String, JobParameter<?>> map = new HashMap<>();
        map.put(AppConstants.INPUT_PATH, new JobParameter<>(sInputPath, String.class));
        map.put(AppConstants.OUTPUT_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.UPLOAD_FILE_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.ERROR_PATH, new JobParameter<>("", String.class));
        JobParameters params = new JobParameters(map);

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The input file ["+sInputPath+"] must be a JSON file", errMsg);
    }

    @Test
    void testFail_when_anonymize_is_not_a_boolean() {
        // given
        String sInputPath = "/foo/bar/file.json";
        String sAnonymize = "yesssss";
        Map<String, JobParameter<?>> map = new HashMap<>();
        map.put(AppConstants.INPUT_PATH, new JobParameter<>(sInputPath, String.class));
        map.put(AppConstants.ANONYMIZE_DATA, new JobParameter<>(sAnonymize, String.class));
        map.put(AppConstants.OUTPUT_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.UPLOAD_FILE_PATH, new JobParameter<>("", String.class));
        map.put(AppConstants.ERROR_PATH, new JobParameter<>("", String.class));
        JobParameters params = new JobParameters(map);

        // when
        String errMsg = assertThrows( JobParametersInvalidException.class, () -> validator.validate(params)).getLocalizedMessage();

        // then
        assertEquals("The anonymize value ["+sAnonymize+"] must be a boolean value", errMsg);
    }
}