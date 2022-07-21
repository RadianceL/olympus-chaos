package com.el.smile.logger.event.model;

import lombok.Data;

import java.util.Map;

/**
 * 日志基础上下文
 * since 6/29/20
 *
 * @author eddie
 */
@Data
public class EventLoggerContext {
    /**
     * 请求ID
     */
    private String traceId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 当前环境
     */
    private String env;
    /**
     * 方法名
     */
    private String method;
    /**
     * 当前事件
     */
    private String event;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 接口调用RT
     */
    private Long costTime;
    /**
     * 参数
     */
    private String parameter;
    /**
     * 返回值
     */
    private String result;
    /**
     * 错误code
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 异常
     */
    private Exception e;
    /**
     * 扩展字段
     */
    private Map<String, String> features;
}
