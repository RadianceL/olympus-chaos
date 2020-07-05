package com.el.smile.support.handler;

import org.aspectj.lang.ProceedingJoinPoint;

import com.el.smile.support.model.EventContext;

/**
 * 事件处理器
 * since 7/5/20
 *
 * @author eddie
 */
public interface EventHandler {

    /**
     * 执行优先级
     * 规范：
     * 0-99 0为最高
     * 0-9为基础内置级别
     * 业务可使用10-99
     * @return  优先级
     */
    int getOrder();
    /**
     * pre handle invoke
     * @param eventContext  事件上下文
     * @param point         调用点
     */
    void preInvoke(EventContext eventContext, ProceedingJoinPoint point);
    /**
     * post handle invoke
     * @param eventContext  事件上下文
     * @param point         调用点
     */
    void postInvoke(EventContext eventContext, ProceedingJoinPoint point);
}
