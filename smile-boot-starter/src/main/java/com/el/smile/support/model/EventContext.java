package com.el.smile.support.model;

import com.el.smile.logger.event.model.EventLoggerContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 事件上下文
 * since 7/5/20
 *
 * @author eddie
 */
@Data
public class EventContext {

    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 代理方法
     */
    private String eventMethod;
    /**
     * 返回对象
     */
    private Object resultObj;
    /**
     * 基础log日志内容
     */
    private EventLoggerContext loggerContext;
    /**
     * 用户上下文
     */
    private Map<String, Object> features;

    private EventContext() {}

    public EventContext(String eventName) {
        this.eventName = eventName;
        this.features = new HashMap<>();
        this.loggerContext = new EventLoggerContext();
    }

    public void addFeature(String key, Object value) {
        if (Objects.isNull(this.features)) {
            this.features = new HashMap<>(8);
        }
        this.features.put(key, value);
    }

    public Integer getUserIntegerData(String key) {
        if (Objects.isNull(this.features)) {
            return null;
        }
        Object o = this.features.get(key);
        if (Objects.nonNull(o)) {
            if (o instanceof Integer) {
                return (Integer) o;
            }
        }
        return null;
    }

    public Long getUserLongData(String key) {
        if (Objects.isNull(this.features)) {
            return null;
        }
        Object o = this.features.get(key);
        if (Objects.nonNull(o)) {
            if (o instanceof Long) {
                return (Long) o;
            }
        }
        return null;
    }

    public String getUserStringData(String key) {
        if (Objects.isNull(this.features)) {
            return null;
        }
        Object o = this.features.get(key);
        if (Objects.nonNull(o)) {
            if (o instanceof String) {
                return (String) o;
            }
        }
        return null;
    }
}
