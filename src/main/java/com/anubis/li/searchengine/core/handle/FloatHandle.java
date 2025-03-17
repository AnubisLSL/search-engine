package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.util.List;
import java.util.stream.Collectors;

public class FloatHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return FloatPoint.newExactQuery(field,Float.parseFloat(value));
    }
    // 大于操作符
    @Override
    public Query gt(String field, List<String> values) {

        return FloatPoint.newRangeQuery(field,FloatPoint.nextUp(Float.parseFloat(values.get(0))) , Float.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return FloatPoint.newRangeQuery(field, Float.MIN_VALUE,FloatPoint.nextDown(Float.parseFloat(values.get(0))) );
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return FloatPoint.newRangeQuery(field, Float.parseFloat(values.get(0)), Float.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return FloatPoint.newRangeQuery(field, Float.MIN_VALUE, Float.parseFloat(values.get(0)));
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return FloatPoint.newRangeQuery(field, Float.parseFloat(values.get(0)), Float.parseFloat(values.get(1)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        return FloatPoint.newSetQuery(field, values.stream()
                .map(Float::parseFloat)
                .collect(Collectors.toList()));
    }
    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        float floatValue = Float.parseFloat(fieldValue);
        document.add(new FloatPoint(fieldConfig.getFieldName(), floatValue));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), floatValue));
        }
        if (fieldConfig.isSort()) {
            document.add(new FloatDocValuesField(fieldConfig.getFieldName(),  floatValue));
        }
        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(fieldValue)));
        }

    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {
        createFields(document,fieldValue.toString(),fieldConfig);
    }

    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.FLOAT,reverse);
    }
}
