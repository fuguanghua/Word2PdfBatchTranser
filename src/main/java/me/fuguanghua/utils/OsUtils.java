package me.fuguanghua.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * os工具类
 */
public class OsUtils {

    private static String getOsName() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        return os.toLowerCase();
    }

    public static boolean isWindows() {
        String osName = getOsName();
        if (StringUtils.isBlank(osName)) {
            return false;
        }
        osName = osName.toLowerCase();
        return osName.indexOf("windows") > -1;
    }

    public static boolean isLinux() {
        String osName = getOsName();
        if (StringUtils.isBlank(osName)) {
            return false;
        }
        osName = osName.toLowerCase();
        return osName.indexOf("linux") > -1;
    }

    public static OS getOs() {
        String osName = getOsName();
        if (StringUtils.isBlank(osName)) {
            return OS.NOT_SUPPORT;
        }
        if (osName.indexOf("windows") > -1) {
            return OS.WINDOWS;
        }
        if (osName.indexOf("linux") > -1) {
            return OS.LINUX;
        }
        return OS.NOT_SUPPORT;
    }

    public enum OS {
        NOT_SUPPORT, WINDOWS, LINUX
    }
}
