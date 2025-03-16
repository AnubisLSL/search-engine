package com.anubis.li.searchengine.core.util;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class QueryUtil {

    private static final Map<Type, TypeHandler> queryTypeHandlersMap = new HashMap<>();
    static {
        queryTypeHandlersMap.put(String.class,new StringHandle());
        queryTypeHandlersMap.put(BigDecimal.class,new BigDecimalHandle());
        queryTypeHandlersMap.put(byte.class,new ByteHandle());
        queryTypeHandlersMap.put(Date.class,new DateHandle());
        queryTypeHandlersMap.put(double.class,new DoubleHandle());
        queryTypeHandlersMap.put(long.class,new LongHandle());
        queryTypeHandlersMap.put(float.class,new FloatHandle());
        queryTypeHandlersMap.put(int.class,new IntHandle());
        queryTypeHandlersMap.put(Time.class,new TimeHandle());
        queryTypeHandlersMap.put(Timestamp.class,new TimestampHandle());
        queryTypeHandlersMap.put(boolean.class,new BooleanHandle());
        queryTypeHandlersMap.put(byte[].class,new BytesHandle());
    }
    public static TypeHandler getTypeHandler(Type type) {
        return queryTypeHandlersMap.get(type);
    }

    public static Query createKeyFiledQuery(FieldConfig keyField, Map<String,Object> values) {
        //如果不存在数据
        if (values.size()==0){
            return null;
        }
        String keyValue = values.get(keyField.getFieldName()).toString();
        if (keyValue==null || keyValue.length()==0){
            return null;
        }
        return keyField.getHandler().eq(keyField.getFieldName(),  keyValue);
    }
    public static Query createKeyFiledQuery(FieldConfig keyField, Document document) {
        //如何不存在数据
        if (document == null ){
            return null;
        }
        //如何关键字段没有
        if (document.getField(keyField.getFieldName())==null){
            return null;
        }
        String keyValue = document.get(keyField.getFieldName()).toString();
        if (keyValue==null || keyValue.length()==0){
            return null;
        }
        return keyField.getHandler().eq(keyField.getFieldName(),  keyValue);
    }

}
