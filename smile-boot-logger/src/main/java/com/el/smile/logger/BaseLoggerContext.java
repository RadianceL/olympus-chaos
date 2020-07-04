package com.el.smile.logger;

import lombok.Data;

/**
 * 日志基础上下文
 * since 6/29/20
 *
 * @author eddie
 */
@Data
public class BaseLoggerContext {

    private String traceId;

    private String ip;

    private String env;

    private String scene;
}
