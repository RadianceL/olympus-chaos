package com.el.smile.support.logger;

import com.alibaba.fastjson.JSON;
import com.el.smile.config.Environment;
import com.el.smile.logger.event.annotation.EventTrace;
import com.el.smile.logger.event.model.EventLoggerContext;
import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import com.el.smile.util.LocalDataUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 花费时间控制器
 * since 7/5/20
 *
 * @author eddie
 */
@Component

public class TraceEventLoggerHandler implements EventHandler {

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public void preInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        EventLoggerContext loggerContext = eventContext.getLoggerContext();
        MethodSignature signature = (MethodSignature) point.getSignature();
        EventTrace annotation = signature.getMethod().getAnnotation(EventTrace.class);

        loggerContext.setTraceId(LocalDataUtils.getTraceId());
        loggerContext.setAppName(Environment.getInstance().getAppName());
        loggerContext.setEnv(Environment.getInstance().getEnvironment());
        loggerContext.setEvent(annotation.event());

        boolean parameter = annotation.paramter();
        if (parameter) {
            Object[] args = point.getArgs();
            if (Objects.nonNull(args) && args.length > 0) {
                List<Object> objects = Arrays.asList(args);
                String parameters = JSON.toJSONString(objects);
                loggerContext.setParameter(parameters);
            } else {
                loggerContext.setParameter("without parameter");
            }
        }
    }

    @Override
    public void postInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        EventLoggerContext loggerContext = eventContext.getLoggerContext();
        MethodSignature signature = (MethodSignature) point.getSignature();
        EventTrace annotation = signature.getMethod().getAnnotation(EventTrace.class);

        if (annotation.response()) {
            Object resultObj = eventContext.getResultObj();
            if (Objects.nonNull(resultObj)) {
                loggerContext.setResult(JSON.toJSONString(resultObj));
            }else {
                loggerContext.setResult("null");
            }
        }
    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
