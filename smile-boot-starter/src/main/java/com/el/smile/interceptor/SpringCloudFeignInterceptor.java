package com.el.smile.interceptor;

import com.el.smile.config.ApplicationConstants;
import com.el.smile.util.LocalDataUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

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
        requestTemplate.header(ApplicationConstants.HEADER_TRACE_ID, LocalDataUtils.getTraceId());
    }

}
