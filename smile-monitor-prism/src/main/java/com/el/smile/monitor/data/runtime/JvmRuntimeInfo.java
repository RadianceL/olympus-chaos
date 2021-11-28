package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Map;

/**
 * 虚拟机运行时信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class JvmRuntimeInfo {
    /**
     * 进程PID
     */
    private String pid;
    /**
     * jvm规范名称
     */
    private String specName;
    /**
     * jvm规范运营商
     */
    private String specVendor;
    /**
     * jvm规范版本
     */
    private String specVersion;
    /**
     * jvm启动时间（毫秒）
     */
    private Long jvmStartTime;
    /**
     * 获取System.properties
     */
    private Map<String, String> systemProperties;
    /**
     * jvm正常运行时间（毫秒）
     */
    private Long jvmUpTime;
    /**
     * jvm名称
     */
    private String vmName;
    /**
     * jvm运营商
     */
    private String vmVendor;
    /**
     * jvm实现版本
     */
    private String vmVersion;
    /**
     * vm参数
     */
    private List<String> inputArguments;
    /**
     * 类路径
     */
    private String classPath;
    /**
     * 库路径
     */
    private String libraryPath;

    public static JvmRuntimeInfo analyseJvmRuntimeInfo(RuntimeMXBean runtime) {
        JvmRuntimeInfo jvmRuntimeInfo = new JvmRuntimeInfo();
        jvmRuntimeInfo.setPid(runtime.getName().split("@")[0]);
        jvmRuntimeInfo.setSpecName(runtime.getSpecName());
        jvmRuntimeInfo.setSpecVendor(runtime.getSpecVendor());
        jvmRuntimeInfo.setSpecVersion(runtime.getSpecVersion());

        //返回虚拟机在毫秒内的开始时间。该方法返回了虚拟机启动时的近似时间
        jvmRuntimeInfo.setJvmStartTime(runtime.getStartTime());
        //相当于System.getProperties
        jvmRuntimeInfo.setSystemProperties(runtime.getSystemProperties());
        jvmRuntimeInfo.setJvmUpTime(runtime.getUptime());

        //相当于System.getProperty("java.vm.name").
        jvmRuntimeInfo.setVmName(runtime.getVmName());

        //相当于System.getProperty("java.vm.vendor").
        jvmRuntimeInfo.setVmVendor(runtime.getVmVendor());
        //相当于System.getProperty("java.vm.version").
        jvmRuntimeInfo.setVmVersion(runtime.getVmVersion());

        List<String> args = runtime.getInputArguments();
        jvmRuntimeInfo.setInputArguments(args);

        jvmRuntimeInfo.setClassPath(runtime.getClassPath());
        jvmRuntimeInfo.setLibraryPath(runtime.getLibraryPath());
        return jvmRuntimeInfo;
    }
}
