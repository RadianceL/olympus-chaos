package com.el.smile.logger.logger;

import com.el.smile.logger.utils.SmileLocalUtils;
import com.el.smile.logger.utils.SpringStaticContextHolder;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Objects;

/**
 * 事件日志 <br/>
 * since 2020/9/14
 *
 * @author eddie.lys
 */
@Slf4j
public class SmileEventLogger {

    private static final Logger eventLogger = SpringStaticContextHolder.getBean("eventLogger", Logger.class);

    private static final String CLASS_PREFIX = "[{}.{}] line [{}]:";

    public static void info(String info, Object... args) {
        if (StringUtils.isBlank(info)) {
            log.info(info, args);
        }
        SmileLocalUtils.getTraceId();
        String message = fillCallClassInfo();
        info = message.concat(info);
        if (Objects.nonNull(eventLogger)) {
            eventLogger.info(info, args);
            return;
        }
        log.info(info, args);
    }

    public static void error(String info, Object... args) {
        if (StringUtils.isBlank(info)) {
            log.info(info, args);
        }
        String message = fillCallClassInfo();
        info = message.concat(info);
        if (Objects.nonNull(eventLogger)) {
            eventLogger.error(info, args);
            return;
        }
        log.error(info, args);
    }


    private static String fillCallClassInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Object[] array = new Object[]{stackTrace[2].getClassName(), stackTrace[2].getMethodName(), stackTrace[2].getLineNumber()};
        return MessageFormatter.arrayFormat(CLASS_PREFIX, array).getMessage();
    }

}
