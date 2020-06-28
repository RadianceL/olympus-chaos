package com.el.smile.config;

import com.el.smile.entity.EnvironmentEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 应用启动配置
 * since 2020/6/21
 *
 * @author eddie
 */
public class ApplicationEnvironmentRunner implements ApplicationRunner {

    @Value("spring.profiles.active")
    private String activeEnv;

    @Value("spring.application.name")
    private String applicationName;

    @Override
    public void run(ApplicationArguments args) {
        if (StringUtils.isEmpty(activeEnv) || StringUtils.isEmpty(applicationName)) {
            return;
        }
        EnvironmentEnum environment = null;
        if (activeEnv.equalsIgnoreCase(EnvironmentEnum.DAILY.name())) {
            environment = EnvironmentEnum.DAILY;
        }

        if (activeEnv.equalsIgnoreCase(EnvironmentEnum.STAGING.name())) {
            environment = EnvironmentEnum.STAGING;
        }

        if (activeEnv.equalsIgnoreCase(EnvironmentEnum.PROD.name())) {
            environment = EnvironmentEnum.PROD;
        }

        if (Objects.isNull(environment)) {
            return;
        }

        new Environment(applicationName, environment);
    }
}
