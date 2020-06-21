package com.el.smile.config;

import com.el.smile.interceptor.BaseTraceInterceptor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseTraceInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public ApplicationEnvironmentRunner application() {
        return new ApplicationEnvironmentRunner();
    }
}
