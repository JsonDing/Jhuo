package com.yunma.utils;


import java.lang.reflect.Method;
import java.util.*;

/**
 * Created on 2017-03-31
 *
 * @author Json.
 */

public class SortList<T> {
    /**
     * @param targetList 目标排序List
     * @param sortField 排序字段(实体类属性名)
     * @param sortMode 排序方式（asc or  desc）
     */

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void sort(List<T> targetList, final String sortField, final String sortMode) {

        Collections.sort(targetList, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                int retVal ;
                try {
                    //首字母转大写
                    String newStr=sortField.substring(0, 1).toUpperCase()+sortField.replaceFirst("\\w","");
                    String methodStr="get"+newStr;

                    Method method1 = ((T)obj1).getClass().getMethod(methodStr);
                    Method method2 = ((T)obj2).getClass().getMethod(methodStr);
                    if (sortMode != null && "desc".equals(sortMode)) {
                       // retVal = method2.invoke(obj2).toString().compareTo(method1.invoke(obj1).toString()); // 倒序
                        retVal = Integer.valueOf(method2.invoke(obj2).toString())
                                .compareTo(Integer.valueOf(method1.invoke(obj1).toString()));
                    } else {
                      //  retVal = method1.invoke(obj1).toString().compareTo(method2.invoke(obj2).toString()); // 正序
                        retVal = Integer.valueOf(method1.invoke(obj1).toString())
                                .compareTo(Integer.valueOf(method2.invoke(obj2).toString()));
                    }
                } catch (Exception e) {
                    throw new RuntimeException();
                }
                return retVal;
            }
        });
    }
}
