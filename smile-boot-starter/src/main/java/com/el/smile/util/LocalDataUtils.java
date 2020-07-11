package com.el.smile.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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
    /**
     * 日志参数前缀
     */
    private static final String LOGGER_PREFIX = "LOGGER";
    /**
     * 请求是否成功
     */
    private static final String LOGGER_IS_SUCCESSS = "LOGGER_IS_SUCCESS";

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

    public static String getUserDate(String key) {
        checkIfNull();
        return SERVICE_INVOKE_INFO.get().get(key);
    }

    public static void addLoggerFeature(String key, String value) {
        checkIfNull();
        SERVICE_INVOKE_INFO.get().put(LOGGER_PREFIX.concat(key), value);
    }

    public static void setIsSucess(boolean success) {
        setTreadLocalField(LOGGER_IS_SUCCESSS, String.valueOf(success));
    }

    public static Boolean getIsSuccess() {
        String isSuccess = getUserDate(LOGGER_IS_SUCCESSS);
        if (StringUtils.isBlank(isSuccess)) {
            return null;
        }

        return Boolean.valueOf(isSuccess);
    }

    public static Map<String, String> getUserLoggerFeature () {
        return parseMapForFilterByOptional();
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

    private static Map<String, String> parseMapForFilterByOptional() {
        return Optional.ofNullable(SERVICE_INVOKE_INFO.get()).map(
                (v) -> v.entrySet().stream()
                        .filter((e) -> StringUtils.isNotBlank(e.getValue()))
                        .filter((e) -> StringUtils.startsWith(e.getKey(), LOGGER_PREFIX))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ))
        ).orElse(null);
    }

}
