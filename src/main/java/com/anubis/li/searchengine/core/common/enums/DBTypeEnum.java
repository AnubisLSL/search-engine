package com.anubis.li.searchengine.core.common.enums;

/**
 * 数据库类型枚举
 */ 
public enum DBTypeEnum
{
    MYSQL("mysql"),
    ORACLE("oracle"),
    SQLSERVER("sqlserver"),
    POSTGRESQL("postgresql"),
    DB2("db2"),
    SQLITE("sqlite"),
    H2("h2"),
    MARIADB("mariadb"),
    MSSQL("mssql"),
    MONGODB("mongodb"),
    MONGO("mongo"),
    MONGO_DB("mongo_db"),
    MONGO_COLLECTION("mongo_collection"),
    ;

    DBTypeEnum(String type) {

    }
}
