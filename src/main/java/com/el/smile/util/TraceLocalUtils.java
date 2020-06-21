package com.el.smile.util;

/**
 * trace线程域
 * since 2020/6/21
 *
 * @author eddie
 */
public class TraceLocalUtils {

    private final static ThreadLocal<String> SERVICE_INVOKE_INFO = new ThreadLocal<>();

    public static String getTraceId() {
        return SERVICE_INVOKE_INFO.get();
    }

    public static void setTraceId(String traceId){
        SERVICE_INVOKE_INFO.set(traceId);
    }
}
