package com.digicert.consent.logging;

/**
 * Store thread local log data.
 */
public class LogSession {
    private LogSession() {
    }

    private static final ThreadLocal<LogState> logStateThreadLocal = new ThreadLocal<>();

    public static void set(LogState value) {
        logStateThreadLocal.set(value);
    }

    public static LogState get() {
        LogState logState = logStateThreadLocal.get();
        if (logState == null) {
            logState = new LogState();
            set(logState);
        }
        return logState;
    }

    public static void remove() {
        logStateThreadLocal.remove();
    }
}
