package com.anubis.li.searchengine.core.common.enums;

/**
 * 消息枚举
 */ 
public enum MessageEnum {
    SUCCESS("000000", "成功"),
    FAIL("999999", "失败"),
    ERROR("999998", "系统异常"),
    PARAM_ERROR("999997", "参数错误"),
    PARAM_NULL("999996", "参数为空"),
    PARAM_FORMAT_ERROR("999995", "参数格式错误");

    MessageEnum(String number, String 成功) {
    }
}
