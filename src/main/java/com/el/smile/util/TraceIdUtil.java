package com.el.smile.util;

import java.util.Date;
import java.util.UUID;

/**
 * since 2020/3/8
 *
 * @author eddie
 */
public class TraceIdUtil {

    public static String getTraceId(){
        String uuid = UUID.randomUUID().toString().replace("-", "");
        Date data = new Date();
        return uuid.concat(data.toString());
    }

}
