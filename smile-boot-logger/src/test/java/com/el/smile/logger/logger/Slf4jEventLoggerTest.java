package com.el.smile.logger.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * since 7/4/20
 *
 * @author eddie
 */
class Slf4jEventLoggerTest {

    @Test
    void build() throws InterruptedException {
        Logger logger = Slf4jEventLogger.builder()
                .level(Level.DEBUG)
                .path("/Users/eddie/Documents/web-app/github/smile-boot")
                .name("event")
                .pattern("%d{yyyy-MM-dd HH:mm:ss} - %msg%n").build();

        logger.error("test");
        TimeUnit.MILLISECONDS.sleep(1);
    }
}