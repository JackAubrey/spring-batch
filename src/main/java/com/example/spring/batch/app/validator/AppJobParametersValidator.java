package com.example.spring.batch.app.validator;

import com.example.spring.batch.app.AppConstants;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

public class AppJobParametersValidator extends DefaultJobParametersValidator {
    private static final String [] REQUIRED_KEYS = {
            AppConstants.INPUT_PATH,
            AppConstants.INPUT_FILE_NAME,
            AppConstants.OUTPUT_PATH,
            AppConstants.UPLOAD_FILE_PATH,
            AppConstants.ERROR_PATH
    };

    private static final String [] OPTIONAL_KEYS = {
            AppConstants.ANONYMIZE_DATA
    };

    public AppJobParametersValidator() {
        super(REQUIRED_KEYS, OPTIONAL_KEYS);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        super.validate(parameters);

        String sInputPath = parameters.getString(AppConstants.INPUT_PATH);
        String sInputFileName = parameters.getString(AppConstants.INPUT_FILE_NAME);
        String sOutputPath = parameters.getString(AppConstants.OUTPUT_PATH);
        String sErrorPath = parameters.getString(AppConstants.ERROR_PATH);
        String sUploadPath = parameters.getString(AppConstants.UPLOAD_FILE_PATH);

        if( StringUtils.isBlank(sInputPath) ) {
            throw new JobParametersInvalidException("The Input path can not null or empty");
        }

        if( StringUtils.isBlank(sInputFileName) ) {
            throw new JobParametersInvalidException("The Input file can not null or empty");
        }

        String extension = FilenameUtils.getExtension(sInputFileName);

        if(StringUtils.isBlank(extension) || !"json".equalsIgnoreCase(extension)) {
            throw new JobParametersInvalidException("The input file ["+sInputFileName+"] must be a JSON file");
        }

        if( StringUtils.isBlank(sOutputPath) ) {
            throw new JobParametersInvalidException("The Output path can not null or empty");
        }

        if( StringUtils.isBlank(sUploadPath) ) {
            throw new JobParametersInvalidException("The Uploaded file path can not null or empty");
        }

        if( StringUtils.isBlank(sErrorPath) ) {
            throw new JobParametersInvalidException("The Error path can not null or empty");
        }

        String sAnonymize = parameters.getString(AppConstants.ANONYMIZE_DATA);

        if( StringUtils.isNotBlank(sAnonymize) &&
                !("true".equalsIgnoreCase(sAnonymize.trim()) || "false".equalsIgnoreCase(sAnonymize.trim()))) {
            throw new JobParametersInvalidException("The anonymize value ["+sAnonymize+"] must be a boolean value");
        }
    }
}
