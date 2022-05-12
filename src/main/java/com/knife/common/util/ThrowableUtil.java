package com.knife.common.util;

/**
 * 异常工具类
 */
public class ThrowableUtil {
    /**
     * 获取以指定包名为前缀的堆栈信息
     *
     * @param e             异常
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable e) {
        StringBuilder s = new StringBuilder().append(e);
        for (StackTraceElement traceElement : e.getStackTrace()) {
            s.append("    at ").append(traceElement);
        }
        return s.toString();
    }

    /**
     * 获取以指定包名为前缀的堆栈信息
     *
     * @param e             异常
     * @param packagePrefix 包前缀
     * @return 堆栈信息
     */
    public static String getStackTraceByPackage(Throwable e, String packagePrefix) {
        StringBuilder s = new StringBuilder().append(e);
        for (StackTraceElement traceElement : e.getStackTrace()) {
            if (!traceElement.getClassName().startsWith(packagePrefix)) {
                break;
            }
            s.append("    at ").append(traceElement);
        }
        return s.toString();
    }

    /**
     * 获取最后n条堆栈信息
     * @param e 异常对象
     * @param n 最后n条
     * @return 错误信息
     */
    public static String getLastStackTrace(Throwable e, Integer n) {
        Integer lineNumber = n;
        if (lineNumber == null) {
            lineNumber = 10;
        }

        StringBuilder s = new StringBuilder().append(e);
        int i = 0;
        for (StackTraceElement traceElement : e.getStackTrace()) {
            s.append("    at ").append(traceElement);
            if (i >= lineNumber) {
                break;
            }
            i++;
        }
        return s.toString();
    }
}
