package com.anubis.li.searchengine.core;

import com.anubis.li.searchengine.core.model.FieldConfig;
import com.anubis.li.searchengine.core.model.IndexConfig;
import com.anubis.li.searchengine.core.service.*;
import com.anubis.li.searchengine.core.util.CreateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.Analyzer;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Data
@Slf4j
public  class LuceneIndex {

    private volatile boolean lock = false;

    private FSDirectory fsDirectory;
    private IndexConfig indexConfig;
    private FieldConfig keyField= null;
    private IndexWriter indexWriter;
    private Analyzer analyzer;
    private IndexSearcher indexSearcher;
    private DeleteService deleteService;
    private GroupService groupService;
    private InsertService insertService;
    private SearchService searchService;
    private UpdateService updateService;
    private JoinService joinService;

    private IndexWriterConfig indexWriterConfig;

    public LuceneIndex(IndexConfig indexConfig)
    {
        this.indexConfig = indexConfig;
        try{

            fsDirectory = CreateUtil.createDirectory(indexConfig);
            analyzer = CreateUtil.createAnalyzer(indexConfig.getAnalyzer());
            indexWriterConfig = CreateUtil.createIndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
            indexSearcher = CreateUtil.createIndexSearcher(indexWriter);
            deleteService = new DeleteService(this);
            insertService = new InsertService(this);
            searchService = new SearchService(this);
            updateService = new UpdateService(this);
            groupService = new GroupService(this);
            joinService = new JoinService(this);
        }catch (Exception exception){
            log.error("创建索引失败{}", indexConfig.getName(), exception);
        }
    }

    public void forceMerge(){
        try {
            indexWriter.deleteUnusedFiles();
            indexWriter.forceMergeDeletes();
            this.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int numDocs() {
        return this.indexSearcher.getIndexReader().numDocs();
    }
    public int getDocNums(){
        return indexSearcher.getIndexReader().numDocs();
    }
    public void commit() {
        synchronized (this.indexSearcher) {
            try {
                IndexSearcher indexSearcher = CreateUtil.createIndexSearcher(this.indexWriter);
                this.indexSearcher = indexSearcher;
            } catch (IOException e) {
                log.error("Near Real-Time updateIndexSource error", e);
            } finally {
                CompletableFuture
                        .runAsync(() -> {
                            try {
                                indexWriter.flush();
                                indexWriter.commit();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }, LuceneService.getExecutorService())
                        .whenComplete((result, throwable) -> {
                            setLock(false);
                            if (throwable != null) {
                                log.error("{} - source commit error", indexConfig.getName(), throwable);
                            }
                        });
            }
        }
    }
    public FieldConfig getKeyFieldConfig()
    {
        if (this.keyField == null){
            for (FieldConfig fieldConfig : indexConfig.getFieldsConfig()) {
                if (fieldConfig.getFieldName().equals(indexConfig.getKey())){
                    this.keyField = fieldConfig;
                    return fieldConfig;
                }
            }
        }
        return this.keyField;
    }
    public  void shutdown()
    {
        try {

            indexWriter.flush();
            indexWriter.commit();
            analyzer.close();
            indexWriter.close();
            fsDirectory.close();
        } catch (IOException e) {
            log.error(LuceneIndex.class.getName(),e);
        }
    }

}
