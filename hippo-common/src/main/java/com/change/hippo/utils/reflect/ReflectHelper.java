package com.change.hippo.utils.reflect;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

/**
 *
 *
 * @author:fulong
 * @create:2017/8/6 16:27.
 */
public class ReflectHelper {

    private static final ConcurrentHashMap<Class<?>,List<Field>> fieldCaches = new ConcurrentHashMap<>();
    private static final ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    private ReflectHelper(){}

    private static final class Inner {
    	private static final ReflectHelper ins = new ReflectHelper();
	}

    public static ReflectHelper me(){
        return Inner.ins;
    }

	/**
	 * 获取类属性
	 * @param clazz
	 * @param annotation
	 * @return
	 */
    public List<Field> getFields(Class<?> clazz,Annotation annotation){
        List<Field> fields = fieldCaches.get(clazz);
        if(fields ==  null){
            fields = new ArrayList<>();
            try {
                for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                    for (Field field : clazz.getDeclaredFields()) {
                        int mod = field.getModifiers();
                        if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                            continue;
                        }
                        boolean addField = annotation==null;
                        if(annotation!=null){
							Annotation[] annotations = field.getAnnotations();
							for (Annotation item : annotations){
								if(item.equals(annotation)){
									addField = true;
									break;
								}
							}

						}
						if(addField){
							field.setAccessible(true);
							fields.add(field);
						}
                    }
                }
            } catch (Exception e) {
                throw new ReflectException("获取类属性失败",e);
            }
            fieldCaches.put(clazz,fields);
        }
        return fields;
    }

	/**
	 * 更新值
	 * @param ins
	 * @param propertyName
	 * @param newValue
	 * @return
	 */
    public ReflectHelper update(Object ins,String propertyName,String newValue){
    	return doUpdate(ins,propertyName,newValue,null);
	}

	/**
	 * 更新值
	 * @param ins
	 * @param propertyName
	 * @param newValue
	 * @param annotation
	 * @return
	 */
	public ReflectHelper update(Object ins,String propertyName,String newValue,Annotation annotation){
    	return doUpdate(ins,propertyName,newValue,annotation);
	}

    /**
     * 更新值
     * @param ins
     * @param propertyName
     * @param newValue
	 * @param annotation
     * @return
     */
    private ReflectHelper doUpdate(Object ins,String propertyName,String newValue,Annotation annotation){
        List<Field> fields = getFields(ins.getClass(),annotation);
        if(fields != null){
            for(Field field : fields){
                if(field.getName().equals(propertyName)){
                    try {
                        Class<?> fieldType = field.getType();
                        Object value = cast(fieldType,newValue);
                        field.set(ins,value);
                        break;
                    } catch (Exception e) {
                        throw new ReflectException("更新值失败",e);
                    }
                }
            }
        }
        return this;
    }

    /**
     * 转换值类型
     * @param fieldType
     * @param newValue
     * @return
     * @throws Exception
     */
    private Object cast(Class<?> fieldType,String newValue) throws Exception{
        if(fieldType == Integer.class || fieldType == int.class){
            return Integer.parseInt(StringUtils.isBlank(newValue)?"0":newValue);
        }else if(fieldType == Double.class || fieldType == double.class){
            return Double.parseDouble(StringUtils.isBlank(newValue)?"0":newValue);
        }else if(fieldType == Long.class || fieldType == long.class){
            return Long.parseLong(StringUtils.isBlank(newValue)?"0":newValue);
        }else if(fieldType == Float.class || fieldType == float.class){
            return Float.parseFloat(StringUtils.isBlank(newValue)?"0":newValue);
        }else if(fieldType == Date.class){
        	if(null!=newValue){
        		 return sdf.get().parse(newValue);
        	}
        }
        return newValue;
    }

}
