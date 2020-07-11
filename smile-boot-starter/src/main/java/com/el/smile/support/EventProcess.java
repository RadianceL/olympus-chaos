package com.el.smile.support;

import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志处理器
 * 2019/10/5
 *
 * @author eddielee
 */
@Slf4j
@Aspect
@Component
public class EventProcess {

    @Pointcut("@annotation(com.el.smile.logger.event.annotation.EventTrace)")
    public void eventTracePointCut() {
    }

    @Around("eventTracePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        List<EventHandler> eventHandlerList = ProcessHandlerManagement.getEventHandler();
        EventContext context = new EventContext("event-logger");
        for (EventHandler eventHandler : eventHandlerList) {
            eventHandler.preInvoke(context, point);
        }
        Object result = point.proceed();
        context.setResultObj(result);
        for (EventHandler eventHandler : eventHandlerList) {
            eventHandler.postInvoke(context, point);
        }
        return result;
    }

    @AfterThrowing(throwing = "ex", value = "eventTracePointCut()")
    public void doRecoveryActions(Throwable ex) {

    }

}