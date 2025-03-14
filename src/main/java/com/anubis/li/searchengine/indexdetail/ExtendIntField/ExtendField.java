package com.anubis.li.searchengine.indexdetail.ExtendIntField;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;


public class ExtendField extends Field {
    public ExtendField(String fieldName, int value, FieldType type) {
        super(fieldName, type);
        this.fieldsData = Integer.valueOf(value);
    }

    @Override
    public BytesRef binaryValue() {
        byte[] bs = new byte[Integer.BYTES];
        NumericUtils.intToSortableBytes((Integer) this.fieldsData, bs, 0);
        return new BytesRef(bs);
    }
}
