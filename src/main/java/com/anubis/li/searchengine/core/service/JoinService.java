package com.anubis.li.searchengine.core.service;

import com.anubis.li.searchengine.core.LuceneIndex;

public class JoinService {
    LuceneIndex luceneIndex;
    public JoinService(LuceneIndex luceneIndex){
        this.luceneIndex = luceneIndex;
    }

    public void join(String joinField, String parentField, String childField, String parentId, String childId) throws Exception{
        //JoinUtil.

    }
}
/**
 *
 * 内连接（‌INNER JOIN）‌：‌返回两个表中存在匹配关系的记录。‌这是最基本的连接类型，‌只返回两个表中字段匹配关系的记录。‌
 * 左外连接（‌LEFT JOIN）‌：‌返回左表中的所有记录和右表中匹配的记录。‌如果左表的记录在右表中没有匹配，‌则结果集中右表的部分会显示为NULL。‌
 * 右外连接（‌RIGHT JOIN）‌：‌与左外连接相反，‌返回右表中的所有记录和左表中匹配的记录。‌如果右表的记录在左表中没有匹配，‌则结果集中左表的部分会显示为NULL。‌
 * 全外连接（‌FULL OUTER JOIN）‌：‌返回左表和右表中的所有记录。‌如果某一边没有匹配的记录，‌结果集中对应部分会显示为NULL。‌需要注意的是，‌MySQL原生并不直接支持FULL OUTER JOIN，‌但可以通过UNION操作来模拟实现。‌
 * 除了上述四种主要连接类型，‌还有三种基于上述类型的变种：‌
 * 左排除连接（‌LEFT EXCLUDING JOIN）‌：‌返回左表中的记录，‌但不包括右表中匹配的记录。‌
 * 右排除连接（‌RIGHT EXCLUDING JOIN）‌：‌与左排除连接相反，‌返回右表中的记录，‌但不包括左表中匹配的记录。‌
 * 外部排除连接（‌OUTER EXCLUDING JOIN）‌：‌结合了左排除连接和右排除连接的特点，‌返回左右表中没有匹配的记录。‌
 * */
