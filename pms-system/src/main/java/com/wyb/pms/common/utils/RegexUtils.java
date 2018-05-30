package com.wyb.pms.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 正则工具
 * Created by weimingfj on 2017/9/8 9:08.
 */
public class RegexUtils {
    
    private static final Logger L = LoggerFactory.getLogger(RegexUtils.class);

    private static ThreadLocal<ConcurrentHashMap<String, Pattern>> patterns = new ThreadLocal<>();

    /**
     * 车牌
     * @return
     */
    public static Pattern truckPlate() {
        String regex = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}" +
                "[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$";
        return pattern(regex);
    }

    /**
     * ipv4地址。 0.0.0.0 - 255.255.255.255
     * @return
     */
    public static Pattern ipv4() {
        String _0_255 = "(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])";
        String regex = "^" + _0_255 + "(\\." + _0_255 + "){3}" + "$";
        return pattern(regex);
    }

    /**
     * 身份证号(18位)
     * @return
     */
    public static Pattern idno() {
        return pattern("^\\d{17}[0-9X]$");
    }

    /**
     * 地区编码
     * @return
     */
    public static Pattern areacode() {
        return pattern("^\\d{6}$");
    }

    /**
     * 文本(中英文)
     * @param length
     * @return
     */
    public static Pattern string(int length) {
        if (length <= 1) {
            return pattern("^.$");
        }
        return pattern(String.format("^.{1,%s}$", length));
    }

    /**
     * 数字文本
     * @param length
     * @return
     */
    public static Pattern number(int length) {
        if (length <= 1) {
            return pattern("^\\d$");
        }
        return pattern(String.format("^\\d{1,%s}$", length));
    }

    /**
     * 中文
     * @return
     */
    public static Pattern chinese() {
        return pattern("^[\\u4e00-\\u9fa5]+$");
    }

    /**
     * 手机号码
     * @return
     */
    public static Pattern mobile() {
        return pattern("^1[3-9]\\d{9}$");
    }

    /**
     * 金额, 两位小数 >=0
     * @return
     */
    public static Pattern moneyGe0() {
        String regex = "^(0|[1-9]\\d*)(\\.\\d{1,2})?$";
        return pattern(regex);
    }

    /**
     * 金额， 两位小数 （含正负）
     * @return
     */
    public static Pattern money() {
        return decimal(2);
    }

    /**
     * 小数，小数点后最多n位（含正负）
     * @param length
     * @return
     */
    public static Pattern decimal(int length) {
        if (length <= 0) {
            return pattern("^-?(0|[1-9]\\d*)$");
        }
        String regex = String.format("^-?(0|[1-9]\\d*)(\\.\\d{1,%s})?$", length);
        return pattern(regex);
    }

    /**
     * 大于0的整数
     * @return
     */
    public static Pattern intergetGt0() {
        return integerGt0(20);
    }

    /**
     * 大于0的n位整数
     * @param length
     * @return
     */
    public static Pattern integerGt0(int length) {
        if (length <= 1) {
            return pattern("^[1-9]$");
        } else {
            String regex = String.format("^[1-9]\\d{0,%s}$", length - 1);
            return pattern(regex);
        }
    }

    /**
     * 整数
     * @return
     */
    public static Pattern integer() {
        return integer(20);
    }

    /**
     * n位整数
     * @param length
     * @return
     */
    public static Pattern integer(int length) {
        if (length <= 1) {
            return pattern("^\\d$");
        } else {
            String regex = String.format("^0|[1-9]\\d{0,%s}$", length - 1);
            return pattern(regex);
        }

    }

    /**
     * 生成pattern
     * @param regex
     * @return
     */
    public static Pattern pattern(String regex) {
        return findPattern(regex);
    }

    /**
     * 匹配
     * @param regex
     * @param string
     * @return
     */
    public static boolean match(String regex, String string) {
        return pattern(regex).matcher(string).find();
    }

    private static Pattern findPattern(String regex) {
        Pattern p = readPattern(regex);
        if (null == p) {
            p = Pattern.compile(regex);
            writePattern(regex, p);
        }
        return p;
    }

    private static void writePattern(String regex, Pattern pattern) {
        ConcurrentHashMap<String, Pattern> map = patterns.get();
        if (null == map) {
            map = new ConcurrentHashMap<>();
        }
        map.put(regex, pattern);
    }

    private static Pattern readPattern(String regex) {
        ConcurrentHashMap<String, Pattern> map = patterns.get();
        if (null == map) {
            map = new ConcurrentHashMap<>();
            patterns.set(map);
        }
        if (map.containsKey(regex)) {
            return map.get(regex);
        } else {
            return null;
        }
    }
}
