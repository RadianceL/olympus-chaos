package com.el.smile.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * trace线程域
 * since 2020/6/21
 *
 * @author eddie
 */
public class LocalDataUtils {

    private static final ThreadLocal<Map<String, String>> SERVICE_INVOKE_INFO = new ThreadLocal<>();

    /**
     * 底层基础字段 - traceId
     */
    private static final String BASE_MAP_FIELD_TRACE_ID = "traceId";

    public static String getTraceId() {
        checkIfNull();
        return SERVICE_INVOKE_INFO.get().get(BASE_MAP_FIELD_TRACE_ID);
    }

    public static void setTraceId(String traceId){
        setTreadLocalField(BASE_MAP_FIELD_TRACE_ID, traceId);
    }

    public static void setTreadLocalField(String key, String value){
        checkIfNull();
        SERVICE_INVOKE_INFO.get().put(key, value);
    }

    private static void checkIfNull() {
        Map<String, String> localBaseMap = SERVICE_INVOKE_INFO.get();
        if (Objects.isNull(localBaseMap)) {
            SERVICE_INVOKE_INFO.set(new HashMap<>(8));
        }
    }

    public static void clear() {
        SERVICE_INVOKE_INFO.remove();
    }
}
