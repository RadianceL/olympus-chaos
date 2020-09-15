package com.el.smile.logger.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * Spring静态获取bean工具 <br/>
 * since 2020/9/9
 *
 * @author eddie.lys
 */
public class SpringStaticContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        if (SpringStaticContextHolder.applicationContext == null) {
            SpringStaticContextHolder.applicationContext = applicationContext;
        }
    }

    /**
     * 获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        if (Objects.isNull(getApplicationContext())) {
            return null;
        }
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(String className, Class<T> clazz) {
        if (Objects.isNull(getApplicationContext())) {
            return null;
        }
        return getApplicationContext().getBean(className, clazz);
    }


}
