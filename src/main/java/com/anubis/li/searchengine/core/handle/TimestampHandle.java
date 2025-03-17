package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import com.anubis.li.searchengine.core.util.DateTimeUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimestampHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return LongPoint.newExactQuery(field, parserDate(value));
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values)  {
        return LongPoint.newRangeQuery(field, parserDate(values.get(0))+1, Long.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, parserDate(values.get(0))-1);
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, parserDate(values.get(0)), Long.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return LongPoint.newRangeQuery(field, Long.MIN_VALUE, parserDate(values.get(0)));
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return LongPoint.newRangeQuery(field,parserDate(values.get(0)), parserDate(values.get(1)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        List<Long> longValues = new ArrayList<>();
        values.forEach(value -> longValues.add(parserDate(value)));
        return LongPoint.newSetQuery(field, longValues);
    }

    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {
        createFields(document, (Object)fieldValue, fieldConfig);
    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {

        Long timeStamp = 0L;
        if(fieldValue instanceof Timestamp){
            timeStamp = ((Timestamp) fieldValue).getTime();
        }else if(fieldValue instanceof LocalDateTime){
            timeStamp =   Date.from( ((LocalDateTime) fieldValue).atZone(ZoneId.systemDefault()).toInstant()).getTime();
        }else if(fieldValue instanceof LocalDate){
            timeStamp = Date.from(((LocalDate) fieldValue).atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
        }else if(fieldValue instanceof Long){
            timeStamp =  Long.parseLong(fieldValue.toString());
        }else if(fieldValue instanceof Date){
            timeStamp = ((Date) fieldValue).getTime();
        }else if(fieldValue instanceof Long){
            timeStamp = Long.parseLong(fieldValue.toString());
        }
        document.add(new LongPoint(fieldConfig.getFieldName(), timeStamp));
        if (fieldConfig.isStored()){
            document.add(new StoredField(fieldConfig.getFieldName(), DateTimeUtil.toLocalDateTimeString( fieldValue)));
        }
        if (fieldConfig.isSort()) {
            document.add(new NumericDocValuesField(fieldConfig.getFieldName(), timeStamp));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue.toString())));
        }

    }

    @Override
    public String getStringValue(Object fieldValue) {
        return fieldValue.toString();
    }

    private static long parserDate(String string)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(string).getTime();
        } catch (java.text.ParseException e) {

        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(string).getTime();
        } catch (java.text.ParseException e) {

        }
        return 0;
    }
    @Override
    public SortField getSortField(String field, boolean reverse){
        return new SortField(field, SortField.Type.LONG,reverse);
    }
}
