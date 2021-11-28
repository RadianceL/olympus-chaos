package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * 系统内存信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class SystemMemoryInfo {

    private static final long MB = 1024 * 1024;
    /**
     * 初始(M)
     */
    private Long headMemoryInit;
    /**
     * 最大(上限)(M)
     */
    private Long headMemoryMax;
    /**
     * 当前(已使用)(M)
     */
    private Long headMemoryUsed;
    /**
     * 提交的内存(已申请)(M)
     */
    private Long headMemoryCommitted;
    /**
     * 使用率
     */
    private Long headMemoryUsedPercentage;

    /**
     * 初始(M)
     */
    private Long nonHeadMemoryInit;
    /**
     * 最大(上限)(M)
     */
    private Long nonHeadMemoryMax;
    /**
     * 当前(已使用)(M)
     */
    private Long nonHeadMemoryUsed;
    /**
     * 提交的内存(已申请)(M)
     */
    private Long nonHeadMemoryCommitted;
    /**
     * 使用率
     */
    private Long nonHeadMemoryUsedPercentage;

    public static SystemMemoryInfo analyseSystemMemoryInfo(MemoryMXBean memoryBean) {
        SystemMemoryInfo systemMemoryInfo = new SystemMemoryInfo();
        MemoryUsage headMemory = memoryBean.getHeapMemoryUsage();
        systemMemoryInfo.setHeadMemoryInit(headMemory.getInit()/MB);
        systemMemoryInfo.setHeadMemoryMax(headMemory.getMax()/MB);
        systemMemoryInfo.setHeadMemoryUsed(headMemory.getUsed()/MB);
        systemMemoryInfo.setHeadMemoryCommitted(headMemory.getCommitted()/MB);
        if (headMemory.getMax() != 0) {
            if (headMemory.getMax() == -1) {
                systemMemoryInfo.setHeadMemoryUsedPercentage(0L);
            }else {
                systemMemoryInfo.setHeadMemoryUsedPercentage(headMemory.getUsed() * 100 / headMemory.getMax());
            }
        }else {
            systemMemoryInfo.setHeadMemoryUsedPercentage(0L);
        }

        MemoryUsage nonHeadMemory = memoryBean.getNonHeapMemoryUsage();
        systemMemoryInfo.setNonHeadMemoryInit(nonHeadMemory.getInit()/MB);
        systemMemoryInfo.setNonHeadMemoryMax(nonHeadMemory.getMax()/MB);
        systemMemoryInfo.setNonHeadMemoryUsed(nonHeadMemory.getUsed()/MB);
        systemMemoryInfo.setNonHeadMemoryCommitted(nonHeadMemory.getCommitted()/MB);
        if (nonHeadMemory.getMax() != 0) {
            if (nonHeadMemory.getMax() == -1) {
                systemMemoryInfo.setNonHeadMemoryUsedPercentage(0L);
            }else {
                systemMemoryInfo.setNonHeadMemoryUsedPercentage(nonHeadMemory.getUsed() * 100 / nonHeadMemory.getMax());
            }
        }else {
            systemMemoryInfo.setNonHeadMemoryUsedPercentage(0L);
        }

        return systemMemoryInfo;
    }
}
