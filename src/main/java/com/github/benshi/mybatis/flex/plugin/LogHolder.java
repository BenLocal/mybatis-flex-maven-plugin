package com.github.benshi.mybatis.flex.plugin;

import org.apache.maven.plugin.logging.Log;

/**
 * 
 * 
 * @date 2025年6月17日
 * @time 22:44:06
 * @description
 * 
 */
public class LogHolder {
    private static Log logger;

    public static void setLog(Log log) {
        logger = log;
    }

    public static Log getLog() {
        return logger;
    }

    public static void info(String message) {
        if (logger != null) {
            logger.info(message);
        }
    }

    public static void warn(String message) {
        if (logger != null) {
            logger.warn(message);
        }
    }

    public static void error(String message) {
        if (logger != null) {
            logger.error(message);
        }
    }

    public static void debug(String message) {
        if (logger != null && logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}
