package com.anubis.li.searchengine.core.handle;


import com.anubis.li.searchengine.core.model.FieldConfig;
import com.anubis.li.searchengine.core.query.QueryNode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface TypeHandler {

    Query createQuery(QueryNode node);
    // 等于操作符
    Query eq(String field, String value);
    //不等于操作符,上一个层面需要使用MUST_NOT连接
    Query ne(String field, List<String> values);
    // 大于操作符
    Query gt(String field, List<String> values);
    // 小于操作符
    Query lt(String field, List<String> values);
    // 大于等于操作符
    Query ge(String field, List<String> values);
    // 小于等于操作符
    Query le(String field, List<String> values);
    // 在区间内操作符
    Query between(String field, List<String> values);
    // 模糊匹配操作符
    Query like(String field, List<String> values);
    // 在集合中操作符
    Query in(String field, List<String> values);
    // 判断是否为空操作符
    Query isNull(String field);
    // 判断是否为空操作符
    Query isNotNull(String field);
    // 判断是否为空操作符
    Query notIn(String field, List<String> values);

    Query fuzzy(String field,List<String> values);


    Query parser(String field, List<String> values, Analyzer analyzer);

    Query phrase(String field, List<String> values, Analyzer analyzer);

    void createFields(Document document, String fieldValue, FieldConfig fieldConfig);

    void createFields(Document document, Object fieldValue, FieldConfig fieldConfig);

    Object getResultSetValue(ResultSet rs,String filedName) throws SQLException;

    String getStringValue(Object fieldValue);

    SortField getSortField(String field, boolean reverse);

    String getQueryString(String type,String field,String... values);
}
