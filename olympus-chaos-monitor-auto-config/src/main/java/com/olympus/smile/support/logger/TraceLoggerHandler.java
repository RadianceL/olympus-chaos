package com.olympus.smile.support.logger;

import com.olympus.logger.event.annotation.EventTrace;
import com.olympus.logger.event.model.EventLoggerContext;
import com.olympus.logger.utils.PublicIpUtil;
import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.smile.config.Environment;
import com.olympus.smile.config.SmileBootProperties;
import com.olympus.smile.support.handler.EventHandler;
import com.olympus.smile.support.management.ProcessHandlerManagement;
import com.olympus.smile.support.model.EventContext;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        } else {
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
                    if (e instanceof MultipartFile) {
                        return false;
                    }
                    return !(e instanceof HttpServletRequest);
                }).collect(Collectors.toList());
                loggerContext.setParameter(objects.toString());
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
                String response = resultObj.toString();
                if (response.length() > 1536) {
                    response = response.substring(0, 1536);
                }
                loggerContext.setResult(response);
            } else {
                loggerContext.setResult("null");
            }
        } else {
            loggerContext.setResult("un-trace");
        }
    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
