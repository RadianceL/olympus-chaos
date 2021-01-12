package com.el.smile.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.el.smile.interceptor.BaseTraceInterceptor;
import com.el.smile.interceptor.SpringCloudFeignInterceptor;
import com.el.smile.logger.logger.Slf4jEventLoggerFactory;
import com.el.smile.logger.logger.Slf4jTraceLoggerFactory;
import com.el.smile.logger.logger.builder.LoggerType;
import com.el.smile.logger.utils.SmileLocalUtils;
import com.el.smile.logger.utils.SpringStaticContextHolder;
import com.el.smile.support.EventProcess;
import com.el.smile.support.logger.CostTimeLoggerHandler;
import com.el.smile.support.logger.LoggerHandler;
import com.el.smile.support.logger.TraceLoggerHandler;
import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

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

    @Value("${spring.application.name}")
    private String applicationName;

    private final SmileBootProperties smileBootProperties;

    /**
     * 基础拦截器 从http header中获取traceId
     * 默认激活：从前端带入trace设置到 {@link SmileLocalUtils}
     * @param registry  添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseTraceInterceptor()).addPathPatterns("/**");
    }

    /**
     * 应用环境
     * 默认激活 「spring.profiles.active， spring.application.name」
     * @return  {@link com.el.smile.config.Environment 激活后获取Environment实例}
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
    public TraceLoggerHandler traceEventLoggerHandler() {
        return new TraceLoggerHandler();
    }

    @Bean
    @Qualifier("traceLogger")
    public LoggerHandler loggerHandler(Logger traceLogger) {
        return new LoggerHandler(traceLogger);
    }

    @Bean
    @Qualifier("traceLogger")
    public EventProcess eventProcess(Logger traceLogger) {
        return new EventProcess(traceLogger);
    }

    /**
     * SpringCloudFeignInterceptor拦截器配置
     * 注入条件： web应用 引入openFeign依赖
     * @return {@link SpringCloudFeignInterceptor spring feign header拦截器配置}
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnClass(RequestInterceptor.class)
    public SpringCloudFeignInterceptor springCloudFeignInterceptor(){
        return new SpringCloudFeignInterceptor();
    }

    @Bean
    public SpringStaticContextHolder prepareSpringStaticContextHolder() {
        return new SpringStaticContextHolder();
    }

    @Bean("eventLogger")
    public Logger eventLogger() {
        SmileBootProperties.TraceLoggerConfig traceLoggerConfig = smileBootProperties.getEventLogger();
        return Slf4jEventLoggerFactory.builder()
                .level(Level.INFO)
                .path(smileBootProperties.getLogPathIfPresent().concat(File.separator).concat(applicationName))
                .pattern(traceLoggerConfig.getPatternIfPresent())
                .name("event-log")
                .maxFileSize(traceLoggerConfig.getMaxFileSizeIfPresent())
                .maxHistory(traceLoggerConfig.getMaxHistoryIfPresent())
                .totalSizeCap(traceLoggerConfig.getTotalSizeCap()).build(LoggerType.EVENT_LOGGER);
    }

    @Bean("traceLogger")
    public Logger traceLogger() {
        SmileBootProperties.TraceLoggerConfig traceLoggerConfig = smileBootProperties.getTraceLogger();
        return Slf4jTraceLoggerFactory.builder()
                .level(Level.INFO)
                .path(smileBootProperties.getLogPathIfPresent().concat(File.separator).concat(applicationName))
                .pattern(traceLoggerConfig.getPattern())
                .name("trace-log")
                .maxFileSize(traceLoggerConfig.getMaxFileSizeIfPresent())
                .maxHistory(traceLoggerConfig.getMaxHistoryIfPresent())
                .totalSizeCap(traceLoggerConfig.getTotalSizeCap()).build(LoggerType.TRACE_LOGGER);
    }
}
