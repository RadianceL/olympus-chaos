package com.el.smile.config;

import com.el.smile.interceptor.BaseTraceInterceptor;
import com.el.smile.interceptor.SpringCloudFeignInterceptor;
import com.el.smile.util.TraceLocalUtils;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
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
public class BasicWebConfiguration implements WebMvcConfigurer{

    /**
     * 基础拦截器 从http header中获取traceId
     * 默认激活：从前端带入trace设置到 {@link TraceLocalUtils}
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
}
