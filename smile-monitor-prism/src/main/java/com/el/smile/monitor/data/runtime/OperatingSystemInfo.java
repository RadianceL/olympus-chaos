package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 操作系统信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class OperatingSystemInfo {

    private static final long MB = 1024 * 1024;
    /**
     * 系统名称
     */
    private String systemName;
    /**
     * 系统版本
     */
    private String systemVersion;
    /**
     * 操作系统的架构
     */
    private String systemArch;
    /**
     * 可用的内核数
     */
    private Integer availableProcessors;
    /**
     * 总物理内存(M)
     */
    private Long totalPhysicalMemory;
    /**
     * 已用物理内存(M)
     */
    private Long usedPhysicalMemorySize;
    /**
     * 剩余物理内存(M)
     */
    private Long freePhysicalMemory;
    /**
     * 总交换空间(M)
     */
    private Long totalSwapSpaceSize;
    /**
     * 已用交换空间(M)
     */
    private Long usedSwapSpaceSize;
    /**
     * 剩余交换空间(M)
     */
    private Long freeSwapSpaceSize;

    public static OperatingSystemInfo analyseOperatingSystemBean(OperatingSystemMXBean operatingSystemBean) {
        OperatingSystemInfo operatingSystemInfo = new OperatingSystemInfo();
        operatingSystemInfo.setSystemName(operatingSystemBean.getName());
        operatingSystemInfo.setSystemVersion(operatingSystemBean.getVersion());
        operatingSystemInfo.setSystemArch(operatingSystemBean.getArch());
        operatingSystemInfo.setAvailableProcessors(operatingSystemBean.getAvailableProcessors());

        if(isSunOsBean(operatingSystemBean)){
            long totalPhysicalMemory = getLongFromOperatingSystem(operatingSystemBean,"getTotalPhysicalMemorySize");
            long freePhysicalMemory = getLongFromOperatingSystem(operatingSystemBean, "getFreePhysicalMemorySize");
            long usedPhysicalMemorySize =totalPhysicalMemory - freePhysicalMemory;
            operatingSystemInfo.setTotalPhysicalMemory(totalPhysicalMemory/MB);
            operatingSystemInfo.setUsedPhysicalMemorySize(usedPhysicalMemorySize/MB);
            operatingSystemInfo.setFreePhysicalMemory(freePhysicalMemory/MB);

            long  totalSwapSpaceSize = getLongFromOperatingSystem(operatingSystemBean, "getTotalSwapSpaceSize");
            long freeSwapSpaceSize = getLongFromOperatingSystem(operatingSystemBean, "getFreeSwapSpaceSize");
            long usedSwapSpaceSize = totalSwapSpaceSize - freeSwapSpaceSize;
            operatingSystemInfo.setTotalSwapSpaceSize(totalSwapSpaceSize/MB);
            operatingSystemInfo.setUsedSwapSpaceSize(usedSwapSpaceSize/MB);
            operatingSystemInfo.setFreeSwapSpaceSize(freeSwapSpaceSize/MB);
        }
        return operatingSystemInfo;
    }

    private static long getLongFromOperatingSystem(OperatingSystemMXBean operatingSystem, String methodName) {
        try {
            final Method method = operatingSystem.getClass().getMethod(methodName, (Class<?>[]) null);
            method.setAccessible(true);
            return (Long) method.invoke(operatingSystem, (Object[]) null);
        } catch (final InvocationTargetException e) {
            if (e.getCause() instanceof Error) {
                throw (Error) e.getCause();
            } else if (e.getCause() instanceof RuntimeException) {
                throw (RuntimeException) e.getCause();
            }
            throw new IllegalStateException(e.getCause());
        } catch (final NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean isSunOsBean(OperatingSystemMXBean operatingSystem) {
        final String className = operatingSystem.getClass().getName();
        return "com.sun.management.OperatingSystem".equals(className)
                || "com.sun.management.UnixOperatingSystem".equals(className);
    }
}
