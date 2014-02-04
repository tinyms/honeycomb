package com.tinyms.app;

import com.tinyms.data.Database;
import com.tinyms.data.ILuceneWriter;
import com.tinyms.data.LuceneUtil;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import tornadoj.web.Utils;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-18.
 */
public class Hospital {
    private static Logger Log = Logger.getAnonymousLogger();

    public static void create_indexer() {
        if (Utils.fileOrDirExists("E:/lucene/hospital/")) {
            Log.warning("fileOrDirExists(\"E:/lucene/hospital/\")");
            return;
        }
        try {
            final List<Map<String, Object>> ds = Database.self().query("select * from gmfsdetailbook", new MapListHandler());
            LuceneUtil lucene = new LuceneUtil("E:/lucene/hospital/");
            lucene.Write(new ILuceneWriter() {
                @Override
                public void doInWrite(IndexWriter writer) {
                    for (Map<String, Object> row : ds) {
                        String id = row.get("id") + "";
                        String product_name = StringUtils.defaultString(String.format("%s", row.get("product_name")), "");
                        String provider_name = StringUtils.defaultString(String.format("%s", row.get("provider_name")), "");
                        String index_no = StringUtils.defaultString(String.format("%s", row.get("index_no")), "");
                        String build = StringUtils.defaultString(String.format("%s", row.get("build")), "");
                        String func = StringUtils.defaultString(String.format("%s", row.get("func")), "");
                        String bak = StringUtils.defaultString(String.format("%s", row.get("bak")), "");
                        String text = String.format("(%s) %s %s %s %s %s", index_no, product_name, func, build, bak, provider_name);
                        Document doc = new Document();
                        doc.add(new StringField("id", id, Field.Store.YES));
                        doc.add(new StringField("product_name", product_name, Field.Store.YES));
                        doc.add(new StringField("provider_name", provider_name, Field.Store.YES));
                        doc.add(new StringField("index_no", index_no, Field.Store.YES));
                        doc.add(new StringField("build", build, Field.Store.YES));
                        doc.add(new StringField("func", func, Field.Store.YES));
                        doc.add(new StringField("bak", bak, Field.Store.YES));
                        doc.add(new TextField("content", new StringReader(text)));
                        try {
                            writer.addDocument(doc);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ds.clear();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
