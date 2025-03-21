package com.anubis.li.searchengine.core.service;

import com.anubis.li.searchengine.core.LuceneIndex;
import com.anubis.li.searchengine.core.common.event.EventData;
import com.anubis.li.searchengine.core.util.DocumentUtil;
import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.List;

public class InsertService {
    LuceneIndex luceneIndex;
    public InsertService(LuceneIndex luceneIndex){
        this.luceneIndex = luceneIndex;
    }

    /**
     * 添加 doc
     * @param doc
     * @return long
     * @throws IOException
     */

    public long addDocument(Document doc) throws IOException {
        long document =luceneIndex.getIndexWriter().addDocument(doc);
        luceneIndex.commit();
        return document;
    }
    public long addDocuments(List<Document> documents) throws IOException {
        long document = luceneIndex.getIndexWriter().addDocuments(documents);
        luceneIndex.commit();
        return document;
    }
    public void addEventData(List<EventData> list) {
        list.forEach(taskData -> {
            try {
                luceneIndex.getIndexWriter().addDocument(
                        DocumentUtil.createDocument(taskData.getAfter(),luceneIndex.getIndexConfig().getFieldsConfig())
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        luceneIndex.commit();
    }

}
