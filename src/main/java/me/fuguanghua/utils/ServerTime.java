package me.fuguanghua.utils;

/**
 * 事件工具类
 */
public class ServerTime {
	private static long offset = 0;

	public static long timeSecond() {
		return (System.currentTimeMillis() + offset) / 1000;
	}

    public static long timeMillis() {
        return System.currentTimeMillis() + offset;
    }

	public static void offset(long offset) {
		ServerTime.offset = offset;
	}
}
