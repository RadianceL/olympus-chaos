package com.el.smile.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.el.smile.interceptor.BaseTraceInterceptor;
import com.el.smile.interceptor.SpringCloudFeignInterceptor;
import com.el.smile.logger.logger.Slf4jEventLogger;
import com.el.smile.support.EventProcess;
import com.el.smile.support.logger.CostTimeLoggerHandler;
import com.el.smile.support.logger.LoggerHandler;
import com.el.smile.support.logger.TraceEventLoggerHandler;
import com.el.smile.util.LocalDataUtils;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自动配置中心
 * since 2020/2/18
 *
 * @author eddie
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasicWebConfiguration implements WebMvcConfigurer{

    private final SmileBootProperties smileBootProperties;

    /**
     * 基础拦截器 从http header中获取traceId
     * 默认激活：从前端带入trace设置到 {@link LocalDataUtils}
     * @param registry  添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseTraceInterceptor()).addPathPatterns("/**");
    }

    /**
     * 应用环境
     * 默认激活 「spring.profiles.active， spring.application.name」
     * @return  {@link Environment 激活后获取Environment实例}
     */
    @Bean
    public ApplicationEnvironmentRunner application() {
        return new ApplicationEnvironmentRunner();
    }

    @Bean
    public CostTimeLoggerHandler costTimeLoggerHandler() {
        return new CostTimeLoggerHandler();
    }

    @Bean
    public TraceEventLoggerHandler traceEventLoggerHandler() {
        return new TraceEventLoggerHandler();
    }

    @Bean
    public LoggerHandler loggerHandler() {
        return new LoggerHandler(eventLogger());
    }

    @Bean
    public EventProcess eventProcess() {
        return new EventProcess();
    }

    /**
     * SpringCloudFeignInterceptor拦截器配置
     * 注入条件： web应用 引入Feign依赖
     * @return {@link SpringCloudFeignInterceptor spring feign header拦截器配置}
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnClass(RequestInterceptor.class)
    public SpringCloudFeignInterceptor springCloudFeignInterceptor(){
        return new SpringCloudFeignInterceptor();
    }

    public Logger eventLogger() {
        SmileBootProperties.TraceLoggerConfig traceLoggerConfig = smileBootProperties.getTraceLogger();
        return Slf4jEventLogger.builder()
                .level(Level.INFO)
                .path(traceLoggerConfig.getLogPath())
                .name(traceLoggerConfig.getLogFileName())
                .pattern(traceLoggerConfig.getPattern())
                .maxFileSize(traceLoggerConfig.getMaxFileSize())
                .maxHistory(traceLoggerConfig.getMaxHistory())
                .totalSizeCap(traceLoggerConfig.getTotalSizeCap()).build();
    }
}
