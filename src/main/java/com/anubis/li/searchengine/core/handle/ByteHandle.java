package com.anubis.li.searchengine.core.handle;

import org.apache.lucene.search.SortField;

public class ByteHandle extends IntHandle {
    @Override
    public SortField getSortField(String field, boolean reverse){
        return  new SortField(field, SortField.Type.CUSTOM,reverse);
    }
}
