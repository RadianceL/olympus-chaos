package com.el.smile.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * since 7/11/20
 *
 * @author eddie
 */
@Slf4j
@Data
@Component
@ConfigurationProperties("spring.smile-boot")
public class SmileBootProperties {

    @NestedConfigurationProperty
    private final TraceLoggerConfig traceLogger = new TraceLoggerConfig();

    @Data
    public static class TraceLoggerConfig {
        /**
         * 日志路径 支持classPath
         */
        private String logPath;
        /**
         * 日志文件名称
         */
        private String logFileName;
        /**
         * 日志格式 <br/>
         * 默认："%d{yyyy-MM-dd HH:mm:ss} - %msg%n
         */
        private String pattern;
        /**
         * 最大历史记录 默认7天
         */
        private Integer maxHistory;
        /**
         * 单个文件最大容量 默认300MB
         */
        private Integer maxFileSize;
        /**
         * 总大小限制 默认2G
         */
        private Integer totalSizeCap;
    }
}
