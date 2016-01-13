package org.xobo.coke.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;

import com.bstek.dorado.data.provider.Page;

public class ClassUtils {
  private static Map<Type, InstanceCreator> classNewInstance = new HashMap<Type, InstanceCreator>();

  static class PageInstanceCreator implements InstanceCreator {
    public static Constructor<?> contructor = null;
    static {
      try {
        contructor = Page.class.getDeclaredConstructor(int.class, int.class);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public Object newInstance(Map<String, Object> parameter) {

      int pageSize = MapUtils.getIntValue(parameter, "pageSize", 20);
      int pageNo = MapUtils.getIntValue(parameter, "pageNo");
      try {
        return contructor.newInstance(pageSize, pageNo);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

  }

  public static interface InstanceCreator {
    public Object newInstance(Map<String, Object> parameter);
  }


  static {
    classNewInstance.put(Page.class, new PageInstanceCreator());
  }

  public static Object createInstance(Type type, Map<String, Object> value) {
    Object instance = null;
    InstanceCreator instanceCreator = classNewInstance.get(type);
    if (instanceCreator != null) {
      instance = instanceCreator.newInstance(value);
    } else {
      try {
        instance = type.getClass().newInstance();
        BeanUtils.populate(instance, (Map<String, ? extends Object>) value);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return instance;
  }

}
