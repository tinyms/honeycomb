package com.tinyms.data;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.highlight.Highlighter;

/**
 * Created by tinyms on 13-12-19.
 */

public interface ILuceneSearch {
    /**
     * custom search data output
     *
     * @param doc         Document
     * @param analyzer    Analyzer
     * @param highlighter Highlighter
     * @return can gson output
     */
    public Object doInSearch(Document doc, Analyzer analyzer, Highlighter highlighter);
}
