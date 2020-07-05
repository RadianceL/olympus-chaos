package com.el.smile.support.logger;

import com.el.smile.support.handler.EventHandler;
import com.el.smile.support.logger.config.LoggerFeaturesConstants;
import com.el.smile.support.management.ProcessHandlerManagement;
import com.el.smile.support.model.EventContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 花费时间控制器
 * since 7/5/20
 *
 * @author eddie
 */
@Component
public class CostTimeLoggerHandler implements EventHandler {

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public void preInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        eventContext.addFeature(LoggerFeaturesConstants.COST_TIME_START, System.currentTimeMillis());
    }

    @Override
    public void postInvoke(EventContext eventContext, ProceedingJoinPoint point) {
        Long userLongData = eventContext.getUserLongData(LoggerFeaturesConstants.COST_TIME_START);
        if (Objects.nonNull(userLongData)) {
            long costTime = System.currentTimeMillis() - userLongData;
            eventContext.getLoggerContext().setCostTime(costTime);
        }
    }

    @PostConstruct
    public void init() {
        ProcessHandlerManagement.registerEventHandler(this);
    }

}
