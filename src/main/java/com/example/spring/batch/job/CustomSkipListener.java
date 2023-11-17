package com.example.spring.batch.job;

import com.example.spring.batch.job.model.Cliente;
import com.example.spring.batch.job.model.Cliente2;
import org.slf4j.Logger;
import org.springframework.batch.core.SkipListener;

import static org.slf4j.LoggerFactory.getLogger;

public class CustomSkipListener implements SkipListener<Cliente, Cliente2> {
    private static final Logger logger = getLogger(CustomSkipListener.class);

    @Override
    public void onSkipInProcess(Cliente item, Throwable t) {
        logger.warn( String.format("Scartato Cliente con codice %s", item.getCodFid()) );
    }
}
