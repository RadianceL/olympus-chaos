package com.el.smile.logger.logger;

import com.el.smile.logger.logger.builder.LoggerType;
import com.el.smile.logger.logger.builder.Slf4jLogger;
import ch.qos.logback.classic.Logger;

/**
 * 基础日志类
 * since 7/4/20
 *
 * @author eddie
 */
public class Slf4jEventLoggerFactory extends Slf4jLogger {

    @Override
    public Logger buildLogger() {
        return super.build(LoggerType.EVENT);
    }

}
