package com.wyb.pms.common.utils;

import java.util.Map;

/**
 *
 * Created by weimingfj on 2017/3/27 14:09.
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return null == str || "".equals(str);
    }

    public static final char UNDERLINE = '_';

    public static boolean isAnyEmpty(String... strings) {
        if (null == strings || strings.length == 0) {
            return true;
        }
        for (String str : strings) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAllEmpty(String... strings) {
        if (null == strings || strings.length == 0) {
            return true;
        }
        for (String str : strings) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    public static String asUrlString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (null == params || params.isEmpty()) {
            return sb.toString();
        }
        for (String key : params.keySet()) {
            sb.append("&").append(key).append("=").append(params.get(key));
        }
        return sb.substring(1);
    }

    public static String underline2Camel(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(str.charAt(i)));
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String camel2underline(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE).append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
