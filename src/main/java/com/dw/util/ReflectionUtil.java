package com.dw.util;

import org.apache.commons.collections.map.HashedMap;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by lodi on 2018/4/27.
 */
public class ReflectionUtil {

    public static  String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  (String)field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String,Object> getFieldsMap(Object object) throws IllegalAccessException {
        Map<String,Object> map = new HashedMap();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
        }
        Field[] parentFields = object.getClass().getSuperclass().getDeclaredFields();
        for(Field field : parentFields){
            field.setAccessible(true);
            map.put(field.getName(),field.get(object));
        }

        return  map;
    }


    public static  String getClassName(Object object){
        //获取obj的类类型
        Class c1=object.getClass();
        return c1.getSimpleName();
    }

}
