package com.anubis.li.searchengine.core.service;

import com.anubis.li.searchengine.core.util.DocumentUtil;
import com.anubis.li.searchengine.core.util.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.Term;

import java.io.IOException;

@Slf4j
public class UpdateService {
    LuceneIndex luceneIndex;

    public UpdateService(LuceneIndex luceneIndex) {
        this.luceneIndex = luceneIndex;
    }

    /**
     * 根据 term 更新
     *
     * @param documents
     * @param term
     * @return
     * @throws IOException
     */
    public long updateDocuments(List<Document> documents, Term term) throws IOException {
        long l = luceneIndex.getIndexWriter().updateDocuments(term, documents);
        luceneIndex.commit();
        return l;
    }

    public long updateDocument(Document document, Term term) throws IOException {

        long l = luceneIndex.getIndexWriter().updateDocument(term, document);
        luceneIndex.commit();
        return l;
    }
    public void addEventData(List<EventData> list)  {
        FieldConfig keyField = luceneIndex.getKeyFieldConfig();
        for (EventData eventData : list) {
            try{
                luceneIndex.getIndexWriter().deleteDocuments(  QueryUtil.createKeyFiledQuery(keyField, eventData.getBefore()));
                luceneIndex.getIndexWriter().addDocument(DocumentUtil.createDocument(eventData.getAfter(), luceneIndex.getIndexConfig().getFieldsConfig()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        luceneIndex.commit();
    }

    public void updateDocuments(List<Document> documents) throws IOException {
        FieldConfig keyField = luceneIndex.getKeyFieldConfig();
        for (Document document : documents) {
            Query query = QueryUtil.createKeyFiledQuery(keyField, document);
            if (query == null) {
                continue;
            }
            luceneIndex.getIndexWriter().deleteDocuments(query);
            luceneIndex.getIndexWriter().addDocument(document);
        }
        luceneIndex.commit();
    }

    public long updateDocumentFieldsValue(Document updateDocument, Query query) throws IOException {

        List<Document> documents = luceneIndex.getSearchService().searchListDoc(query, Integer.MAX_VALUE, null, "*");

        for (Document document : documents) {
            //替换字段值
            updateDocument.getFields().forEach(field -> {
                document.removeFields(field.name());
                document.add(field);
            });
        }
        long l = luceneIndex.getInsertService().addDocuments(documents);
        documents.clear();
        return l;
    }
}
