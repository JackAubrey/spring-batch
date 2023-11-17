package com.example.spring.batch.job;

import com.example.spring.batch.job.model.Cliente;
import com.example.spring.batch.job.model.Cliente2;
import org.slf4j.Logger;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

import static org.slf4j.LoggerFactory.getLogger;

public class CustomRetryListener implements RetryListener {
    private static final Logger logger = getLogger(CustomRetryListener.class);
    /**
     * Called before the first attempt in a retry. For instance, implementers can set up
     * state that is needed by the policies in the {@link RetryOperations}. The whole
     * retry can be vetoed by returning false from this method, in which case a
     * {@link TerminatedRetryException} will be thrown.
     *
     * @param context  the current {@link RetryContext}.
     * @param callback the current {@link RetryCallback}.
     * @return true if the retry should proceed.
     */
    @Override
    public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
        if(context.getRetryCount() > 0) {
            logger.warn("Retry attempt {} | open | retry", context.getRetryCount());
        }
        return true;
    }

    /**
     * Called after every unsuccessful attempt at a retry.
     *
     * @param context   the current {@link RetryContext}.
     * @param callback  the current {@link RetryCallback}.
     * @param throwable the last exception that was thrown by the callback.
     */
    @Override
    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
        if(context.getRetryCount() > 0) {
            logger.warn("Retry attempt {} | onError | error", context.getRetryCount() );
        }
    }
}
