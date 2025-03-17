package com.anubis.li.searchengine.core.handle;

import com.anubis.li.searchengine.core.model.FieldConfig;
import org.apache.lucene.document.BinaryPoint;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.BytesRef;

import java.util.List;

public class BytesHandle extends AbstractHandle {

    @Override
    public Query eq(String field, String value) {
        return null;
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
        return null;
    }

    @Override
    public void createFields(Document document, String fieldValue, FieldConfig fieldConfig) {
        byte[] bytes = fieldValue.getBytes();
        if (bytes.length>16){
            return;
        }
        document.add(new BinaryPoint(fieldConfig.getFieldName(), bytes));
        if (fieldConfig.isStored()){
            document.add(new StoredField(fieldConfig.getFieldName(), bytes));
        }
        if (fieldConfig.isSort()) {
            document.add(new SortedDocValuesField(fieldConfig.getFieldName(), new BytesRef(bytes)));
        }
    }

    @Override
    public void createFields(Document document, Object fieldValue, FieldConfig fieldConfig) {

        byte[] bytes =  (byte[])fieldValue;
        if (bytes.length>16 || bytes.length==0){
            return;
        }
        document.add(new BinaryPoint(fieldConfig.getFieldName(), bytes));
        if (fieldConfig.isStored()){
            document.add(new StoredField(fieldConfig.getFieldName(), bytes));
        }
        if (fieldConfig.isSort()) {
            document.add(new SortedDocValuesField(fieldConfig.getFieldName(), new BytesRef(bytes)));
        }

        if (fieldConfig.isGrouping()) {
            document.add(new SortedDocValuesField("_group_"+fieldConfig.getFieldName(), new BytesRef(bytes)));
        }
    }

    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.CUSTOM,reverse);
    }
}
