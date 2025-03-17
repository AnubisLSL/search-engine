package com.anubis.li.searchengine.core;

import com.anubis.li.searchengine.core.model.IndexConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class LuceneService {

    private static ExecutorService executorService;

    private static ConcurrentHashMap<String, LuceneIndex> serviceMap = new ConcurrentHashMap<>();


    public static List<LuceneIndex> getAllIndex()
    {
        return new ArrayList<>(serviceMap.values());
    }

    public static LuceneIndex getIndex(String indexName)
    {
        return serviceMap.get(indexName);
    }
    public static void createIndex(List<IndexConfig> indexConfigs)
    {
        initExecutorService();
        indexConfigs.forEach(config ->{
            createIndex(config);
        });
    }
    public static void createIndex(IndexConfig indexConfig) {
        if (serviceMap.containsKey(indexConfig.getName())) {
            return;
        }
        LuceneIndex luceneIndex = new LuceneIndex(indexConfig);
        serviceMap.put(indexConfig.getName(), luceneIndex);
    }
    public static void shutdown(String indexName) {
        if (serviceMap.containsKey(indexName)){
            LuceneIndex luceneIndex = getIndex(indexName);
            luceneIndex.shutdown();
            serviceMap.remove(indexName);
        }
    }
    public static void deleteIndex(String indexName) {
        if (serviceMap.containsKey(indexName)){
            LuceneIndex luceneIndex = getIndex(indexName);
            try {
                luceneIndex.getDeleteService().deleteAll();
                luceneIndex.shutdown();
                serviceMap.remove(indexName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static  void shutdownAll()
    {
        //延迟3秒关闭，防止有数据没提交进来
        serviceMap.forEach((k,v)->{
            IndexWriter indexWriter = v.getIndexWriter();
            try {
                log.info("lucene index {} shutdown", v.getIndexConfig().getName());
                v.shutdown();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        serviceMap.clear();
    }
    private static void initExecutorService(){
        if (executorService!=null){
            return;
        }
        int numThread = Runtime.getRuntime().availableProcessors()*2;

        executorService =  Executors.newFixedThreadPool(numThread);
    }


    public static CompletableFuture<Void> submit(Runnable runnable) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        executorService.submit(() -> {
            try {
                runnable.run();
                future.complete(null);
            } catch (Exception ex) {
                future.completeExceptionally(ex);
            }
        });
        return future;
    }

    public static ExecutorService getExecutorService() {
        return executorService;
    }

}