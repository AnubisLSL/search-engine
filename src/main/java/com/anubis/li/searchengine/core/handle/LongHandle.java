package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.util.List;
import java.util.stream.Collectors;

public class LongHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return LongPoint.newExactQuery(field,Long.parseLong(value));
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values)  {
        return LongPoint.newRangeQuery(field, Long.parseLong(values.get(0))+1, Long.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, Long.parseLong(values.get(0))-1);
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.parseLong(values.get(0)), Long.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, Long.parseLong(values.get(0)));
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.parseLong(values.get(0)), Long.parseLong(values.get(1)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        return LongPoint.newSetQuery(field, values.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList()));
    }

    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        long longValue = Long.parseLong(fieldValue);
        document.add(new LongPoint(fieldConfig.getFieldName(), longValue));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), longValue));
        }
        if (fieldConfig.isSort()) {
            document.add(new NumericDocValuesField(fieldConfig.getFieldName(), longValue));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue.toString())));
        }
    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {
        createFields(document, fieldValue.toString(), fieldConfig);
    }
    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.LONG,reverse);
    }

}
