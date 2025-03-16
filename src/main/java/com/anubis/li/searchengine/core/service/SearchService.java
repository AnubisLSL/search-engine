package com.anubis.li.searchengine.core.service;

import com.anubis.li.searchengine.core.common.SearchPage;
import com.anubis.li.searchengine.core.util.DocumentUtil;
import org.apache.lucene.document.StringField;
import org.apache.lucene.search.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SearchService {
    LuceneIndex luceneIndex;
    public SearchService(LuceneIndex luceneIndex){
        this.luceneIndex = luceneIndex;
    }


    /**
     * 查询单个
     * @param query
     * @return
     * @throws IOException
     */
    public Document searchOneDocument(Query query,String selectFields) throws IOException {
        ScoreDoc[] scoreDocs = search(query, null,1).scoreDocs;
        if(scoreDocs.length == 0) {return null;}
        Document document = getDocument(scoreDocs[0].doc,getFieldsSet(selectFields));

        if (document == null){
            return null;
        }
        document.add(new StringField("_docId",String.valueOf(scoreDocs[0].doc),StringField.Store.YES));
        return  document;
    }
    public Map<String,Object> searchOneMap(Query query, String selectFields) throws IOException {
        return  DocumentUtil.getModel(searchOneDocument(query, selectFields), luceneIndex.getIndexConfig().getFieldsConfig());
    }

    /**
     * 根据 Query 查询集合
     * @param query
     * @param num
     * @return List<Document>
     * @throws IOException
     */
    public List<Document> searchListDoc(Query query, int num,Sort sort,String selectFields) throws IOException {
        ScoreDoc[] scoreDocs = search(query, sort,num).scoreDocs;
        return getDocuments(scoreDocs,getFieldsSet(selectFields));
    }


    public List<Map<String,Object>> searchList(Query query, int num, Sort sort, String selectFields) throws IOException {
        List<Document> documents = searchListDoc(query, num,sort,selectFields);
        return DocumentUtil.getModels(documents,  luceneIndex.getIndexConfig().getFieldsConfig());
    }

    /**
     * @param query
     * @param page  = Page.newPage(1, 10)
     * @return
     * @throws IOException
     * @Title: searchList
     * @Description: 简单分页查询
     * @return: Page<Document>
     */
    public SearchPage<Document> searchListDoc(Query query, SearchPage<Document> page,Sort sort,String selectFields) throws IOException {

        TopDocsCollector collector;
        if(sort ==null || sort.getSort().length==0){
            collector = TopScoreDocCollector.create(page.getPageNum()+page.getPageSize(),luceneIndex.numDocs());
        }else{
            collector = TopFieldCollector.create(sort,page.getPageNum()+page.getPageSize(),luceneIndex.numDocs());
        }
        luceneIndex.getIndexSearcher().search(query, collector);
        int totalHits = collector.getTotalHits();
        ScoreDoc[] scoreDocs = collector.topDocs(page.getPageNum(), page.getPageSize()).scoreDocs;
        page.setList(getDocuments(scoreDocs,getFieldsSet(selectFields)));
        page.setTotalRow(totalHits);
        return page;
    }

    /**
     * @param query
     * @param page  = Page.newPage(1, 10)
     * @return
     * @throws IOException
     * @Title: searchList
     * @Description: 简单分页查询
     * @return: Page<Document>
     */
    public SearchPage searchList(Query query, SearchPage page, Sort sort, String returnField) throws IOException {

        TopDocsCollector collector ;
        if(sort ==null || sort.getSort().length==0){
            collector = TopScoreDocCollector.create(page.getPageNum()+page.getPageSize(),luceneIndex.numDocs());
        }else{
            collector = TopFieldCollector.create(sort,page.getPageNum()+page.getPageSize(),luceneIndex.numDocs());
        }
        luceneIndex.getIndexSearcher().search(query, collector);
        page.setTotalRow(collector.getTotalHits());
        ScoreDoc[] scoreDocs = collector.topDocs(page.getPageNum(), page.getPageSize()).scoreDocs;
        //scoreDocs[0].score = 0;
        page.setList(DocumentUtil.getModels(getDocuments(scoreDocs,getFieldsSet(returnField)), luceneIndex.getIndexConfig().getFieldsConfig()));
        return page;
    }

    /**
     * @return
     * @throws IOException
     * @Title: searchTotal
     * @Description: 查询所有文档 必须使用 MyDocument 添加
     * @return: List<Document>
     */
    public List<Document> searchTotalDoc() throws IOException {
        return searchListDoc(new MatchAllDocsQuery(), Integer.MAX_VALUE,null,"*");
    }
    /**
     * @return
     * @throws IOException
     * @Title: searchTotal
     * @Description: 查询所有文档 必须使用 MyDocument 添加
     * @return: List<T>
     */
    public List<Map<String,Object>> searchTotal() throws IOException {
        return DocumentUtil.getModels(searchTotalDoc(), luceneIndex.getIndexConfig().getFieldsConfig());
    }

    /**
     * 统计数量
     * @param query
     * @return
     * @throws IOException
     */
    public int count(Query query) throws IOException {
        return luceneIndex.getIndexSearcher().count(query);
    }

    /**
     * 获取列表
     * @param scoreDocs
     * @return
     * @throws IOException
     */
    public List<Document> getDocuments(ScoreDoc[] scoreDocs,Set<String> fields) throws IOException {
        List<Document> documents = new ArrayList(scoreDocs.length);
        for (int i = 0, size = scoreDocs.length; i < size; i++) {
            Document document = getDocument(scoreDocs[i].doc,fields);
            documents.add(document);
        }
        return documents;
    }

    /**
     * 根据docId获取
     * @param docID
     * @return
     * @throws IOException
     */
    public Document  getDocument(int docID, Set<String> fields) throws IOException {
        if (fields == null || fields.isEmpty() || fields.size() == 0){
            return luceneIndex.getIndexSearcher().doc(docID);
        }else{
            return luceneIndex.getIndexSearcher().doc(docID,fields);
        }
    }

    public TopDocs search(Query query, Sort sort, int n) throws IOException {
        if (sort == null || sort.getSort().length==0){
            return luceneIndex.getIndexSearcher().search(query,n);
        }else {
            return luceneIndex.getIndexSearcher().search(query,n,sort);
        }
    }
    public Set<String> getFieldsSet(String fields) throws IOException {
        if(fields == null || "*".equals(fields) || fields.isEmpty() ){
            return null;
        }else{
            return Arrays.stream(fields.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
        }
    }
}
