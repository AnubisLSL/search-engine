package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return LongPoint.newExactQuery(field, Time.valueOf(value).getTime());
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Time.valueOf(values.get(0)).getTime()+1, Long.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, Time.valueOf(values.get(0)).getTime()-1);
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Time.valueOf(values.get(0)).getTime(), Long.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, Time.valueOf(values.get(0)).getTime());
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return LongPoint.newRangeQuery(field,Time.valueOf(values.get(0)).getTime(), Time.valueOf(values.get(1)).getTime());
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        List<Long> longValues = new ArrayList<>();
        values.forEach(value -> longValues.add(Time.valueOf(value).getTime()));
        return LongPoint.newSetQuery(field, longValues);
    }
    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        Long time = (new Time(Long.parseLong(fieldValue))).getTime();
        document.add(new LongPoint(fieldConfig.getFieldName(), time));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), fieldValue.toString()));
        }
        if (fieldConfig.isSort()) {
            document.add(new NumericDocValuesField(fieldConfig.getFieldName(), time));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue.toString())));
        }
    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {

        Long time = 0L;
        if(fieldValue instanceof Time){
            time = ((Time) fieldValue).getTime();
        }else if(fieldValue instanceof LocalTime){
            time =  Time.valueOf((LocalTime) fieldValue).getTime();
        }else if(fieldValue instanceof Long){
            time =  Long.parseLong(fieldValue.toString());
        }
        document.add(new LongPoint(fieldConfig.getFieldName(), time));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), fieldValue.toString()));
        }
        if (fieldConfig.isSort()) {
            document.add(new NumericDocValuesField(fieldConfig.getFieldName(), time));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue.toString())));
        }
    }

    @Override
    public SortField getSortField(String field, boolean reverse){
        return   new SortField(field, SortField.Type.LONG,reverse);
    }
}
