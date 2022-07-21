package com.olympus.smile.monitor.data.runtime;

import lombok.Data;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * java线程信息
 * since 11/28/21
 *
 * @author eddie
 */
@Data
public class JvmThreadInfo {
    /**
     * ObjectName
     */
    private String objectName;
    /**
     * 仍活动的线程总数
     */
    private Integer threadCount;
    /**
     * 峰值
     */
    private Integer peakThreadCount;
    /**
     * 线程总数（被创建并执行过的线程总数）
     */
    private Long totalStartedThreadCount;
    /**
     * 当初仍活动的守护线程（daemonThread）总数
     */
    private Integer daemonThreadCount;
    /**
     * 系统运行死锁列表
     */
    private List<DeadLockedThread> deadLockedThreads;
    /**
     * 系统运行列表
     */
    private List<ThreadMessage> threadMessages;

    @Data
    public static class DeadLockedThread {
        /**
         * 线程ID
         */
        private Long threadId;
        /**
         * 线程名称
         */
        private String threadName;
        /**
         * 线程状态
         */
        private String threadState;
        /**
         * 锁定事件
         */
        private Long blockedTime;
        /**
         * 等待事件
         */
        private Long waitedTime;
        /**
         * 线程堆栈
         */
        private String stackTrace;
    }

    @Data
    public static class ThreadMessage {
        /**
         * 线程ID
         */
        private Long threadId;
        /**
         * 线程名称
         */
        private String threadName;
        /**
         * 线程状态
         */
        private String threadState;
    }

    public static JvmThreadInfo analyseThreadInfo(ThreadMXBean threadBean) {
        JvmThreadInfo jvmThreadInfo = new JvmThreadInfo();
        jvmThreadInfo.setObjectName(threadBean.getObjectName().toString());
        jvmThreadInfo.setThreadCount(threadBean.getThreadCount());
        jvmThreadInfo.setPeakThreadCount(threadBean.getPeakThreadCount());
        jvmThreadInfo.setTotalStartedThreadCount(threadBean.getTotalStartedThreadCount());
        jvmThreadInfo.setDaemonThreadCount(threadBean.getDaemonThreadCount());

        //检查是否有死锁的线程存在
        long[] deadlockedIds =  threadBean.findDeadlockedThreads();
        if(deadlockedIds != null && deadlockedIds.length > 0){
            ThreadInfo[] deadlockInfos = threadBean.getThreadInfo(deadlockedIds);
            List<DeadLockedThread> deadLockedThreads = new ArrayList<>();
            for(ThreadInfo deadlockInfo : deadlockInfos){
                DeadLockedThread deadLockedThread = new DeadLockedThread();
                deadLockedThread.setThreadId(deadlockInfo.getThreadId());
                deadLockedThread.setThreadName(deadlockInfo.getThreadName());
                deadLockedThread.setThreadState(deadlockInfo.getThreadState().name());
                deadLockedThread.setBlockedTime(deadlockInfo.getBlockedTime());
                deadLockedThread.setWaitedTime(deadlockInfo.getWaitedTime());
                deadLockedThread.setStackTrace(Arrays.toString(deadlockInfo.getStackTrace()));
                deadLockedThreads.add(deadLockedThread);
            }
            jvmThreadInfo.setDeadLockedThreads(deadLockedThreads);
        }

        long[] threadIds = threadBean.getAllThreadIds();
        if(threadIds != null && threadIds.length > 0){
            ThreadInfo[] threadInfos = threadBean.getThreadInfo(threadIds);
            List<ThreadMessage> threadMessages = new ArrayList<>();
            for(ThreadInfo threadInfo : threadInfos){
                ThreadMessage threadMessage = new ThreadMessage();
                threadMessage.setThreadId(threadInfo.getThreadId());
                threadMessage.setThreadName(threadInfo.getThreadName());
                threadMessage.setThreadState(threadInfo.getThreadState().name());
                threadMessages.add(threadMessage);
            }
            jvmThreadInfo.setThreadMessages(threadMessages);
        }

        return jvmThreadInfo;
    }
}
