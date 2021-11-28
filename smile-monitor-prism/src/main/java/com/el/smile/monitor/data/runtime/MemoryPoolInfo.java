package com.el.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 内存池信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class MemoryPoolInfo {

    private static final long MB = 1024 * 1024;

    /**
     * 内存池列表
     */
    private List<MemoryPool> memoryPools;

    @Data
    public static class MemoryPool {
        /**
         * 内存去民称
         */
        private String memoryName;
        /**
         * 所属内存管理者
         */
        private String memoryManagerName;
        /**
         * ObjectName
         */
        private String objectName;
        /**
         * 初始(M)
         */
        private Long memoryInit;
        /**
         * 最大(上限)(M)
         */
        private Long memoryMax;
        /**
         * 当前(已使用)(M)
         */
        private Long memoryUsed;
        /**
         * 提交的内存(已申请)(M)
         */
        private Long memoryCommitted;
        /**
         * 使用率
         */
        private Long memoryUsedPercentage;
    }

    public static MemoryPoolInfo analyseMemoryPoolInfo(List<MemoryPoolMXBean> memoryPoolBeanList) {
        MemoryPoolInfo memoryPoolInfo = new MemoryPoolInfo();
        if (memoryPoolBeanList != null && !memoryPoolBeanList.isEmpty()){
            List<MemoryPool> memoryPools = new ArrayList<>();
            for(MemoryPoolMXBean pool : memoryPoolBeanList){
                MemoryPool memoryPool = new MemoryPool();
                //只打印一些各个内存区都有的属性，一些区的特殊属性，可看文档或百度
                //最大值，初始值，如果没有定义的话，返回-1，所以真正使用时，要注意
                memoryPool.setMemoryName(pool.getName());
                memoryPool.setMemoryManagerName(Arrays.deepToString(pool.getMemoryManagerNames()));
                memoryPool.setObjectName(pool.getObjectName().toString());
                memoryPool.setMemoryInit(pool.getUsage().getInit()/MB);
                memoryPool.setMemoryMax(pool.getUsage().getMax()/MB);
                memoryPool.setMemoryUsed(pool.getUsage().getUsed()/MB);
                memoryPool.setMemoryCommitted(pool.getUsage().getCommitted()/MB);
                if (pool.getUsage().getMax() != 0) {
                    if (pool.getUsage().getMax() == -1) {
                        memoryPool.setMemoryUsedPercentage(0L);
                    }else {
                        memoryPool.setMemoryUsedPercentage(pool.getUsage().getUsed()*100/pool.getUsage().getMax());
                    }
                }else {
                    memoryPool.setMemoryUsedPercentage(0L);
                }
                memoryPools.add(memoryPool);
            }
            memoryPoolInfo.setMemoryPools(memoryPools);
        }
        return memoryPoolInfo;
    }
}
