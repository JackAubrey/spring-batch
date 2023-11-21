package com.example.spring.batch.app;

public class AppConstants {
    public static final String INPUT_PATH = "inputPath";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String OUTPUT_FILE_NAME = "outputFileName";
    public static final String UPLOAD_FILE_PATH = "uploadFilePath";
    public static final String ERROR_PATH = "errorPath";
    public static final String ANONYMIZE_DATA = "anonymizeData";

    private static final String JOB_PARAMS_REF = "#{jobParameters['%s']}";

    public static final String JOB_PARAM_INPUT_PATH_REF = String.format(JOB_PARAMS_REF, INPUT_PATH);
    public static final String JOB_PARAM_OUTPUT_PATH_REF = String.format(JOB_PARAMS_REF, OUTPUT_PATH);
    public static final String JOB_PARAM_OUTPUT_FILE_NAME_REF = String.format(JOB_PARAMS_REF, OUTPUT_FILE_NAME);
    public static final String JOB_PARAM_ANONYMIZE_DATA_REF = String.format(JOB_PARAMS_REF, ANONYMIZE_DATA);

    /**
     *
     */
    private AppConstants() {
        super();
    }
}
