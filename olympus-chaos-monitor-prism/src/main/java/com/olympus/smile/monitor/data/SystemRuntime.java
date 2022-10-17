package com.olympus.smile.monitor.data;

import com.olympus.smile.monitor.data.runtime.*;
import lombok.Data;

/**
 * 系统运行时信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class SystemRuntime {
    /**
     * 类加载信息
     */
    private ClassLoadingInfo classLoadingInfo;
    /**
     * 编译器信息
     */
    private CompilationInfo compilationInfo;
    /**
     * 垃圾收集器信息
     */
    private GarbageCollectorInfo garbageCollectorInfo;
    /**
     * 虚拟机运行时信息
     */
    private JvmRuntimeInfo jvmRuntimeInfo;
    /**
     * java线程信息
     */
    private JvmThreadInfo jvmThreadInfo;
    /**
     * 内存使用情况
     */
    private MemoryManagerInfo memoryManagerInfo;
    /**
     * 内存池信息
     */
    private MemoryPoolInfo memoryPoolInfo;
    /**
     * 操作系统信息
     */
    private OperatingSystemInfo operatingSystemInfo;
    /**
     * 系统内存信息
     */
    private SystemMemoryInfo systemMemoryInfo;
}
