package com.yunma.utils;

/*
 * Created on 2017-03-13
 *
 * @author Json.
 */

import java.util.List;

/**
 * 判断对象是否为空
 *
 * @author Rex
 *
 */

public class EmptyUtil {
    /**
     * 判断对象为空
     *
     * @param obj
     *            对象名
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if ((obj instanceof List)) {
            return ((List) obj).size() == 0;
        }
        return (obj instanceof String) && ((String) obj).trim().equals("");
    }

    /**
     * 判断对象不为空
     *
     * @param obj
     *            对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
}
