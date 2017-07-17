package net.gini.switchsdk.utils;


import android.support.annotation.RestrictTo;
import android.util.Log;

public class Logging {

    private static final String TAG = "Gini Switch SDK";

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static LogLevel LOG_LEVEL = LogLevel.ALL;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static boolean SHOW_LOGS = false;

    private Logging() {
    }

    public static void d(final String message) {
        if (shouldShowLog(LogLevel.DEBUG)) {
            Log.d(TAG, message);
        }
    }

    public static void e(final String message) {
        if (shouldShowLog(LogLevel.ERROR)) {
            Log.e(TAG, message);
        }
    }

    public static void e(final String message, final Exception exception) {
        if (shouldShowLog(LogLevel.ERROR)) {
            Log.e(TAG, message, exception);
        }
    }

    public static void i(final String message) {
        if (shouldShowLog(LogLevel.INFO)) {
            Log.i(TAG, message);
        }
    }

    public static void v(final String message) {
        if (shouldShowLog(LogLevel.VERBOSE)) {
            Log.v(TAG, message);
        }
    }

    public static void w(final String message) {
        if (shouldShowLog(LogLevel.WARN)) {
            Log.w(TAG, message);
        }
    }

    private static boolean shouldShowLog(final LogLevel logLevel) {
        return (LOG_LEVEL == LogLevel.ALL || LOG_LEVEL == logLevel) && SHOW_LOGS;
    }

    public enum LogLevel {
        ALL, VERBOSE, DEBUG, ERROR, WARN, INFO
    }
}
