package com.el.smile.logger.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * trace线程域
 * since 2020/6/21
 *
 * @author eddie
 */
public class SmileLocalUtils {

    private static final ThreadLocal<Map<String, String>> SERVICE_INVOKE_INFO = new ThreadLocal<>();

    private static final ThreadLocal<Object> USER_INFO = new ThreadLocal<>();

    private static Object userData;

    public static <T> void setCurrentUser(T userData) {
        USER_INFO.set(userData);
    }

    public static <T> T getCurrentUser() {
        return (T) USER_INFO.get();
    }

    /**
     * 底层基础字段 - traceId
     */
    private static final String BASE_MAP_FIELD_TRACE_ID = "traceId";
    /**
     * 日志参数前缀
     */
    private static final String LOGGER_FEATURE = "LOGGER_FEATURE";
    /**
     * 关键帧处理结果前缀
     */
    private static final String KEY_FRAMES_PROCESSING_RESULTS = "KEY_FRAMES_PROCESSING_RESULTS";

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
        SERVICE_INVOKE_INFO.get().put(LOGGER_FEATURE.concat(key), value);
    }

    /**
     * 业务关键点配置，不阻塞业务，后续日志输出自动转换为error级别日志
     * @param success 关键点是否成功
     */
    public static void setIsSuccess(boolean success) {
        setTreadLocalField(KEY_FRAMES_PROCESSING_RESULTS, String.valueOf(success));
    }

    /**
     * 全流程处理是否成功
     * @return  关键点是否成功
     */
    public static Boolean getProcessIsSuccess() {
        String isSuccess = getUserDate(KEY_FRAMES_PROCESSING_RESULTS);
        if (StringUtils.isBlank(isSuccess)) {
            // 关键点未配置 默认成功
            return true;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Map<String, String> getUserLoggerFeature() {
        return parseMapForFilterByOptional();
    }

    private static void checkIfNull() {
        Map<String, String> localBaseMap = SERVICE_INVOKE_INFO.get();
        if (Objects.isNull(localBaseMap)) {
            SERVICE_INVOKE_INFO.set(new HashMap<>(8));
        }
    }

    public static void clear() {
        USER_INFO.remove();
        SERVICE_INVOKE_INFO.remove();
    }

    private static Map<String, String> parseMapForFilterByOptional() {
        return Optional.ofNullable(SERVICE_INVOKE_INFO.get()).map(
                map -> map.entrySet().stream()
                        .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                        .filter(entry -> StringUtils.startsWith(entry.getKey(), LOGGER_FEATURE))
                        .peek(entry -> entry.setValue(entry.getValue().replaceFirst(LOGGER_FEATURE, "")))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue
                        ))
        ).orElse(null);
    }

    private static String getCaller() {
        StackTraceElement[] stackTrace = (new Throwable()).getStackTrace();
        StackTraceElement targetStackTraceElement = stackTrace[stackTrace.length - 1];
        return targetStackTraceElement.getClassName() +
                '(' +
                targetStackTraceElement.getMethodName() +
                ':' +
                targetStackTraceElement.getLineNumber() +
                ")";
    }

}
