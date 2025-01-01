package com.example.cxx.utils;

public class ThreadLocalUtil {
    // 提供threadlocal对象, 本地线程，里面存储当前线程独有的变量
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    // 根据键获取值
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    // 存储键值对, val键值对
    public static void set(Object val) {
        THREAD_LOCAL.set(val);
    }

    // 清除threadlocal，防止内存泄露
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
