package com.anubis.li.searchengine.core.common.enums;

/**
 * 操作符枚举
 */ 
public enum OperatorEnum {
    // 等于操作符
    EQ,
    // 不等于操作符
    NE,
    // 大于操作符
    GT,
    // 小于操作符
    LT,
    // 大于等于操作符
    GE,
    // 小于等于操作符
    LE,
    // 在区间内操作符
    BETWEEN,
    // 在集合中操作符
    IN,
    // 模糊匹配操作符
    LIKE,
    // 判断是否为空操作符
    ISNULL,
    // 判断是否不为空操作符
    ISNOTNULL,
    // 不在集合中操作符
    NOTIN,
    // 分段查询操作符
    SECTION,
    Fuzzy,
    Filter,
    Parser,
    Phrase
}
