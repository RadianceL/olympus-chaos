package com.olympus.logger.logger.builder;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * since 7/4/20
 *
 * @author eddie
 */
@NoArgsConstructor
public class BaseLoggerBuilder {

    private String name;

    private String path;

    private Level level;

    private String pattern;

    private Integer maxHistory;

    private Integer maxFileSize;

    private Integer totalSizeCap;
    /**
     * 是否为本地环境
     */
    private Boolean isLocalEnv;

    private static final Integer DEFAULT_MAX_HISTORY = 7;

    private static final Integer DEFAULT_MAX_FILE_SIZE = 300;

    private static final Integer DEFAULT_TOTAL_SIZE_CAP = 2 * 1024;

    public BaseLoggerBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BaseLoggerBuilder path(String path) {
        this.path = path;
        return this;
    }

    public BaseLoggerBuilder isLocalEnv(boolean isLocalEnv) {
        this.isLocalEnv = isLocalEnv;
        return this;
    }

    public BaseLoggerBuilder level(Level level) {
        this.level = level;
        return this;
    }

    public BaseLoggerBuilder pattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public BaseLoggerBuilder maxHistory(Integer maxHistory) {
        this.maxHistory = maxHistory;
        return this;
    }

    public BaseLoggerBuilder maxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    public BaseLoggerBuilder totalSizeCap(Integer totalSizeCap) {
        this.totalSizeCap = totalSizeCap;
        return this;
    }

    public String getLoggerName() {
        if (StringUtils.isBlank(this.name)) {
           throw new RuntimeException("logger name can`t be blank");
        }
        return this.name;
    }

    public String getLoggerPath() {
        if (StringUtils.isBlank(this.path)) {
            throw new RuntimeException("logger path can`t be blank");
        }
        return this.path;
    }

    public String getLoggerPattern() {
        if (StringUtils.isBlank(this.pattern)) {
            throw new RuntimeException("logger pattern can`t be blank");
        }
        return this.pattern;
    }

    public Level getLoggerLevel() {
        if (Objects.isNull(this.level)) {
           return Level.TRACE;
        }
        return this.level;
    }

    public Boolean isLocalEnv() {
        if (Objects.isNull(this.isLocalEnv)) {
            return false;
        }
        return this.isLocalEnv;
    }

    public Integer getMaxHistoryWithDefault() {
        if (Objects.isNull(this.maxHistory)) {
            return DEFAULT_MAX_HISTORY;
        }
        return this.maxHistory;
    }

    public Integer getMaxFileSizeWithDefault() {
        if (Objects.isNull(this.maxFileSize)) {
            return DEFAULT_MAX_FILE_SIZE;
        }
        return this.maxFileSize;
    }
    public Integer getTotalSizeCapWithDefault() {
        if (Objects.isNull(this.totalSizeCap)) {
            return DEFAULT_TOTAL_SIZE_CAP;
        }
        return this.totalSizeCap;
    }

    public Logger build(LoggerType loggerType) {
        throw new RuntimeException("logger builder must override #build()");
    }

    public Logger buildLogger() {
        throw new RuntimeException("logger builder must override #buildLogger()");
    }
}
