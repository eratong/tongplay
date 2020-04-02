package com.tongplay.tongplay.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class SignUtil {
    private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 构建key=value&key=value形式的待签名字符串
     *
     * @param parameters
     * @return
     */
    private static String waitForSignString(Map<String, Object> parameters) {
        StringBuilder text = new StringBuilder();
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(parameters.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {

            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
        for (Map.Entry<String, Object> tmpMap : infoIds) {
            String value = String.valueOf(tmpMap.getValue());
            if (!StringUtils.isEmpty(value)) {
                text.append(tmpMap.getKey()).append("=").append(value).append("&");
            }
        }
        text = new StringBuilder(text.substring(0, text.length() - 1));
        return text.toString();
    }

    /**
     * 构建key=value&key=value形式的待签名字符串，并在字符串后追加&key=value，最后计算md5值
     *
     * @param parameters
     * @param key
     * @return
     */
    public static String md5Sign(Map<String, Object> parameters, String key) {
        String text = "";
        String waitForSignString = waitForSignString(parameters);
        text = waitForSignString + "&key=" + key;
        logger.debug("待签名字符串:" + text);
        byte[] strByteArray = null;
        try {
            strByteArray = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("待签名字符串转byte[]出错", e);
            return null;
        }
        return DigestUtils.md5DigestAsHex(strByteArray).toUpperCase();
    }

    public static String md5SignNoLog(Map<String, Object> parameters, String key) {
        String text = "";
        String waitForSignString = waitForSignString(parameters);
        text = waitForSignString + "&key=" + key;
        byte[] strByteArray = null;
        try {
            strByteArray = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("待签名字符串转byte[]出错", e);
            return null;
        }
        return DigestUtils.md5DigestAsHex(strByteArray).toUpperCase();
    }

    private static String waitForSignStringTen(Map<String, Object> parameters) {
        StringBuilder text = new StringBuilder();
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(parameters.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {

            public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                return (o1.getKey()).compareTo(o2.getKey());
            }
        });
        for (Map.Entry<String, Object> tmpMap : infoIds) {
            String value = String.valueOf(tmpMap.getValue());
            if (!StringUtils.isEmpty(value)) {

                text.append(tmpMap.getKey()).append("=").append(URLEncoder.encode(value)).append("&");
            }
        }
        text = new StringBuilder(text.substring(0, text.length() - 1));
        return text.toString();
    }

    public static String md5SignTen(Map<String, Object> parameters, String key) {
        String text = "";
        String waitForSignString = waitForSignStringTen(parameters);
        text = waitForSignString + "&app_key=" + key;
//        logger.info("待签名字符串:" + text);
        byte[] strByteArray = null;
        try {
            strByteArray = text.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("待签名字符串转byte[]出错", e);
            return null;
        }
        return DigestUtils.md5DigestAsHex(strByteArray).toUpperCase();
    }
}
