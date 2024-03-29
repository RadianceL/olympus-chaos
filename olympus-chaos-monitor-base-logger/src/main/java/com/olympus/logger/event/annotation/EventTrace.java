package com.olympus.logger.event.annotation;

import com.olympus.logger.event.model.LoggerType;

import java.lang.annotation.*;

/**
 * 事件日志
 * since 7/4/20
 *
 * @author eddie
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventTrace {

    /**
     * 事件描述
     */
    String event() default "";
    /**
     * 请求参数
     */
    boolean parameter() default true;
    /**
     * 返回值
     */
    boolean response() default true;

    LoggerType loggerType() default LoggerType.FORMAT;

    String key() default "";

}
