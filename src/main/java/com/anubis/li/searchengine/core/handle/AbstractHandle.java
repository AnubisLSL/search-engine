package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.query.QueryNode;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DocValuesFieldExistsQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;



public abstract class AbstractHandle implements TypeHandler{
    
    public Query createQuery(QueryNode queryNode) {
        return switch (queryNode.getOperator()) {
            case EQ -> eq(queryNode.getField(), queryNode.getValues().get(0));
            case GE -> ge(queryNode.getField(), queryNode.getValues());
            case GT -> gt(queryNode.getField(), queryNode.getValues());
            case IN -> in(queryNode.getField(), queryNode.getValues());
            case ISNOTNULL -> isNotNull(queryNode.getField());
            case ISNULL -> isNull(queryNode.getField());
            case LE -> le(queryNode.getField(), queryNode.getValues());
            case LIKE -> like(queryNode.getField(), queryNode.getValues());
            case LT -> lt(queryNode.getField(), queryNode.getValues());
            case NE -> ne(queryNode.getField(), queryNode.getValues());
            case NOTIN -> notIn(queryNode.getField(), queryNode.getValues());
            case BETWEEN -> between(queryNode.getField(), queryNode.getValues());
            case Fuzzy -> fuzzy(queryNode.getField(), queryNode.getValues());
            default -> null;
        };
    }
    // 模糊匹配操作符
    @Override
    public Query like(String field, List<String> values) {
        return null;
    }

    /**
     * 以下操作不需要一一实现构建Query方法都一直，但是上一个层面需要使用MUST_NOT连接
     * */
    //不等于操作符,

    public Query ne(String field, List<String> values) {
        return eq(field, values.get(0));
    }

    // 判断是否为空操作符

    public Query isNull(String field) {
        return new DocValuesFieldExistsQuery(field);
    }

    // 判断是否为空操作符

    public Query isNotNull(String field) {
        return new DocValuesFieldExistsQuery(field);
    }

    // 不在操作符
    public Query notIn(String field, List<String> values) {
        return in(field,values);
    }

    public Query fuzzy(String field,List<String> values) {
        int fuzzyLength = 0;
        if(values.get(1) != null){
            fuzzyLength = Integer.parseInt(values.get(1));
        }
        return new FuzzyQuery(new Term(field,values.get(0)), fuzzyLength);
    }
    @Override
    public Query phrase(String field,List<String> values, Analyzer analyzer) {
        int slop = 0;
        if(values.get(1) != null){
            slop = Integer.parseInt(values.get(1));
        }
        try {
            QueryParser queryParser = new QueryParser(field, analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            return   queryParser.parse("\""+values.get(0)+"\"~"+slop);
        }catch (Exception e){
            return null;
        }
    }

    public String getStringValue(Object fieldValue) {
        return fieldValue.toString();
    }

    public Object getResultSetValue(ResultSet rs,String filedName) throws SQLException {
        try {
            return rs.getObject(filedName);
        }catch (Exception e){
            return null;
        }
    }

    public Query parser(String field, List<String> values, Analyzer analyzer) {
        try {
            QueryParser queryParser = new QueryParser(field, analyzer);
            queryParser.setDefaultOperator(QueryParser.Operator.AND);
            return queryParser.parse(values.get(0));
        }catch (Exception e){
            return null;
        }
    }

    public String getQueryString(String type, String field, String... values) {
        if("eq".equals(type)){
            return "[" + values[0] + " TO "+values[0]+"]";
        }else if("gt".equals(type)){
            return field + ":{" + values[0] + " TO *}";
        }else if("lt".equals(type)){
            return field + ":{* TO " + values[0] + "}";
        }else if("ge".equals(type)){
            return field + ":[" + values[0] + " TO *]";
        }else if("le".equals(type)){
            return field + ":[* TO " + values[0] + "]";
        }else if("between".equals(type)){
            return field + ":[" + values[0] + " TO " + values[1] + "]";
        }else if("like".equals(type)){
            return field + ":*" + values[0] + "*";
        }
        return null;
    }
        
}
