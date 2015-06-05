package org.xobo.coke.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javassist.util.proxy.ProxyObject;

import org.apache.commons.beanutils.PropertyUtils;

public class BeanReflectionUtils {

	public static Collection<Field> loadClassFields(Class<?> clazz) {
		Collection<Field> fields = new ArrayList<Field>();
		Class<?> currentClazz = clazz;
		while (!currentClazz.equals(Object.class)) {
			fields.addAll(Arrays.asList(currentClazz.getDeclaredFields()));
			currentClazz = currentClazz.getSuperclass();
		}
		return fields;
	}

	public static Class<?> getClass(Object instance) {
		Class<?> clazz = instance.getClass();
		if (instance instanceof ProxyObject) {
			clazz = clazz.getSuperclass();
		}
		return clazz;
	}

	public static void mergeObjectWithAppointedProperties(Object target, Object source, String[] properties) {
		for (String property : properties) {
			try {
				Object targetValue = PropertyUtils.getProperty(target, property);
				Object sourceValue = PropertyUtils.getProperty(source, property);
				if (targetValue == null && sourceValue != null) {
					PropertyUtils.setProperty(target, property, sourceValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
