package com.olympus.smile.config;

import com.olympus.smile.entity.EnvironmentEnum;
import lombok.Data;

/**
 * 环境
 * since 2020/6/21
 *
 * @author eddie
 */
@Data
public class Environment {

    private static Environment ENV = new Environment("UNKNOWN", EnvironmentEnum.UNKNOWN);

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 环境名称
     */
    private String environment;
    /**
     * 是否开发
     */
    private boolean dev = false;
    /**
     * 是否日常
     */
    private boolean daily = false;

    /**
     * 是否预发
     */
    private boolean staging = false;

    /**
     * 是否线上
     */
    private boolean prod = false;

    public Environment(String appName, EnvironmentEnum environment) {
        this.appName = appName;
        this.environment = environment.name();
        switch (environment) {
            case DAILY:
                this.dev = true;
                break;
            case DEV:
                this.daily = true;
                break;
            case STAGING:
                this.staging = true;
                break;
            case PROD:
                this.prod = true;
                break;
            default:
                break;
        }
        ENV = this;
    }

    public static Environment getInstance(){
        return ENV;
    }
}
