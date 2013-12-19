package com.tinyms.api;

import com.tinyms.core.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.highlight.Highlighter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tinyms on 13-12-18.
 */

@Api(name = "com.tinyms.hospital.doc")
public class HospitalSearch {
    public Object search(HttpContext context){
        int page = Utils.parseInt(context.request.getParameter("page"),1);
        int limit = Utils.parseInt(context.request.getParameter("limit"),20);
        String keyWords = context.request.getParameter("kw");
        LuceneUtil lucene = new LuceneUtil("E:/lucene/hospital/");
        return lucene.paginate(keyWords,new String[]{"content"},page,limit,new ILuceneSearch() {
            @Override
            public Object doInSearch(Document doc, Analyzer analyzer, Highlighter highlighter) {
                Map<String,String> item = new HashMap<String, String>();
                String id = doc.get("id");
                String product_name = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"product_name");
                String provider_name = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"provider_name");
                String index_no = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"index_no");
                String build = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"build");
                String func = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"func");
                String bak = LuceneUtil.colorKeyWords(doc,analyzer,highlighter,"bak");
                item.put("id",id);
                item.put("product_name",product_name);
                item.put("provider_name",provider_name);
                item.put("index_no",index_no);
                item.put("build",build);
                item.put("func",func);
                item.put("bak",bak);
                return item;
            }
        });
    }
}
