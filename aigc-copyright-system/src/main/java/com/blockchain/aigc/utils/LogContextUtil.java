package com.blockchain.aigc.utils;

/**
 * @author gaoxinyu
 * @date 2026/3/26 19:35
 **/
public class LogContextUtil {

    private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();

    public static void set(Long targetId) {
        CONTEXT.set(targetId);
    }

    public static Long get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
