package com.el.smile.collector;

import com.el.smile.monitor.SystemRuntimeGrabber;
import com.el.smile.monitor.core.Grabber;

/**
 * @author eddie.lys
 * @since 2021/11/2
 */
public class Main {
    public static void main(String[] args) {
        Grabber<String> grabber = new SystemRuntimeGrabber();
        System.out.println(grabber.getInfo());
    }
}
