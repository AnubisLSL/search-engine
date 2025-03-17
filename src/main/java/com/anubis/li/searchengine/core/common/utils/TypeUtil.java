package com.anubis.li.searchengine.core.common.utils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

public class TypeUtil
{
    //todo  Oracle中的类型 java.sql.Array    SUBSCRIBERS ANYDATA   STRUCT这几种类型为处理
    public static Class<?> SQLTypeToJavaType(int sqlType) {
        switch (sqlType) {
            case Types.NCHAR:
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.LONGVARCHAR:
            case Types.ROWID:
                return String.class;
            case Types.INTEGER:
                return int.class;
            case Types.DATE:
                // Date 类不是基本类型，保持不变
                return Date.class;
            case Types.TIME:
                // Time 类不是基本类型，保持不变
                return Time.class;
            case Types.TIMESTAMP:
                //Oracle 的TYPE_NAME:TIMESTAMP(6) WITH TIME ZONE,DATA_TYPE-101
            case -101:
                return Timestamp.class;
            case Types.BIT:
                return boolean.class;
            case Types.TINYINT:
                return byte.class;
            case Types.SMALLINT:
                return short.class;
            case Types.BIGINT:
                return long.class;
            case Types.REAL:
                return float.class;
            case Types.FLOAT:
            case Types.DOUBLE:
                return double.class;
            case Types.NUMERIC:
            case Types.DECIMAL:
                return BigDecimal.class;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.BLOB:
            case Types.CLOB:
            case Types.NCLOB:
                return byte[].class;
            case Types.ARRAY:
                return java.sql.Array.class;
            default:
                throw new IllegalArgumentException("Unsupported SQL type: " + sqlType);
        }
    }

    public static Type getType(String type){
        String typeName = type.toLowerCase();
        switch (typeName){
            case "string":
                return String.class;
            case "int":
                return int.class;
            case "boolean":
                return boolean.class;
            case "date":
                return Date.class;
            case "byte":
                return byte.class;
            case "short":
                return short.class;
            case "long":
                return long.class;
            case "float":
                return float.class;
            case "double":
                return double.class;
            case "time":
                return Time.class;
            case "byte[]":
                return byte[].class;
            case "timestamp":
                return Timestamp.class;
            case "bigdecimal":
                return BigDecimal.class;
            default:
                throw new IllegalArgumentException("Unsupported SQL type: " + type);
        }
    }
    /**
     * 判断是否为二进制类型
     * */

    public static boolean isBinaryColumn(int sqlType){
        //Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY,Types.BLOB,Types.CLOB,Types.NCLOB
        if (sqlType ==-2 || sqlType ==-3 || sqlType ==-4 || sqlType ==2004 || sqlType ==2005 || sqlType ==2011 ){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isStringColumn(int sqlType){

        if (sqlType ==-15 || sqlType == 1 || sqlType ==12 || sqlType ==-9 || sqlType ==-1  ){
            return true;
        }else {
            return false;
        }
    }
    public static boolean isDatetimeColumn(String sqlTypeName) {
        return  "datetime".equalsIgnoreCase(sqlTypeName);
    }
    private static boolean isTimestampColumn(int sqlType) {
        if (sqlType == 93  ){
            return true;
        }else {
            return false;
        }
    }
    private static boolean isTimeColumn(int sqlType) {
        if (sqlType == 92  ){
            return true;
        }else {
            return false;
        }
    }
}
