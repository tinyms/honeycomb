package com.tinyms.data;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import tornadoj.web.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-18.
 */
public class LuceneUtil {
    private Logger Log = Logger.getAnonymousLogger();
    private Directory directory = null;

    public LuceneUtil(String storedPath) {
        try {
            Utils.mkdirs(storedPath);
            directory = FSDirectory.open(new File(storedPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Write(ILuceneWriter custom) {
        try {
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                    new StandardAnalyzer(Version.LUCENE_46));
            IndexWriter writer = new IndexWriter(directory, config);
            custom.doInWrite(writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> paginate(String keyWord, String[] fieldNames, int curpage, int pageSize, ILuceneSearch iSearchResultHandler) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("rows", 0);
        result.put("pages", 0);
        result.put("items", new ArrayList());
        keyWord = StringUtils.trimToEmpty(keyWord);
        if (StringUtils.isBlank(keyWord)) {
            return result;
        }
        if (keyWord.indexOf("\"") == -1) {
            List<String> chinese = Utils.parseChinese(keyWord);
            for (String str : chinese) {
                keyWord = StringUtils.replace(keyWord, str, "\"" + str + "\"");
            }
        }
        try {
            if (curpage <= 0) {
                curpage = 1;
            }
            if (pageSize <= 0) {
                pageSize = 20;
            }

            int start = (curpage - 1) * pageSize;
            IndexReader reader = DirectoryReader.open(this.directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);

            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_46, fieldNames, analyzer);
            queryParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query query = queryParser.parse(keyWord);
            int hm = start + pageSize;
            TopScoreDocCollector res = TopScoreDocCollector.create(hm, false);
            searcher.search(query, res);

            Highlighter highlighter = createHighlighter(query);

            List<Object> items = new ArrayList<Object>();
            int rowCount = res.getTotalHits();
            int pages = (rowCount - 1) / pageSize + 1; //计算总页数
            result.put("rows", rowCount);
            result.put("pages", pages);
            TopDocs tds = res.topDocs(start, pageSize);
            ScoreDoc[] sd = tds.scoreDocs;
            for (int i = 0; i < sd.length; i++) {
                Document doc = reader.document(sd[i].doc);
                Object item = iSearchResultHandler.doInSearch(doc, analyzer, highlighter);
                items.add(item);
            }
            result.put("items", items);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Log.warning(e.getLocalizedMessage());
        }

        return result;
    }

    public static void main(String[] args) {
        LuceneUtil lucene = new LuceneUtil("E:/lucene/hospital/");
        Object o = lucene.paginate("病毒", new String[]{"content"}, 1, 20, new ILuceneSearch() {
            @Override
            public Object doInSearch(Document doc, Analyzer analyzer, Highlighter highlighter) {
                Map<String, String> item = new HashMap<String, String>();
                String id = doc.get("id");
                String product_name = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "product_name");
                String provider_name = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "provider_name");
                String index_no = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "index_no");
                String build = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "build");
                String func = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "func");
                String bak = LuceneUtil.colorKeyWords(doc, analyzer, highlighter, "bak");
                item.put("id", id);
                item.put("product_name", product_name);
                item.put("provider_name", provider_name);
                item.put("index_no", index_no);
                item.put("build", build);
                item.put("func", func);
                item.put("bak", bak);
                return item;
            }
        });
        System.out.println(Utils.encode(o));
    }

    public static String colorKeyWords(Document doc, Analyzer analyzer, Highlighter highlighter, String fieldName) {
        String val = doc.get(fieldName);
        if (StringUtils.isBlank(val)) {
            return "";
        }
        try {
            String colorText = highlighter.getBestFragment(analyzer, fieldName, val);
            if (!StringUtils.isBlank(colorText)) {
                return colorText;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return val;
    }

    private static Highlighter createHighlighter(Query query) {
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
        SimpleFragmenter sf = new SimpleFragmenter(100);
        highlighter.setTextFragmenter(sf);
        return highlighter;
    }

    /**
     * 删除索引 删除的索引会保存到一个新的文件中（以del为结尾的文件 相当于删除到回收站）
     *
     * @param force 是否强制清空
     * @throws IOException
     */
    public void delete(String id, boolean force) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteDocuments(new Term("id", id));
        if (force) {
            writer.forceMergeDeletes();
        }
        writer.close();
    }

    /**
     * 删除索引 删除的索引会保存到一个新的文件中（以del为结尾的文件 相当于删除到回收站）
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param force      是否强制清空
     * @throws IOException
     */
    public void delete(String fieldName, String fieldValue, boolean force) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteDocuments(new Term("id", fieldValue));
        if (force) {
            writer.forceMergeDeletes();
        }
        writer.close();
    }

    /**
     * 删除索引 删除的索引会保存到一个新的文件中（以del为结尾的文件 相当于删除到回收站）
     *
     * @throws IOException
     */
    public void update(String id, Document doc) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.updateDocument(new Term("id", id), doc);
        writer.commit();
        writer.close();
    }

    /**
     * 删除索引 删除的索引会保存到一个新的文件中（以del为结尾的文件 相当于删除到回收站）
     *
     * @throws IOException
     */
    public void update(String fieldName, String fieldValue, Document doc) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.updateDocument(new Term(fieldName, fieldValue), doc);
        writer.commit();
        writer.close();
    }

    /**
     * 删除所有的索引 删除的索引会保存到一个新的文件中（以del为结尾的文件 相当于删除到回收站）
     *
     * @throws IOException
     */
    public void deleteAll() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll();
        writer.close();
    }

    /**
     * 删除已经删除的索引 对应上一个删除方法 清空回收站的文件
     *
     * @throws IOException
     */
    public void forceMergeDeletes() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46,
                new StandardAnalyzer(Version.LUCENE_46));
        IndexWriter writer = new IndexWriter(directory, config);
        writer.forceMergeDeletes();
        writer.close();
    }
}
