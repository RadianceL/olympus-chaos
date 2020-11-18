package com.el.smile.logger.logger;

import com.el.smile.logger.utils.SpringStaticContextHolder;
import org.slf4j.Logger;

/**
 * 事件日志 <br/>
 * since 2020/9/14
 *
 * @author eddie.lys
 */
public class SmileEventLogger {

    private static final Logger log = SpringStaticContextHolder.getBean("eventLogger", Logger.class);

    public static void info() {
    }


}
