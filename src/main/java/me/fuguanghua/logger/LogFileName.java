package me.fuguanghua.logger;

import me.fuguanghua.utils.StringUtils;

/**
 * <p>
 *     日志包装
 * </p>
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
public enum LogFileName {
    CONSOLE("console"),
    WORD2PDF("word2pdf");

    private String logFileName;

    LogFileName(String fileName) {
        this.logFileName = fileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public static LogFileName getAwardTypeEnum(String value) {
        LogFileName[] arr = values();
        for (LogFileName item : arr) {
            if (null != item && StringUtils.isNotBlank(item.logFileName)) {
                return item;
            }
        }
        return null;
    }
}
