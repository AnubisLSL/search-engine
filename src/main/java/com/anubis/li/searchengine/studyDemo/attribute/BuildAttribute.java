package com.anubis.li.searchengine.studyDemo.attribute;

import org.apache.lucene.util.Attribute;

public interface BuildAttribute extends Attribute {

    public void setChars(char[] buffer,int length);

    public char[] getChars();

    public int getLength();

    public String getString();
}
