package com.example.spring.batch.app.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

import static com.example.spring.batch.app.AppConstants.*;
import static com.example.spring.batch.app.AppConstants.ANONYMIZE_DATA;

@Service
@Slf4j
public class MyJobLauncher {
    @Value("${watcher.uploadPath:/home/dcividin/Scrivania/test/upload_watched}")
    private String watchedUploadPath;
    @Value("${watcher.inputPath:/home/dcividin/Scrivania/test/input}")
    private String inputPath;
    @Value("${watcher.outputPath:/home/dcividin/Scrivania/test/output}")
    private String outputPath;
    @Value("${watcher.outputPath:/home/dcividin/Scrivania/test/error}")
    private String errorPath;
    private final Job job;
    private final JobLauncher launcher;

    public MyJobLauncher(Job job, JobLauncher launcher) {
        this.job = job;
        this.launcher = launcher;
    }

    @PostConstruct
    void setUp() {
        log.info("The watch upload Path {}", watchedUploadPath);
        log.info("The Input Path {}", inputPath);
        log.info("The Output Path {}", outputPath);
        log.info("The Error Path {}", errorPath);
    }

    public void runJob(File file) {
        log.info("Received file {}", file);
        JobParameters parameters = new JobParametersBuilder()
                .addString(INPUT_PATH, inputPath)
                .addString(INPUT_FILE_NAME, file.getName())
                .addString(OUTPUT_PATH, outputPath)
                .addString(UPLOAD_FILE_PATH, file.toString())
                .addString(ERROR_PATH, errorPath)
                .addString(ANONYMIZE_DATA, "true")
                .toJobParameters();

        try {
            launcher.run(job, parameters);
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("the Job has been already started");
        } catch (JobRestartException e) {
            log.warn("unable to restart the job");
        } catch (JobInstanceAlreadyCompleteException e) {
            log.warn("the Job is already completed");
        } catch (JobParametersInvalidException e) {
            log.warn("the Job parameters are invalid");
        }
    }
}
