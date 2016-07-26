/*
 * Copyright 2006-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.professor.chaos.service;

import com.consol.professor.chaos.exception.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author Christoph Deppisch
 */
@Service
public class ChaosService {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(ChaosService.class);

    /**
     * Loads data into Java heap for given amount of time.
     * @param size
     * @param time
     */
    public void createHeapUsage(Long size, Long time) {
        log.info(String.format("Creating memory usage %s mb for %s ms", size / 1024, time));

        Long currentSize = 0L;
        byte[] buffer = new byte[1024];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = Byte.valueOf((i % 2 == 0) ? "0" : "1");
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while (currentSize < size) {
                    bos.write(buffer);
                currentSize += 1024;
                log.debug("Loading data: " + currentSize);
            }

            Future future = new CompletableFuture();
            try {
                future.get(time, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("Failed to allocate heap memory for time: " + time);
            } catch (TimeoutException e) {
                // do nothing
            }
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Failed to allocate heap memory: " + currentSize);
        }

        log.info("Finished memory usage");
    }

    /**
     * Create CPU usage by creating several threads with calculating some stuff.
     * @param threads
     * @param keepAliveTime
     */
    public void createCpuUsage(Integer threads, Long keepAliveTime) {
        log.info(String.format("Creating %s threads consuming CPU for %s ms", threads, keepAliveTime));

        ExecutorService executorService = new ThreadPoolExecutor(threads, threads, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(threads, true));
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                while(!executorService.isShutdown()) {
                    Math.tan(Math.atan(Math.tan(Math.random() * 1000)));
                }

                log.info(Thread.currentThread().getName() + " terminating");
            });
        }

        try {
            executorService.awaitTermination(keepAliveTime, TimeUnit.MILLISECONDS);
            executorService.shutdownNow();

            log.info(String.format("All %s threads shut down", threads));
        } catch (InterruptedException e) {
            throw new ApplicationRuntimeException(e);
        }
    }
}
