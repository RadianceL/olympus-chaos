package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 垃圾收集器信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class GarbageCollectorInfo {
    /**
     * jvm 垃圾收集器列表
     */
    private List<GarbageCollector> garbageCollectors;

    @Data
    public static class GarbageCollector {
        /**
         * 垃圾收集器名称
         */
        private String garbageName;
        /**
         * 内存区名称
         */
        private String memoryPoolName;
        /**
         * 总花费时间
         */
        private Long garbageCollectionTime;
        /**
         * 收集次数
         */
        private Long garbageCollectionCount;
    }

    public static GarbageCollectorInfo analyseGarbageCollectorInfo(List<GarbageCollectorMXBean> garbageList) {
        GarbageCollectorInfo garbageCollectorInfo = new GarbageCollectorInfo();
        List<GarbageCollector> garbageCollectors = new ArrayList<>();
        for(GarbageCollectorMXBean garbage : garbageList){
            GarbageCollector garbageCollector = new GarbageCollector();
            garbageCollector.setGarbageName(garbage.getName());
            garbageCollector.setMemoryPoolName(Arrays.deepToString(garbage.getMemoryPoolNames()));
            garbageCollector.setGarbageCollectionCount(garbage.getCollectionCount());
            garbageCollector.setGarbageCollectionTime(garbage.getCollectionTime());
            garbageCollectors.add(garbageCollector);
        }
        garbageCollectorInfo.setGarbageCollectors(garbageCollectors);
        return garbageCollectorInfo;
    }
}
