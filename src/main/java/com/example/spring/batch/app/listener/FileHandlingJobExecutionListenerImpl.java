package com.example.spring.batch.app.listener;

import com.example.spring.batch.app.AppConstants;
import com.example.spring.batch.app.AppRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class FileHandlingJobExecutionListenerImpl implements FileHandlingJobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        String sUploadFilePath = jobExecution.getJobParameters().getString(AppConstants.UPLOAD_FILE_PATH);
        String sInputPath = jobExecution.getJobParameters().getString(AppConstants.INPUT_PATH);
        String sOutputFileName = jobExecution.getJobParameters().getString(AppConstants.OUTPUT_FILE_NAME);
        Assert.hasText(sUploadFilePath, "The Upload path can not be null or empty");
        Assert.hasText(sInputPath, "The Input path can not be null or empty");
        Assert.hasText(sOutputFileName, "The Output file-name can not be null or empty");

        log.info("BeforeJob | try to move the uploaded file [{}] to input destination [{}]", sUploadFilePath, sInputPath);

        Path uploadPath = Paths.get(sUploadFilePath);
        Path inputPath = Paths.get(sInputPath);
        Path destination = inputPath.resolve(sOutputFileName);

        if( !Files.exists(uploadPath) || !Files.isRegularFile(uploadPath) ) {
            throw new IllegalArgumentException("the uploaded file ["+uploadPath+"] not exits or is not a file");
        }

        if( !Files.exists(inputPath) || !Files.isDirectory(inputPath) ) {
            throw new IllegalArgumentException("the input path ["+inputPath+"] not exits or is not a directory");
        }

        try {
            FileUtils.moveFile(uploadPath.toFile(), destination.toFile());
            log.info("BeforeJob | the uploaded file [{}] has been moved onto the destination [{}]", uploadPath, destination);
        } catch (IOException e) {
            throw new AppRuntimeException("unable to move the uploaded file ["+uploadPath+"] onto the destination ["+destination+"]", e);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String sInputPath = jobExecution.getJobParameters().getString(AppConstants.INPUT_PATH);
        String sErrorPath = jobExecution.getJobParameters().getString(AppConstants.ERROR_PATH);
        String sOutputFileName = jobExecution.getJobParameters().getString(AppConstants.OUTPUT_FILE_NAME);
        Assert.hasText(sErrorPath, "The Error path can not be null or empty ");
        Assert.hasText(sInputPath, "The Input path can not be null or empty ");
        Assert.hasText(sOutputFileName, "The Output file-name can not be null or empty ");

        Path inputPath = Paths.get(sInputPath);
        Path destination = inputPath.resolve(sOutputFileName);

        log.info("AfterJob | try to delete the input file [{}]", destination);

        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            if(! FileUtils.deleteQuietly(destination.toFile()) ) {
                log.warn("AfterJob | Unable to delete the source file ["+destination+"]");
            } else {
                log.info("AfterJob | The input file ["+destination+"] has been deleted");
            }
        } else {
            log.warn("AfterJob | Unable to delete the input file ["+sInputPath+"] because the job is not completed");
            File errorPath = new File(sErrorPath);

            try {
                FileUtils.moveFile(destination.toFile(), errorPath);
                log.info("BeforeJob | the input file [{}] has been moved onto the error path [{}]", destination, errorPath);
            } catch (IOException e) {
                throw new AppRuntimeException("unable to move the input file ["+destination+"] onto the error path ["+errorPath+"]", e);
            }
        }
    }
}
