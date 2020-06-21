package com.el.smile.interceptor;

import com.alibaba.dubbo.rpc.*;
import com.el.smile.config.ApplicationConstants;
import com.el.smile.util.TraceIdUtil;
import com.el.smile.util.TraceLocalUtils;
import org.springframework.util.StringUtils;

/**
 * dubbo前置过滤器
 * since 2020/6/22
 *
 * @author eddie
 */
public class CloudRpcFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(ApplicationConstants.HEADER_TRACE_ID);
        if (!StringUtils.isEmpty(traceId)) {
            // *) 从RpcContext里获取traceId并保存
            TraceLocalUtils.setTraceId(traceId);
        } else {
            // *) 交互前重新设置traceId, 避免信息丢失
            traceId = TraceLocalUtils.getTraceId();
            if (StringUtils.isEmpty(traceId)) {
                traceId = TraceIdUtil.getTraceId();
            }
            RpcContext.getContext().setAttachment(ApplicationConstants.HEADER_TRACE_ID, traceId);
        }
        // *) 实际的rpc调用
        return invoker.invoke(invocation);
    }
}
