package com.el.smile.logger.logger;

import ch.qos.logback.classic.Logger;
import com.el.smile.logger.logger.builder.LoggerType;
import com.el.smile.logger.logger.builder.Slf4jLogger;

/**
 * 基础日志类
 * since 7/4/20
 *
 * @author eddie
 */
public class Slf4jTraceLoggerFactory extends Slf4jLogger {

    @Override
    public Logger buildLogger() {
        return super.build(LoggerType.TRACE_LOGGER);
    }

}