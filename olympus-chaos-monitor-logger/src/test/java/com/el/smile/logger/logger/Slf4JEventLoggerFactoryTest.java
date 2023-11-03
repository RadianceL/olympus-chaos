package com.el.smile.logger.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.olympus.logger.logger.Slf4jTraceLoggerFactory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * since 7/4/20
 *
 * @author eddie
 */
class Slf4JEventLoggerFactoryTest {

    @Test
    void build() throws InterruptedException {
        Logger logger = Slf4jTraceLoggerFactory.builder()
                .level(Level.DEBUG)
                .path("absolute:/Users/eddie.lys/Documents/workspace/utils/smile-boot")
                .name("event")
                .pattern("%d{yyyy-MM-dd HH:mm:ss} - %msg%n").buildLogger();

        logger.error("test");
        TimeUnit.MILLISECONDS.sleep(1);
    }
}