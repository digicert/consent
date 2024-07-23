package com.digicert.consent.logging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtils {

    private LogUtils() {
    }

    public static void error(String logStr, Object... objs) {
        log.error(prependTransactionIdToStr(logStr), prependTransactionIdToObjects(objs));
    }

    public static void warn(String logStr, Object... objs) {
        log.warn(prependTransactionIdToStr(logStr), prependTransactionIdToObjects(objs));
    }

    public static void info(String logStr, Object... objs) {
        log.info(prependTransactionIdToStr(logStr), prependTransactionIdToObjects(objs));
    }

    public static void debug(String logStr, Object... objs) {
        log.debug(prependTransactionIdToStr(logStr), prependTransactionIdToObjects(objs));
    }

    static String prependTransactionIdToStr(String logStr) {
        return logStr != null ? "transaction_id={} " + logStr : "transaction_id={}";
    }

    static Object[] prependTransactionIdToObjects(Object... objs) {
        int length = objs != null ? objs.length + 1 : 1;
        Object[] logObjs = new Object[length];
        logObjs[0] = LogSession.get().getTransactionId();

        if (objs != null) {
            System.arraycopy(objs, 0, logObjs, 1, objs.length);
        }

        return logObjs;
    }
}
