package com.el.smile.logger.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取公网IP
 * since 7/11/20
 *
 * @author eddie
 */
@Slf4j
public class PublicIpUtil {

    private static final Pattern IP_PATTERN = Pattern.compile("\\d+.\\d+.\\d+.\\d+");

    private static final String IP_REQUEST_URL = "http://202020.ip138.com";

    private static final String PUBLIC_IP_ADDRESS;

    private static final String LOCAL_IP_ADDRESS;

    static {
        PUBLIC_IP_ADDRESS = getIp();
        log.info("application public ip address: [{}]", PUBLIC_IP_ADDRESS);
        String localIpAddressTemp;
        try {
            localIpAddressTemp = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            localIpAddressTemp = "UN-KNOW";
        }
        LOCAL_IP_ADDRESS = localIpAddressTemp;
        log.info("application local ip address: [{}]", LOCAL_IP_ADDRESS);
    }

    public static String getPublicIpAddress() {
        return PUBLIC_IP_ADDRESS;
    }

    public static String getLocalIpAddress() {
        return LOCAL_IP_ADDRESS;
    }

    private static String getIp() {
        String result = doGet();
        if (StringUtils.isBlank(result)) {
            return "";
        }
        Matcher matcher = IP_PATTERN.matcher(result);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private static String doGet() {
        String message = "";
        try {
            URL url = new URL(PublicIpUtil.IP_REQUEST_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                byte[] data = new byte[1024];
                StringBuilder builder = new StringBuilder();
                int length;
                while ((length = inputStream.read(data)) != -1) {
                    String s = new String(data, 0, length);
                    builder.append(s);
                }
                message = builder.toString();
                inputStream.close();
            }
            //关闭连接
            connection.disconnect();
        } catch (Exception e) {
            return "";
        }
        return message;
    }
}
