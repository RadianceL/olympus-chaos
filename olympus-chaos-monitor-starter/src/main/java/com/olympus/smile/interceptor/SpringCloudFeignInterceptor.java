package com.olympus.smile.interceptor;

import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.smile.config.ApplicationConstants;
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
        requestTemplate.header(ApplicationConstants.HEADER_TRACE_ID, SmileLocalUtils.getTraceId());
    }

}
