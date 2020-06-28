package com.el.smile.interceptor;

import com.el.smile.config.ApplicationConstants;
import com.el.smile.util.TraceLocalUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

/**
 * Feign前置拦截器
 * since 2020/6/21
 *
 * @author eddie
 */
@Slf4j
public class SpringCloudFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate){
        log.info("feign头设置traceId {}", TraceLocalUtils.getTraceId());
        requestTemplate.header(ApplicationConstants.HEADER_TRACE_ID, TraceLocalUtils.getTraceId());
    }

}
