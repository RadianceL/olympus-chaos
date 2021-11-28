package com.el.smile.collector;

import com.alibaba.fastjson.JSON;
import com.el.smile.monitor.SystemRuntimeGrabber;
import com.el.smile.monitor.core.Grabber;
import com.el.smile.monitor.data.SystemRuntime;

/**
 * @author eddie.lys
 * @since 2021/11/2
 */
public class Main {
    public static void main(String[] args) {
        Grabber<SystemRuntime> grabber = new SystemRuntimeGrabber();
        System.out.println(JSON.toJSONString(grabber.getInfo()));
    }
}
