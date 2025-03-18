package com.anubis.li.searchengine.studyDemo.analyzer;

import org.apache.lucene.util.PagedBytes;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author  songling.li
 * IK分词器demo学习与编写
 */
public class IKAnalyzer {
    public static void main(String[] args) {
        String text = "北京市昌平区霍营地铁站回龙观南大街华龙苑南里15号楼8单元501";
        try (StringReader reader = new StringReader(text)) {
            IKSegmenter ikSegmenter = new IKSegmenter(reader, true);
            Lexeme lexeme;
            while ((lexeme = ikSegmenter.next()) != null) {
                System.out.println(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /** 返回输出结果
     *  加载扩展词典：ext.dic
     * 加载扩展停止词典：stopword.dic
     * 北京市
     * 昌平区
     * 霍
     * 营
     * 地铁站
     * 回龙观
     * 南大街
     * 华龙
     * 苑
     * 南里
     * 13号
     * 楼
     * 8
     * 单元
     * 501
     * 索引时用ik_max_word，在搜索时用ik_smart。
     */
}
