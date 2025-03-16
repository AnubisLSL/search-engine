package com.anubis.li.searchengine.core.util;


import com.anubis.li.searchengine.core.model.IndexConfig;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.NoLockFactory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class CreateUtil {


    /**
     * 根据提供的分析器名称创建并返回相应的Lucene分析器实例。
     *
     * @param analyzerName 分析器的名称，支持 "smartcn"、"whitespace" 或默认的 "simple"。
     * @return 根据指定名称创建的Lucene分析器实例。
     *
     * 此方法提供了一个工厂模式的实现，用于根据配置或参数动态创建不同类型的分析器。
     * 支持的分析器包括：
     * - "smartcn": 使用SmartChineseAnalyzer，适合中文分词，能识别中文词汇和短语。
     * - "whitespace": 使用WhitespaceAnalyzer，仅基于空白字符进行分词，不执行其他文本处理。
     * - 默认: 使用SimpleAnalyzer，执行基本的文本分析，包括大小写转换和停用词过滤。
     */
    public static Analyzer createAnalyzer(String analyzerName)  {
        Analyzer analyzer;
        switch (analyzerName) {
            case "ik":
                analyzer = new IKAnalyzer(true);
                break;
            case "whitespace":
                analyzer = new WhitespaceAnalyzer();
                break;
            case "standard":
                analyzer = new StandardAnalyzer();
                break;
            default:
                analyzer = new SmartChineseAnalyzer();
                break;
        }
        return analyzer;
    }
    /**
     * 创建写入索引
     * @param indexConfig
     * @return
     * @throws IOException
     */
    public static IndexWriter createIndexWriter(IndexConfig indexConfig) throws IOException {
        FSDirectory fsDirectory =  createDirectory(indexConfig);
        Analyzer analyzer = createAnalyzer(indexConfig.getAnalyzer());
        IndexWriterConfig indexWriterConfig = createIndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(fsDirectory, indexWriterConfig);
        return indexWriter;
    }
    public static FSDirectory createDirectory(IndexConfig indexConfig) throws IOException {
        String path = indexConfig.getPath()+ File.separator+indexConfig.getName();
        if("mmap".equals(indexConfig.getFsdType()) ) {
            return  MMapDirectory.open(new File(path).toPath(), NoLockFactory.INSTANCE);
        }else{
            return NIOFSDirectory.open(new File(path).toPath(), NoLockFactory.INSTANCE);
        }
    }
    public static IndexWriterConfig createIndexWriterConfig(Analyzer analyzer) {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        indexWriterConfig.setMaxBufferedDocs(20000);
        return indexWriterConfig;
    }

    /**
     * 创建多线程读取索引
     *
     * @param indexWriter
     * @return
     * @throws IOException
     */
    public static IndexSearcher createIndexSearcher(IndexWriter indexWriter) throws IOException {
        DirectoryReader directoryReader = DirectoryReader.open(indexWriter);
        IndexSearcher indexSearcher = new IndexSearcher(directoryReader, LuceneService.getExecutorService());
        return indexSearcher;
    }
    /**
     * 创建多线程读取索引
     *
     * @param indexWriter
     * @return
     * @throws IOException
     */
    public static DirectoryReader createDirectoryReader(IndexWriter indexWriter) throws IOException {
        return DirectoryReader.open(indexWriter);
    }
}
