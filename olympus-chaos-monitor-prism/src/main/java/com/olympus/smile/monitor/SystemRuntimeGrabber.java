package com.olympus.smile.monitor;

import com.olympus.smile.monitor.core.Grabber;
import com.olympus.smile.monitor.data.SystemRuntime;
import com.el.smile.monitor.data.runtime.*;
import com.olympus.smile.monitor.data.runtime.*;

import java.lang.management.*;
import java.util.List;

/**
 * 系统运行信息抓取器
 *
 * @author eddie.lys
 * @since 2021/11/2
 */
public class SystemRuntimeGrabber implements Grabber<SystemRuntime> {

    @Override
    public SystemRuntime getInfo() {
        SystemRuntime systemRuntime = new SystemRuntime();
        //打印系统信息
        OperatingSystemInfo operatingSystemInfo = printOperatingSystemInfo();
        systemRuntime.setOperatingSystemInfo(operatingSystemInfo);
        //打印编译信息
        CompilationInfo compilationInfo = printCompilationInfo();
        systemRuntime.setCompilationInfo(compilationInfo);
        //打印类加载信息
        ClassLoadingInfo classLoadingInfo = printClassLoadingInfo();
        systemRuntime.setClassLoadingInfo(classLoadingInfo);
        //打印运行时信息
        JvmRuntimeInfo jvmRuntimeInfo = printRuntimeInfo();
        systemRuntime.setJvmRuntimeInfo(jvmRuntimeInfo);
        //打印内存管理器信息
        MemoryManagerInfo memoryManagerInfo = printMemoryManagerInfo();
        systemRuntime.setMemoryManagerInfo(memoryManagerInfo);
        //打印垃圾回收信息
        GarbageCollectorInfo garbageCollectorInfo = printGarbageCollectorInfo();
        systemRuntime.setGarbageCollectorInfo(garbageCollectorInfo);
        //打印vm内存
        SystemMemoryInfo systemMemoryInfo = printMemoryInfo();
        systemRuntime.setSystemMemoryInfo(systemMemoryInfo);
        //打印vm各内存区信息
        MemoryPoolInfo memoryPoolInfo = printMemoryPoolInfo();
        systemRuntime.setMemoryPoolInfo(memoryPoolInfo);
        //打印线程信息
        JvmThreadInfo jvmThreadInfo = printThreadInfo();
        systemRuntime.setJvmThreadInfo(jvmThreadInfo);
        return systemRuntime;
    }

    public static OperatingSystemInfo printOperatingSystemInfo(){
        OperatingSystemMXBean operatingSystemBean = ManagementFactory.getOperatingSystemMXBean();
        return OperatingSystemInfo.analyseOperatingSystemBean(operatingSystemBean);
    }

    public static CompilationInfo printCompilationInfo(){
        CompilationMXBean compilation = ManagementFactory.getCompilationMXBean();
        return CompilationInfo.analyseOperatingSystemBean(compilation);
    }

    public static ClassLoadingInfo printClassLoadingInfo(){
        ClassLoadingMXBean classLoad = ManagementFactory.getClassLoadingMXBean();
        return ClassLoadingInfo.analyseClassLoadingInfo(classLoad);
    }

    public static JvmRuntimeInfo printRuntimeInfo(){
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        return JvmRuntimeInfo.analyseJvmRuntimeInfo(runtime);
    }

    public static MemoryManagerInfo printMemoryManagerInfo(){
        List<MemoryManagerMXBean> memoryManagerBeans = ManagementFactory.getMemoryManagerMXBeans();
        return MemoryManagerInfo.analyseMemoryManagerInfo(memoryManagerBeans);
    }

    public static GarbageCollectorInfo printGarbageCollectorInfo(){
        List<GarbageCollectorMXBean> garbageList = ManagementFactory.getGarbageCollectorMXBeans();
        return GarbageCollectorInfo.analyseGarbageCollectorInfo(garbageList);
    }

    public static SystemMemoryInfo printMemoryInfo(){
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        return SystemMemoryInfo.analyseSystemMemoryInfo(memoryBean);
    }

    public static MemoryPoolInfo printMemoryPoolInfo(){
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        return MemoryPoolInfo.analyseMemoryPoolInfo(pools);
    }

    public static JvmThreadInfo printThreadInfo(){
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return JvmThreadInfo.analyseThreadInfo(threadBean);
    }
}
