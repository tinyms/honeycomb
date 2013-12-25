package com.tinyms.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinyms on 13-12-25.
 */
public class Configuration {

    /**
     * WebApp absolute path
     */
    public static String WebAbsPath = "/";
    /**
     * Lucene index store root path
     */
    public static String LuceneIndexerPath = "d:/lucene-index/";
    /**
     * mysql,sqlite,postgres,mssql,oracle
     */
    public static String DatabaseSchama = "mysql";
    /**
     * add plugin package name for search
     */
    public static List<String> PluginPackageNames = new ArrayList<String>();
    /**
     * File upload cache size, default 1M
     */
    public static int FileUploadSizeTempCache = 1024 * 1024;
    /**
     * File upload limit size, default 50M
     */
    public static int FileUploadSizeLimit = 5 * 1024 * 1024;

}
