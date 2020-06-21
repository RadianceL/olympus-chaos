package com.el.smile.interceptor;

import com.el.smile.config.ApplicationConstants;
import com.el.smile.util.TraceIdUtil;
import com.el.smile.util.TraceLocalUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
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
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String tradeId = request.getHeader(ApplicationConstants.HEADER_TRACE_ID);
        if (StringUtils.isEmpty(tradeId)) {
            tradeId = TraceIdUtil.getTraceId();
        }
        TraceLocalUtils.setTraceId(tradeId);
        return true;
    }

}
