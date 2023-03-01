package com.olympus.smile.support.management;

import com.olympus.smile.support.handler.EventHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 过程处理器协调器
 * since 7/5/20
 *
 * @author eddie
 */
@Slf4j
public class ProcessHandlerManagement {

    private static final Integer MAX_HANDLER_ORDER = 99;
    private static final Integer MIN_HANDLER_ORDER = 0;

    /**
     * 事件处理器集合
     */
    private static final List<EventHandler> EVENT_HANDLER = new ArrayList<>();

    public static void registerEventHandler(EventHandler eventHandler) {
        if (Objects.isNull(eventHandler)) {
            throw new IllegalArgumentException("register handler is null: please check");
        }
        int order = eventHandler.getOrder();

        if (order > MAX_HANDLER_ORDER || order < MIN_HANDLER_ORDER) {
            throw new IllegalArgumentException("register handler order limit: 0 - 99");
        }
        EVENT_HANDLER.add(eventHandler);
        EVENT_HANDLER.sort(Comparator.comparingInt(EventHandler::getOrder));
    }

    /**
     * 获取事件管理器
     *
     * @return 事件管理器列表
     */
    public static List<EventHandler> getEventHandler() {
        return EVENT_HANDLER;
    }

}
