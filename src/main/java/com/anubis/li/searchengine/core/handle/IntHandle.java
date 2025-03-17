package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.util.List;
import java.util.stream.Collectors;

public class IntHandle extends AbstractHandle {

    // 等于操作符
    @Override
    public Query eq(String field, String value){
        return IntPoint.newExactQuery(field,Integer.parseInt(value));
    }

    // 大于操作符
    @Override
    public Query gt(String field, List<String> values) {
        return IntPoint.newRangeQuery(field, Integer.parseInt(values.get(0))+1, Integer.MAX_VALUE);
    }

    // 小于操作符
    @Override
    public Query lt(String field, List<String> values) {
        return IntPoint.newRangeQuery(field, Integer.MIN_VALUE, Integer.parseInt(values.get(0))-1);
    }

    // 大于等于操作符
    @Override
    public Query ge(String field, List<String> values) {
        return IntPoint.newRangeQuery(field, Integer.parseInt(values.get(0)), Integer.MAX_VALUE);
    }

    // 小于等于操作符
    @Override
    public Query le(String field, List<String> values) {
        return IntPoint.newRangeQuery(field, Integer.MIN_VALUE, Integer.parseInt(values.get(0)));
    }

    // 在区间内操作符
    @Override
    public Query between(String field, List<String> values) {
        return IntPoint.newRangeQuery(field, Integer.parseInt(values.get(0)), Integer.parseInt(values.get(1)));
    }

    // 在集合中操作符
    @Override
    public Query in(String field, List<String> values) {
        return IntPoint.newSetQuery(field, values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList()));
    }


    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        int intValue = Integer.parseInt(fieldValue);
        document.add(new IntPoint(fieldConfig.getFieldName(), intValue));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), intValue));
        }
        if (fieldConfig.isSort()) {
            document.add(new NumericDocValuesField(fieldConfig.getFieldName(), intValue));
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
        return  new SortField(field, SortField.Type.INT,reverse);
    }
}
