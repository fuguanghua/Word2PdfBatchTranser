package me.fuguanghua.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具类
 */
public class ReflectUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(ReflectUtils.class);

	public static List<Class<?>> reflectFieldType(Field field) {
		List<Class<?>> clazzList = new ArrayList<>();

		Class<?> fieldType = field.getType();
		clazzList.add(fieldType);

		if (fieldType == List.class) {
			Type valueType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			clazzList.add((Class<?>) valueType);
		}

		if (fieldType == Set.class) {
			Type valueType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			clazzList.add((Class<?>) valueType);
		}

		if (fieldType == Map.class) {
			Type keyType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			clazzList.add((Class<?>) keyType);

			Type valueType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];
			try {
				clazzList.add((Class<?>) valueType);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return clazzList;
	}

	public static Object getFieldValue(Field field, Object instance) {
		if (field == null) {
			return null;
		}
		if (instance == null) {
			return null;
		}
		try {
			Object value = field.get(instance);
			return value;
		} catch (IllegalArgumentException e) {
			LOGGER.error("", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("", e);
		}
		return null;
	}
}
