package me.fuguanghua.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     日志包装
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public class LoggerUtils {

    public static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static Logger Logger(LogFileName desc) {
        return LoggerFactory.getLogger(desc.getLogFileName());
    }
}
