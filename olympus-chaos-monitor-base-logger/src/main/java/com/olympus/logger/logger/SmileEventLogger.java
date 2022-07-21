package com.olympus.logger.logger;

import com.olympus.logger.utils.SmileLocalUtils;
import com.olympus.logger.utils.SpringStaticContextHolder;
import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 事件日志工具
     */
    private static final Logger eventLogger = SpringStaticContextHolder.getBean("eventLogger", Logger.class);
    /**
     * 日志格式化模板
     */
    private static final String CLASS_PREFIX = "{} - [{}.{}] line [{}]: ";

    /**
     * 打印事件信息 - 打印至配置路径
     * 当配置核心关键点，且核心关键点为false时，则自动打印error日志
     * @param info  logger的自定义日志模板
     * @param args  自定义模板的参数
     */
    public static void printInfo(String info, Object... args) {
        String message = fillCallClassInfo();
        info = message.concat(info);
        if (Objects.nonNull(eventLogger)) {
            if (SmileLocalUtils.getProcessIsSuccess()) {
                eventLogger.info(info, args);
            }else {
                eventLogger.error(info, args);
            }
            return;
        }
        log.info("！！！local logger printf！！！： " + info, args);
    }

    /**
     * 打印异常信息 - 打印至配置路径
     * @param info  logger的自定义日志模板
     * @param args  自定义模板的参数
     */
    public static void error(String info, Object... args) {
        String message = fillCallClassInfo();
        info = message.concat(info);
        if (Objects.nonNull(eventLogger)) {
            eventLogger.error(info, args);
            return;
        }
        log.error("！！！local logger printf！！！： " + info, args);
    }

    private static String fillCallClassInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String traceId = SmileLocalUtils.getTraceId();
        Object[] array = new Object[]{traceId, stackTrace[3].getClassName(), stackTrace[3].getMethodName(), stackTrace[3].getLineNumber()};
        return MessageFormatter.arrayFormat(CLASS_PREFIX, array).getMessage();
    }

}
