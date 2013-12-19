package com.tinyms.core;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 * Created by tinyms on 13-12-18.
 */
public interface ILuceneWriter {
    public void doInWrite(IndexWriter writer);
}
