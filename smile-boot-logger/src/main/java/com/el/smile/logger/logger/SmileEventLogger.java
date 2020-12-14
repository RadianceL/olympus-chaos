package com.el.smile.logger.logger;

import com.el.smile.logger.utils.SpringStaticContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

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

    public static void info(String info, Object... args) {
        if (Objects.nonNull(eventLogger)) {
            eventLogger.info(info, args);
            return;
        }
        log.info(info, args);
    }

    public static void error(String info, Object... args) {
        if (Objects.nonNull(eventLogger)) {
            eventLogger.error(info, args);
            return;
        }
        log.error(info, args);
    }

}
