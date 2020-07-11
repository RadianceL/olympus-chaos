package com.el.smile.logger.event.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Map<String, Object> features;

    public void addFeature(String key, Object value) {
        if (Objects.isNull(features)) {
            features = new HashMap<>(8);
        }
        this.features.put(key, value);
    }
}
