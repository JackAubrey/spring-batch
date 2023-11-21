package com.example.spring.batch.app;

public class AppConstants {
    // the path where we will look for the uploaded file
    public static final String INPUT_PATH = "inputPath";
    // the path where we will write the processed file
    public static final String OUTPUT_PATH = "outputPath";
    // the name of the file after its import from upload
    public static final String INPUT_FILE_NAME = "inputFileName";
    // the path of file uploaded
    public static final String UPLOAD_FILE_PATH = "uploadFilePath";
    // the path where we will write the processing error data
    public static final String ERROR_PATH = "errorPath";
    public static final String ANONYMIZE_DATA = "anonymizeData";

    private static final String JOB_PARAMS_REF_OPEN = "#{jobParameters['";
    private static final String JOB_PARAMS_REF_CLOSE = "']}";

    public static final String JOB_PARAM_INPUT_PATH_REF = JOB_PARAMS_REF_OPEN + INPUT_PATH + JOB_PARAMS_REF_CLOSE;
    public static final String JOB_PARAM_INPUT_FILE_NAME_REF = JOB_PARAMS_REF_OPEN + INPUT_FILE_NAME + JOB_PARAMS_REF_CLOSE;
    public static final String JOB_PARAM_OUTPUT_PATH_REF = JOB_PARAMS_REF_OPEN + OUTPUT_PATH + JOB_PARAMS_REF_CLOSE;
    public static final String JOB_PARAM_ANONYMIZE_DATA_REF = JOB_PARAMS_REF_OPEN + ANONYMIZE_DATA + JOB_PARAMS_REF_CLOSE;

    /**
     *
     */
    private AppConstants() {
        super();
    }
}
