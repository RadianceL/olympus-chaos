package com.el.smile.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

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
         * 是否获取公网IP *可能失效
         */
        private Boolean publicIp;
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

        public String getLogPathIfPresent() {
            return logPath;
        }

        public Boolean getPublicIpIfPresent() {
            if (Objects.isNull(this.publicIp)) {
                return false;
            }
            return publicIp;
        }

        public String getPatternIfPresent() {
            return pattern;
        }

        public Integer getMaxHistoryIfPresent() {
            return maxHistory;
        }

        public Integer getMaxFileSizeIfPresent() {
            return maxFileSize;
        }

        public Integer getTotalSizeCapIfPresent() {
            return totalSizeCap;
        }
    }
}
