package com.anubis.li.searchengine.studyDemo.tokenFilter;

import com.anubis.li.searchengine.studyDemo.attribute.BuildAttribute;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;

public class BuildTokenFilter extends TokenFilter {
    public BuildTokenFilter(TokenStream input) {
        super(input);
    }

    BuildAttribute charAttr = this.addAttribute(BuildAttribute.class);
    @Override
    public boolean incrementToken() throws IOException {
        boolean res = this.input.incrementToken();
        if (res) {
            char[] chars = charAttr.getChars();
            int length = charAttr.getLength();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    chars[i] = Character.toLowerCase(chars[i]);
                }
            }
        }
        return res;
    }
}
