package com.el.smile.support;

import com.el.smile.config.Environment;
import com.el.smile.logger.utils.SmileLocalUtils;
import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventProcess {

    private final Logger traceLogger;

    private static final String DEFAULT_LOGGER_TEMPLATE = "TRACE LOG -" +
            "  traceId [{}]" +
            ", appName [{}]" +
            ", env [{}]" +
            ", errorMessage [{}]";

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
        String traceId = SmileLocalUtils.getTraceId();
        String appName = Environment.getInstance().getAppName();
        String environment = Environment.getInstance().getEnvironment();

        traceLogger.error(DEFAULT_LOGGER_TEMPLATE, traceId, appName, environment, ex.getMessage());
    }

}