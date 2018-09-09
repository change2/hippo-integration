package com.change.hippo.utils.trans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by mike on 16/6/1.
 */
public abstract class BeanUtil {

    /**
     * trans java bean to hashmap
     * @param bean javabean
     * @return
     */
    public static Map<String, Object> bean2Map(Object bean) {
        Map<String, Object> map = new HashMap();
        bean2Map(bean, map);
        return map;
    }

    /**
     *  trans java bean to method map
     * @param bean javabean
     * @param map hash
     * @return
     */
    public static Map<String, Object> bean2Map(Object bean, Map map) {
        if (bean == null)
            return null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                String key = propertyDescriptor.getName();
                if (!key.equals("class")) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object invoke = readMethod.invoke(bean);
                    map.put(key, invoke);

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * trans to map use field
     * @param bean javaBean
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String,Object> bean2Map2(Object bean) throws IllegalAccessException {

        Field[] flelds = bean.getClass().getDeclaredFields();
        Map<String,Object> map = new HashMap();
        for (Field field : flelds){
            field.setAccessible(true);
            map.put(field.getName(),field.get(bean));
        }
        return map;
    }

    /**
     *trans java bean to sortedMap
     * @param bean javaBean
     * @return
     */
    public static SortedMap<String, Object> bean2SortedMap(Object bean) {
        SortedMap<String, Object> map = new TreeMap();
        bean2Map(bean, map);
        return map;
    }

}
