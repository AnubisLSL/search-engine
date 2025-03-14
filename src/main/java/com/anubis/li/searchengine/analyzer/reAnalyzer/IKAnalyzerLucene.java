package com.anubis.li.searchengine.analyzer.reAnalyzer;

import com.anubis.li.searchengine.ext.IKTokenizer4Lucene7;
import org.apache.lucene.analysis.Analyzer;

public class IKAnalyzerLucene extends Analyzer {
    private boolean useSmart = false;

    public IKAnalyzerLucene() {
        this(false);
    }

    public IKAnalyzerLucene(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    public boolean isUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        IKTokenizer4Lucene7 tk = new IKTokenizer4Lucene7(this.useSmart);
        return new TokenStreamComponents(tk);
    }
}
