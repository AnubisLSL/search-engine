package com.anubis.li.searchengine.analyzer;

import com.anubis.li.searchengine.attribute.BuildAttribute;
import com.anubis.li.searchengine.tokenFilter.BuildTokenFilter;
import com.anubis.li.searchengine.tokenizer.BuildTokenizer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;

import java.io.IOException;

public class BuildAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String address) {
        Tokenizer source =new BuildTokenizer();
        TokenStream filter = new BuildTokenFilter(source);
        return new TokenStreamComponents(source,filter);
    }
    public static void main(String[] args) {
        //英文按照空白字符进行分词
        String text = "An AttributeSource contains a list of different AttributeImpls, and methods to add and get them. ";

        try {
            Analyzer ana = new BuildAnalyzer();
            TokenStream ts = ana.tokenStream("aa", text);
            BuildAttribute ca = ts.getAttribute(BuildAttribute.class);
            ts.reset();
            while (ts.incrementToken()) {
                System.out.print(ca.getString() + "|");
            }
            ts.end();
            ana.close();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
