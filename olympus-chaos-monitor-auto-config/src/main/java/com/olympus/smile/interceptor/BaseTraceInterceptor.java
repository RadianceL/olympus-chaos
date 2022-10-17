package com.olympus.smile.interceptor;

import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.logger.utils.TraceIdUtil;
import com.olympus.smile.config.ApplicationConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础拦截器
 * since 2020/6/21
 *
 * @author eddie
 */
public class BaseTraceInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String tradeId = request.getHeader(ApplicationConstants.HEADER_TRACE_ID);
        if (StringUtils.isBlank(tradeId)) {
            tradeId = TraceIdUtil.getTraceId();
        }
        SmileLocalUtils.setTraceId(tradeId);
        response.addHeader(ApplicationConstants.HEADER_TRACE_ID, tradeId);
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        SmileLocalUtils.clear();
    }
}
