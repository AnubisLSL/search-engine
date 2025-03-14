package com.anubis.li.searchengine.ext;

import org.apache.lucene.analysis.Analyzer;

public class IKAnalyzer4Lucene extends Analyzer {

    private boolean useSmart = false;

    public IKAnalyzer4Lucene() {
        this(false);
    }

    public IKAnalyzer4Lucene(boolean useSmart) {
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
