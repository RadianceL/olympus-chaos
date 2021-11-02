package com.el.smile.support.logger;

import com.alibaba.fastjson.JSON;
import com.el.smile.config.Environment;
import com.el.smile.config.SmileBootProperties;
import com.el.smile.logger.event.annotation.EventTrace;
import com.el.smile.logger.event.model.EventLoggerContext;
import com.el.smile.logger.utils.PublicIpUtil;
import com.el.smile.logger.utils.SmileLocalUtils;
import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 花费时间统计处理器
 * since 7/5/20
 *
 * @author eddie
 */
@Component
public class TraceLoggerHandler implements EventHandler {

    @Autowired
    private SmileBootProperties smileBootProperties;

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public void preInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        EventLoggerContext loggerContext = eventContext.getLoggerContext();
        MethodSignature signature = (MethodSignature) point.getSignature();
        EventTrace annotation = signature.getMethod().getAnnotation(EventTrace.class);

        loggerContext.setTraceId(SmileLocalUtils.getTraceId());
        loggerContext.setAppName(Environment.getInstance().getAppName());
        loggerContext.setEnv(Environment.getInstance().getEnvironment());

        if (smileBootProperties.getPublicIpIfPresent()) {
            loggerContext.setIp(PublicIpUtil.getPublicIpAddress());
        }else {
            loggerContext.setIp(PublicIpUtil.getLocalIpAddress());
        }
        loggerContext.setEvent(annotation.event());

        String methodClassName = point.getSignature().getDeclaringTypeName();
        String methodDeclaringName = point.getSignature().getName();
        if (StringUtils.isNotBlank(methodClassName) && StringUtils.isNotBlank(methodDeclaringName)) {
            loggerContext.setMethod(methodClassName.concat(".").concat(methodDeclaringName).concat("()"));
        }

        boolean parameter = annotation.parameter();
        if (parameter) {
            Object[] args = point.getArgs();
            if (Objects.nonNull(args) && args.length > 0) {
                List<Object> objects = Arrays.asList(args);
                objects = objects.stream().filter(e -> {
                    if (e instanceof HttpServletResponse) {
                        return false;
                    }
                    return !(e instanceof HttpServletRequest);
                }).collect(Collectors.toList());
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

        eventContext.setLoggerType(annotation.loggerType());

        if (annotation.response()) {
            Object resultObj = eventContext.getResultObj();
            if (Objects.nonNull(resultObj)) {
                loggerContext.setResult(JSON.toJSONString(resultObj));
            }else {
                loggerContext.setResult("null");
            }
        }else {
            loggerContext.setResult("un-trace");
        }
    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
