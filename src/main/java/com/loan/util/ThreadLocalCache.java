package com.loan.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author:
 * @time: 2019/12/8 19:47
 */
public final class ThreadLocalCache {

    private ThreadLocalCache() {
    }

    private static final ThreadLocal<Map<String, Object>> STORE = ThreadLocal.withInitial(HashMap::new);

    public static Object get(KeyEnum key) {
        return STORE.get().get(key.name());
    }

    public static void put(KeyEnum key, Object value) {
        STORE.get().put(key.name(), value);
    }

    public static void clear() {
        STORE.remove();
    }

    public enum KeyEnum {
        /**
         * 系统配置
         */
        SYSTEM_CONFIG,
        /**
         * 网站设置
         */
        WEBSITE_CONFIG,
        /**
         * 用户
         */
        USER,
        ;
    }
}
