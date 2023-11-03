package com.olympus.logger.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 获取公网IP
 * since 7/11/20
 *
 * @author eddie
 */
@Slf4j
public class LocalIpUtil {

    private static final String LOCAL_IP_ADDRESS;

    static {
        String localIpAddressTemp;
        try {
            localIpAddressTemp = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            localIpAddressTemp = "UN-KNOW";
        }
        LOCAL_IP_ADDRESS = localIpAddressTemp;
        log.info("application local ip address: [{}]", LOCAL_IP_ADDRESS);
    }

    public static String getLocalIpAddress() {
        return LOCAL_IP_ADDRESS;
    }
}
