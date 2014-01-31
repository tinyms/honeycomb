package com.tinyms.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinyms on 13-12-25.
 */
public class HoneycombConfiguration {
    public static String Ver = "1.0";

    public static String SiteUrl = "http://localhost:8080/";
    /**
     * WebApp absolute path
     */
    public static String WebAbsPath = "/";
    /**
     * template path
     */
    public static String TemplatePath = "/WEB-INF/templates/";
    /**
     * if recompile layout file to ftl
     */
    public static boolean CacheLayout = false;
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
