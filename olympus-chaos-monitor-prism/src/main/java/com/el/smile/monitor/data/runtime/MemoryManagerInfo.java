package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.MemoryManagerMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 内存使用情况
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class MemoryManagerInfo {
    /**
     * java 内存空间
     */
    private List<JavaMemoryInfo> javaMemoryInfos;

    @Data
    public static class JavaMemoryInfo {
        /**
         * vm内存管理器名称
         */
        private String jvmName;
        /**
         * 管理的内存区
         */
        private String memoryPoolName;
        /**
         * ObjectName
         */
        private String objectName;
    }

    public static MemoryManagerInfo analyseMemoryManagerInfo(List<MemoryManagerMXBean> memoryManagerBeans) {
        MemoryManagerInfo memoryManagerInfo = new MemoryManagerInfo();
        if(memoryManagerBeans != null && !memoryManagerBeans.isEmpty()){
            List<JavaMemoryInfo> javaMemoryInfos = new ArrayList<>();
            for(MemoryManagerMXBean manager : memoryManagerBeans){
                JavaMemoryInfo javaMemoryInfo = new JavaMemoryInfo();
                javaMemoryInfo.setJvmName(manager.getName());
                javaMemoryInfo.setMemoryPoolName(Arrays.deepToString(manager.getMemoryPoolNames()));
                javaMemoryInfo.setObjectName(manager.getObjectName().toString());
                javaMemoryInfos.add(javaMemoryInfo);
            }
            memoryManagerInfo.setJavaMemoryInfos(javaMemoryInfos);
        }
        return memoryManagerInfo;
    }
}
