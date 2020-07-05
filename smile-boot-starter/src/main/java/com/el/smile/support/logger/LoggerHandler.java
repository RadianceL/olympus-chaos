package com.el.smile.support.logger;

import com.el.smile.logger.event.model.EventLoggerContext;
import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 花费时间控制器
 * since 7/5/20
 *
 * @author eddie
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggerHandler implements EventHandler {

    private final Logger eventLogger;

    private static final String DEFAULT_LOGGER_TEMPLATE = "TRACE LOG - traceId [{}]" +
            ", appName [{}]" +
            ", env [{}]" +
            ", ip [{}]" +
            ", event [{}]" +
            ", costTime [{}]" +
            ", parameter [{}]" +
            ", response [{}]";

    @Override
    public int getOrder() {
        return 99;
    }

    @Override
    public void preInvoke(EventContext eventContext, ProceedingJoinPoint point) {

    }

    @Override
    public void postInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        EventLoggerContext loggerContext = eventContext.getLoggerContext();
        eventLogger.info(DEFAULT_LOGGER_TEMPLATE,
                loggerContext.getTraceId(),
                loggerContext.getAppName(),
                loggerContext.getEnv(),
                loggerContext.getIp(),
                loggerContext.getEvent(),
                loggerContext.getCostTime(),
                loggerContext.getParameter(),
                loggerContext.getResult()
        );
    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
