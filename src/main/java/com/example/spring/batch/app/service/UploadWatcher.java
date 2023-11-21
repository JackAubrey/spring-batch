package com.example.spring.batch.app.service;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class UploadWatcher {
    private static final String WATCH_FOLDER = "/home/dcividin/Scrivania/test/upload_watched";

    private final MyJobLauncher myJobLauncher;

    public UploadWatcher(MyJobLauncher myJobLauncher) {
        this.myJobLauncher = myJobLauncher;
    }

    @EventListener(value = ApplicationReadyEvent.class)
    void setUp() {
        triggerJob();
        createWatcher();
    }

    @SneakyThrows
    private void createWatcher() {
        FileAlterationObserver observer = new FileAlterationObserver(new File(WATCH_FOLDER));
        FileAlterationMonitor monitor = new FileAlterationMonitor(5000);

        observer.addListener(new FileAlterationListenerAdaptor(){

            /**
             * File created Event.
             *
             * @param file The file created (ignored)
             */
            @Override
            public void onFileCreate(File file) {
                myJobLauncher.runJob(file);
            }
        });

        monitor.addObserver(observer);
        monitor.start();
    }

    private void triggerJob() {
        FileUtils.listFiles(new File(WATCH_FOLDER), new String[]{"*.json"}, false)
                .forEach( myJobLauncher::runJob);
    }
}
