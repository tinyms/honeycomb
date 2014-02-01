package com.tinyms.data;

import org.apache.lucene.index.IndexWriter;

/**
 * Created by tinyms on 13-12-18.
 */
public interface ILuceneWriter {
    public void doInWrite(IndexWriter writer);
}
