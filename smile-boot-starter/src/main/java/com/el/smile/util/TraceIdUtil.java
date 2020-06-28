package com.el.smile.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * since 2020/3/8
 *
 * @author eddie
 */
public class TraceIdUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String getTraceId(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime localDateTime = LocalDateTime.now();
        String localDate = DATE_TIME_FORMATTER.format(localDateTime);
        return localDate.concat("-").concat(uuid);
    }

}
