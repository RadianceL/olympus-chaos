package com.olympus.smile.collector;

import com.alibaba.fastjson.JSON;
import com.olympus.smile.monitor.SystemRuntimeGrabber;
import com.olympus.smile.monitor.core.Grabber;
import com.olympus.smile.monitor.data.SystemRuntime;

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
