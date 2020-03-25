package com.loan.asyncTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @description:
 * @author:
 * @time: 2019/12/15 23:16
 */
@Component
public class AsyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTask.class);

    @Async
    public Future<String> doTask1() throws InterruptedException {
        LOGGER.info("Task1 started.");
        long start = System.currentTimeMillis();
        Thread.sleep(5000);
        long end = System.currentTimeMillis();
        LOGGER.info("Task1 finished, time elapsed: {} ms.", end - start);
        return new AsyncResult<>("Task1 accomplished!");
    }
}
