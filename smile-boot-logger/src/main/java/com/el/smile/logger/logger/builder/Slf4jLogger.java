package com.el.smile.logger.logger.builder;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.el.smile.logger.config.SmileLoggerConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * slf4j logger构造器 <br/>
 * since 2020/9/14
 *
 * @author eddie.lys
 */
public class Slf4jLogger extends BaseLoggerBuilder {

    private static final String ROLLING_PATTERN = ".%d{yyyy-MM-dd}.%i";

    public static Slf4jLogger builder() {
        return new Slf4jLogger();
    }

    @Override
    public Logger build(LoggerType loggerType) {
        String logFile = buildLogPath();

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = (Logger) LoggerFactory.getLogger(this.getLoggerName());

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(this.getLoggerPattern());
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.start();

        // rollingAppender
        RollingFileAppender<ILoggingEvent> rollingAppender = new RollingFileAppender<>();
        rollingAppender.setContext(context);
        rollingAppender.setEncoder(encoder);
        String logFilePath = logFile + File.separator + loggerType.name().toLowerCase() + ".log";
        rollingAppender.setFile(logFilePath);

        // 滚动策略
        SizeAndTimeBasedRollingPolicy<?> rollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
        rollingPolicy.setContext(context);
        rollingPolicy.setFileNamePattern(logFile + ROLLING_PATTERN);
        rollingPolicy.setMaxHistory(this.getMaxHistoryWithDefault());
        rollingPolicy.setMaxFileSize(FileSize.valueOf(this.getMaxFileSizeWithDefault() + "mb"));
        rollingPolicy.setTotalSizeCap(FileSize.valueOf(this.getTotalSizeCapWithDefault() + "mb"));
        rollingPolicy.setParent(rollingAppender);
        rollingPolicy.start();

        rollingAppender.setRollingPolicy(rollingPolicy);
        rollingAppender.start();

        // 异步Appender
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(context);
        asyncAppender.setName(this.getLoggerName());

        asyncAppender.setQueueSize(512);
        asyncAppender.setDiscardingThreshold(0);

        asyncAppender.addAppender(rollingAppender);
        asyncAppender.start();

        logger.setLevel(this.getLoggerLevel());
        logger.addAppender(asyncAppender);

        return logger;
    }

    private String buildLogPath() {
        String logBasePath = this.getLoggerPath();
        int index = StringUtils.indexOf(logBasePath, ":") + 1;
        String logPath = StringUtils.trim(StringUtils.substring(logBasePath, index));
        if (StringUtils.startsWith(logBasePath, SmileLoggerConstants.RELATIVE)) {
            String property = System.getProperty("user.dir");
            if (StringUtils.startsWith(logPath, File.separator)) {
                return property.concat(logPath);
            }
            return property.concat(File.separator).concat(logPath);
        }else if (StringUtils.startsWith(logBasePath, SmileLoggerConstants.ABSOLUTE)) {
            if (StringUtils.startsWith(logPath, File.separator)) {
                return logPath;
            }
            return File.separator.concat(logPath);
        }

        throw new IllegalArgumentException("smile-boot logger config error: log path illegal");
    }


}
