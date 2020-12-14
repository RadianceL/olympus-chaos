package com.el.smile.support.logger;

import com.alibaba.fastjson.JSON;
import com.el.smile.logger.event.model.EventLoggerContext;
import com.el.smile.logger.utils.SmileLocalUtils;
import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 花费时间控制器
 * since 7/5/20
 *
 * @author eddie
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggerHandler implements EventHandler {

    private final Logger traceLogger;

    private static final String DEFAULT_LOGGER_TEMPLATE = "TRACE LOG -" +
            "  traceId [{}]" +
            ", appName [{}]" +
            ", env [{}]" +
            ", ip [{}]" +
            ", event [{}]" +
            ", method [{}]" +
            ", costTime [{}]" +
            ", parameter [{}]" +
            ", response [{}]" +
            ", features [{}]";

    @Override
    public int getOrder() {
        return 99;
    }

    @Override
    public void preInvoke(EventContext eventContext, ProceedingJoinPoint point) { }

    @Override
    public void postInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        EventLoggerContext loggerContext = eventContext.getLoggerContext();
        loggerContext.setFeatures(SmileLocalUtils.getUserLoggerFeature());
        switch (eventContext.getLoggerType()) {
            case JSON:
                traceLogger.info(JSON.toJSONString(loggerContext));
                break;
            case FORMAT:
                traceLogger.info(DEFAULT_LOGGER_TEMPLATE,
                        loggerContext.getTraceId(),
                        loggerContext.getAppName(),
                        loggerContext.getEnv(),
                        loggerContext.getIp(),
                        loggerContext.getEvent(),
                        loggerContext.getMethod(),
                        loggerContext.getCostTime(),
                        loggerContext.getParameter(),
                        loggerContext.getResult(),
                        loggerContext.getFeatures());
                break;
            default:
                break;
        }

    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
