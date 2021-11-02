package com.el.smile.monitor.core;

/**
 * 抓取器
 * @author eddie.lys
 * @since 2021/11/2
 */
public interface Grabber<T> {

    /**
     * 抓取信息
     * @return  信息
     */
    T getInfo();

}
