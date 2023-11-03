package com.olympus.smile.support;

import com.alibaba.fastjson.JSON;
import com.olympus.logger.event.annotation.EventTrace;
import com.olympus.logger.event.model.EventLoggerContext;
import com.olympus.logger.utils.PublicIpUtil;
import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.smile.config.Environment;
import com.olympus.smile.config.SmileBootProperties;
import com.olympus.smile.support.handler.EventHandler;
import com.olympus.smile.support.management.ProcessHandlerManagement;
import com.olympus.smile.support.model.EventContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Autowired
    private SmileBootProperties smileBootProperties;

    private static final String DEFAULT_LOGGER_TEMPLATE = "TRACE LOG -" +
            "  traceId [{}]" +
            ", appName [{}]" +
            ", env [{}]" +
            ", ip [{}]" +
            ", event [{}]" +
            ", method [{}]" +
            ", parameter [{}]" +
            ", errorMessage [{}]";

    @Pointcut("@annotation(com.olympus.logger.event.annotation.EventTrace)")
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
    public void doRecoveryActions(JoinPoint point, Throwable ex) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        EventTrace annotation = signature.getMethod().getAnnotation(EventTrace.class);

        EventLoggerContext loggerContext = new EventLoggerContext();
        loggerContext.setTraceId(SmileLocalUtils.getTraceId());
        loggerContext.setAppName( Environment.getInstance().getAppName());
        loggerContext.setEvent(annotation.event());
        loggerContext.setEnv(Environment.getInstance().getEnvironment());

        if (smileBootProperties.getPublicIpIfPresent()) {
            loggerContext.setIp(PublicIpUtil.getPublicIpAddress());
        }else {
            loggerContext.setIp(PublicIpUtil.getLocalIpAddress());
        }

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

        switch (annotation.loggerType()) {
            case JSON:
                traceLogger.info(JSON.toJSONString(loggerContext));
                break;
            case FORMAT:
                traceLogger.error(DEFAULT_LOGGER_TEMPLATE,
                        loggerContext.getTraceId(),
                        loggerContext.getAppName(),
                        loggerContext.getEnv(),
                        loggerContext.getIp(),
                        loggerContext.getEvent(),
                        loggerContext.getMethod(),
                        loggerContext.getParameter(),
                        ex.getMessage());
                break;
            default:
                break;
        }

    }

}