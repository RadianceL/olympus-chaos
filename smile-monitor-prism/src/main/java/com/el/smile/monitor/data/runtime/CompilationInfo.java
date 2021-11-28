package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.CompilationMXBean;

/**
 * 编译器信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class CompilationInfo {
    /**
     * 编译器名称
     */
    private String compilationName;
    /**
     * 总编译时常
     */
    private Long totalCompilationTime;

    public static CompilationInfo analyseOperatingSystemBean(CompilationMXBean compilation) {
        CompilationInfo compilationInfo = new CompilationInfo();
        compilationInfo.setCompilationName(compilation.getName());
        //判断jvm是否支持编译时间的监控
        if(compilation.isCompilationTimeMonitoringSupported()){
            compilationInfo.setTotalCompilationTime(compilation.getTotalCompilationTime());
        }
        return compilationInfo;
    }
}
