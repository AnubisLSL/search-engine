package com.anubis.li.searchengine.core.common.utils;

public class StringUtil {
    public static boolean isEmpty(String str)
    {
        return str == null || str.length() == 0;
    }
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }
    public static String assertNull(String str,String assertMsg)
    {
        return isEmpty(str) ? assertMsg : "";
    }
    public static boolean isNotNull(Object obj)
    {
        return null != obj && false == obj.equals(null);
    }

    public static String getTaskName(String taskName){
        // 查找第一个下划线的位置
        int index = taskName.indexOf('_');
        // 截取下划线之后的部分
        return taskName.substring(index + 1);
    }
}
