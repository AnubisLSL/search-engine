package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.util.BytesRef;

import java.util.List;
import java.util.StringTokenizer;

public class StringHandle extends AbstractHandle {
    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return new TermQuery(new Term(field, value));
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values) {
        return TermRangeQuery.newStringRange(field, values.get(0), null, false, false);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return TermRangeQuery.newStringRange(field, null, values.get(0), false, false);
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return TermRangeQuery.newStringRange(field, values.get(0), null, true, false);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return TermRangeQuery.newStringRange(field, null, values.get(0), false, true);
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return TermRangeQuery.newStringRange(field, values.get(0), values.get(1), true, true);
    }

    // 模糊匹配操作符
    @Override
    public Query like(String field, List<String> values) {
        return new WildcardQuery(new Term(field, values.get(0)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for(String key:values){
            builder.add(new TermQuery(new Term(field,key)), BooleanClause.Occur.SHOULD);
        }
        return builder.build();
    }
    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {
        if (fieldConfig.isSets()) {
            StringTokenizer tokenizer = new StringTokenizer(fieldValue, fieldConfig.getSetsSplit());
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token.length() > 0){
                    document.add(new StringField(fieldConfig.getFieldName(), token, Field.Store.YES));
                }
            }
        }
        if (fieldConfig.isAnalyzerd()){
            if (fieldConfig.isStored()){
                document.add(new TextField(fieldConfig.getFieldName(), fieldValue, Field.Store.YES));
            }else{
                document.add(new TextField(fieldConfig.getFieldName(), fieldValue, Field.Store.NO));
            }
        }else{
            if (fieldConfig.isStored()){
                document.add(new StringField(fieldConfig.getFieldName(), fieldValue, Field.Store.YES));
            }
        }
        if (fieldConfig.isSort()) {
            document.add(new SortedDocValuesField(fieldConfig.getFieldName(), new BytesRef(fieldValue)));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue)));
        }
    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {
        createFields(document, fieldValue.toString(), fieldConfig);
    }

    @Override
    public SortField getSortField(String field, boolean reverse){
        return   new SortField(field, SortField.Type.STRING,reverse);
    }
}
