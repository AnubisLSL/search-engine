package com.anubis.li.searchengine.service;

import com.anubis.li.searchengine.core.model.FieldConfig;
import com.anubis.li.searchengine.util.QueryUtil;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.MatchAllDocsQuery;

import java.io.IOException;

public class DeleteService {
    LuceneIndex luceneIndex;
    public DeleteService(LuceneIndex luceneIndex){
        this.luceneIndex = luceneIndex;
    }
    /**
     * 清空索引
     * @return
     * @throws IOException
     */
    public long deleteAll() throws IOException {
        long deleteAll = luceneIndex.getIndexWriter().deleteAll();
        luceneIndex.commit();
        return deleteAll;
    }

    /**
     * 根据 query 删除
     * @param queries
     * @return
     * @throws IOException
     */
    public long deleteDocuments(Query... queries) throws IOException {
        long l = 0L;
        if (queries==null || queries.length==0){
            l = luceneIndex.getIndexWriter().deleteDocuments(new MatchAllDocsQuery());
        }else {
            l = luceneIndex.getIndexWriter().deleteDocuments(queries);
        }
        luceneIndex.commit();
        return l;
    }

    /**
     * 根据 term 删除
     * @param terms
     * @return
     * @throws IOException
     */
    public long deleteDocuments(Term... terms) throws IOException {
        long l = luceneIndex.getIndexWriter().deleteDocuments(terms);
        luceneIndex.commit();
        return l;
    }
    /**
     * 删除未使用的索引
     * @throws IOException
     */
    public void deleteUnusedFiles() throws IOException {
        luceneIndex.getIndexWriter().deleteUnusedFiles();
        luceneIndex.commit();
    }
    public void addEventData(List<EventData> list) throws IOException {
        FieldConfig keyField = luceneIndex.getKeyFieldConfig();
        for(EventData eventData : list){
            luceneIndex.getIndexWriter().deleteDocuments(QueryUtil.createKeyFiledQuery(keyField, eventData.getAfter()));
        }
        luceneIndex.commit();
    }


}
