package com.wyb.pms.common.utils;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 *
 * Created by weiming on 2017/2/14 15:28.
 */
public class MapUtils {

    /**
     * 转化为http请求参数字符串
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String toHttpParamString(Map<K, V> map) {
        if (null == map || map.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (K key : map.keySet()) {
            V val = map.get(key);
            String k = key == null ? "" : String.valueOf(key);
            String v = val == null ? "" : String.valueOf(val);
            sb.append("&").append(key).append("=").append(val);
        }
        return sb.substring(1);
    }

    /**
     *
     * @param map
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String getString(Map<K, V> map, K key, String defaultVal) {
        V v = map.get(key);
        return null == v ? defaultVal : String.valueOf(v);
    }


    /**
     *
     * @param map
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String getStringForce(Map<K, V> map, K key, String defaultVal) {
        V v = map.get(key);
        return (null == v || StringUtils.isEmpty(String.valueOf(v))) ? defaultVal : String.valueOf(v);
    }

    /**
     *
     * @param map
     * @param key
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> String getString(Map<K, V> map, K key) {
        return getString(map, key, "");
    }

    /**
     * 从map中选取指定的key，生成一个新的map
     * @param map
     * @param keys
     * @param <K>
     * @param <V>
     * @return
     */
    @SafeVarargs
    public static <K, V> Map<K, V> filter(Map<K, V> map, K... keys) {
        Map<K, V> res = new HashMap<>();
        if (null == keys || keys.length == 0) {
            return res;
        }
        for (K key : keys) {
            res.put(key, map.get(key));
        }
        return res;
    }

    public static int getInt(Map<String, ?> map, String key, int def) {
        int value=def;
        try{
            value=Double.valueOf(getString(map,key)).intValue();
        }catch(Exception e){
            e.printStackTrace();
        }
        return value;
    }
    public static int getInt(Map<String, ?> map, String key) {
        return getInt(map, key, 0);
    }
    public static float getFloat(Map<String, Object> map, String key, int def) {

        return Float.parseFloat(getString(map,key,String.valueOf(def)));
    }
    public static float getFloat(Map<String, Object> map, String key) {
        return getFloat(map, key, 0);
    }
    public static double getDouble(Map<String, Object> map, String key, int def) {
        return Double.parseDouble(getString(map,key,String.valueOf(def)));
    }
    public static double getDouble(Map<String, Object> map, String key) {
        return getDouble(map, key, 0);
    }

    /**
     * 从map中删除指定的key
     * @param map
     * @param excludeKeys
     * @param <K>
     * @param <V>
     * @return
     */
    @SafeVarargs
    public static <K, V> Map<K, V> exclude(Map<K, V> map, K... excludeKeys) {
        Map<K, V> res = new HashMap<>();
        if (null == excludeKeys || excludeKeys.length == 0) {
            return map;
        }
        Set<K> exkeys = new HashSet<>();
        Collections.addAll(exkeys, excludeKeys);
        for (K key : map.keySet()) {
            if (exkeys.contains(key)) {
                continue;
            }
            res.put(key, map.get(key));
        }
        return res;
    }


    public static <K, V> Map<K, V> build(K[] keys, V[] values) {
        Map<K, V> map = new HashMap<>();
        if (null != keys) {
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], (null != values && i < values.length) ? values[i] : null);
            }
        }
        return map;
    }

    public static <K, V> Map<K, V> build(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    /**
     * 并集
     * @param srcMap
     * @param tarMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> unionMap(Map<K, V> srcMap, Map<K, V> tarMap) {
        Set<K> srcSet = srcMap.keySet();
        Set<K> tarSet = tarMap.keySet();
        Set<K> unionSet = unionSets(srcSet, tarSet);
        Map<K, V> res = new HashMap<>();
        for (K key : unionSet) {
            if (srcMap.containsKey(key)) {
                res.put(key, srcMap.get(key));
            } else {
                res.put(key, tarMap.get(key));
            }
        }
        return res;
    }

    /**
     * 差集
     * @param srcMap
     * @param tarMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> differenceMap(Map<K, V> srcMap, Map<K, V> tarMap) {
        Set<K> srcSet = srcMap.keySet();
        Set<K> tarSet = tarMap.keySet();
        Set<K> diffSet = differenceSets(srcSet, tarSet);
        Map<K, V> res = new HashMap<>();
        for (K key : diffSet) {
            res.put(key, srcMap.get(key));
        }
        return res;
    }

    /**
     * 交集
     * @param srcMap
     * @param tarMap
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> intersectionMap(Map<K, V> srcMap, Map<K, V> tarMap) {
        Set<K> srcSet = srcMap.keySet();
        Set<K> tarSet = tarMap.keySet();
        Set<K> interSet = intersectionSets(srcSet, tarSet);
        Map<K, V> res = new HashMap<>();
        for (K key : interSet) {
            res.put(key, srcMap.get(key));
        }
        return res;
    }

    /**
     * 差集
     * @param srcSet
     * @param tarSet
     * @param <K>
     * @return
     */
    private static <K> Set<K> differenceSets(Set<K> srcSet, Set<K> tarSet) {
        Set<K> resKeys = new HashSet<>();
        resKeys.addAll(srcSet);
        resKeys.removeAll(tarSet);
        return resKeys;
    }

    /**
     * 交集
     * @param srcSet
     * @param tarSet
     * @param <K>
     * @return
     */
    private static <K> Set<K> intersectionSets(Set<K> srcSet, Set<K> tarSet) {
        Set<K> resKeys = new HashSet<>();
        resKeys.addAll(srcSet);
        resKeys.retainAll(tarSet);
        return resKeys;
    }

    /**
     * 并集
     * @param srcSet
     * @param tarSet
     * @param <K>
     * @return
     */
    private static <K> Set<K> unionSets(Set<K> srcSet, Set<K> tarSet) {
        Set<K> resKeys = new HashSet<>();
        resKeys.addAll(srcSet);
        resKeys.addAll(tarSet);
        return resKeys;
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        BeanUtils.populate(obj, map);

        return obj;
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if(obj == null)
            return null;

        return new BeanMap(obj);
    }

}
