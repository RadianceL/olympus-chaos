package com.olympus.smile.interceptor;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.logger.utils.TraceIdUtil;
import com.olympus.smile.config.ApplicationConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * dubbo前置过滤器
 * since 2020/6/22
 *
 * @author eddie
 */
@Activate(group = { Constants.PROVIDER })
public class CloudRpcFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(ApplicationConstants.HEADER_TRACE_ID);
        if (!StringUtils.isEmpty(traceId)) {
            // *) 从RpcContext里获取traceId并保存
            SmileLocalUtils.setTraceId(traceId);
        } else {
            // *) 交互前重新设置traceId, 避免信息丢失
            traceId = SmileLocalUtils.getTraceId();
            if (StringUtils.isEmpty(traceId)) {
                traceId = TraceIdUtil.getTraceId();
            }
            RpcContext.getContext().setAttachment(ApplicationConstants.HEADER_TRACE_ID, traceId);
        }
        // *) 实际的rpc调用
        return invoker.invoke(invocation);
    }
}
