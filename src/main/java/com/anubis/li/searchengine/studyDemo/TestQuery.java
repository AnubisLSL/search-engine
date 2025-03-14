package com.anubis.li.searchengine.studyDemo;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;

public class TestQuery {
    private final static String IDX_DIR = "D:\\lucene";

    public static void main(String[] args) {
        try {
            //创建索引库对象
            Directory directory = FSDirectory.open(Paths.get(IDX_DIR));
            //索引读取工具
            IndexReader indexReader = DirectoryReader.open(directory);
            //索引搜索工具
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            //创建查询解析器
            QueryParser parser = new QueryParser("content", new SmartChineseAnalyzer());
            //创建查询对象
            Query query = parser.parse("浓眉");
            //获取搜索结果 第二个参数n是返回多少条,可以根据情况限制
            TopDocs docs = indexSearcher.search(query, 10);

            //获取总条数
            System.out.println("本次查询共搜索到:" + docs.totalHits + " 条相关数据");
            //获取得分对象
            ScoreDoc[] scoreDocs = docs.scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取文档编号
                int docId = scoreDoc.doc;
                //根据文档编号获取文档内容
                Document document = indexReader.document(docId);
                System.out.println("id: " + docId);
                System.out.println("title: " + document.getField("title"));
                System.out.println("content: " + document.getField("content"));
                System.out.println("搜索得分: " + scoreDoc.score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
