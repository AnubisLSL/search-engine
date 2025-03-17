package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BooleanHandle extends AbstractHandle {

    @Override
    public Query eq(String field, String value) {
        int intValue = 1;
        if("false".equals(value) || "0".equals(value)){
            intValue = 0;
        }
        return IntPoint.newExactQuery(field,intValue);
    }

    @Override
    public Query gt(String field, List<String> values) {
        return null;
    }

    @Override
    public Query lt(String field, List<String> values) {
        return null;
    }

    @Override
    public Query ge(String field, List<String> values) {
        return null;
    }

    @Override
    public Query le(String field, List<String> values) {
        return null;
    }

    @Override
    public Query between(String field, List<String> values) {
        return null;
    }

    @Override
    public Query like(String field, List<String> values) {
        return null;
    }

    @Override
    public Query in(String field, List<String> values) {
        return IntPoint.newSetQuery(field, values.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList()));
    }


    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {

        int intValue = 1;
        if("false".equals(fieldValue) || "0".equals(fieldValue)){
            intValue = 0;
        }
        document.add(new IntPoint(fieldConfig.getFieldName(), intValue));
        if (fieldConfig.isStored()) {
            document.add(new StoredField(fieldConfig.getFieldName(), intValue==0?"false":"true"));
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
        createFields(document,String.valueOf(fieldValue), fieldConfig);
    }
    @Override
    public Object getResultSetValue(ResultSet rs, String filedName) throws SQLException {
        return rs.getBoolean(filedName);
    }
    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.INT,reverse);
    }
}
