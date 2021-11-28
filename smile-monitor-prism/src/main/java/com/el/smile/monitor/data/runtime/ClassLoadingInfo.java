package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.ClassLoadingMXBean;

/**
 * 类加载信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class ClassLoadingInfo {
    /**
     * 已加载类总数
     */
    private Long totalLoadedClassCount;
    /**
     * 已加载当前类
     */
    private Integer loadedClassCount;
    /**
     * 已卸载类总数
     */
    private Long unloadedClassCount;

    public static ClassLoadingInfo analyseClassLoadingInfo(ClassLoadingMXBean classLoadingBean) {
        ClassLoadingInfo classLoadingInfo = new ClassLoadingInfo();
        classLoadingInfo.setTotalLoadedClassCount(classLoadingBean.getTotalLoadedClassCount());
        classLoadingInfo.setLoadedClassCount(classLoadingBean.getLoadedClassCount());
        classLoadingInfo.setUnloadedClassCount(classLoadingBean.getUnloadedClassCount());
        return classLoadingInfo;
    }
}
