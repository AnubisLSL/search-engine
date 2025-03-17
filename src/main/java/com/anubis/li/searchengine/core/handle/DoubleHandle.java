package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.util.List;
import java.util.stream.Collectors;

public class DoubleHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return DoublePoint.newExactQuery(field,Double.parseDouble(value));
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values) {
        return DoublePoint.newRangeQuery(field,DoublePoint.nextUp(Double.parseDouble(values.get(0))), Double.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return DoublePoint.newRangeQuery(field, Double.MIN_VALUE, DoublePoint.nextDown(Double.parseDouble(values.get(0))));
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return DoublePoint.newRangeQuery(field, Double.parseDouble(values.get(0)), Double.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return DoublePoint.newRangeQuery(field, Double.MIN_VALUE, Double.parseDouble(values.get(0)));
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return DoublePoint.newRangeQuery(field, Double.parseDouble(values.get(0)), Double.parseDouble(values.get(1)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        return DoublePoint.newSetQuery(field, values.stream()
                .map(Double::parseDouble)
                .collect(Collectors.toList()));
    }

    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        if(fieldValue == null || fieldValue.isEmpty()){ return ;}
        double doubleValue = Double.parseDouble(fieldValue);
        document.add(new DoublePoint(fieldConfig.getFieldName(), doubleValue));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), doubleValue));
        }
        if (fieldConfig.isSort()) {
            document.add(new DoubleDocValuesField(fieldConfig.getFieldName(),doubleValue));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(String.valueOf(doubleValue))));
        }

    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {
        createFields(document,fieldValue.toString(),fieldConfig);

    }

    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.DOUBLE,reverse);
    }
}
