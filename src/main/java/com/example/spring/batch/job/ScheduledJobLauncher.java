package com.example.spring.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
public class ScheduledJobLauncher {
    private final JobLauncher launcher;
    private final Job job;

    public ScheduledJobLauncher(JobLauncher launcher, Job job) {
        this.launcher = launcher;
        this.job = job;
    }

    // BEWARE: in order to prevent the automatic run of this job
    // we have disabled via the application property "spring.batch.job.enabled=false"
    @Scheduled(cron = "0/10 * * * * *")
    public void scheduleJob() {
        JobParametersBuilder parametersBuilder = new JobParametersBuilder();
        parametersBuilder.addDate("runTime", new Date());

        try {
            launcher.run(job, parametersBuilder.toJobParameters());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
