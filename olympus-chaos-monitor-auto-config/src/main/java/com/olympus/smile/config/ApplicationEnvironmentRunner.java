package com.olympus.smile.config;

import com.olympus.smile.entity.EnvironmentEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 应用启动配置
 * since 2020/6/21
 *
 * @author eddie
 */
public class ApplicationEnvironmentRunner {

    @Value("${spring.profiles.active}")
    private String activeEnv;

    @Value("${spring.application.name}")
    private String applicationName;

    @PostConstruct
    public void init() {
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
