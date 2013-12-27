package com.tinyms.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 13-12-18.
 */
public class Utils {
    private static Logger Log = Logger.getAnonymousLogger();
    /**
     * 1024 byte
     */
    public static int KB = 1024;
    /**
     * 1024 M
     */
    public static int MB = 1024 * KB;
    /**
     * 1024 G
     */
    public static int GB = 1024 * MB;
    /**
     * 1024 T
     */
    public static int TB = 1024 * GB;
    private static CacheManager cacheManager = CacheManager.create();

    public static void log(Object o){
        System.out.println(encode(o));
    }

    //Re valid
    public static int parseInt(String text, int defalutValue) {
        return NumberUtils.toInt(text, defalutValue);
    }

    public static float parseFloat(String text, float defalutValue) {
        return NumberUtils.toFloat(text, defalutValue);
    }

    public static double parseDouble(String text, double defalutValue) {
        return NumberUtils.toDouble(text, defalutValue);
    }

    public static List<String> parseChinese(String text) {
        List<String> chineses = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            chineses.add(matcher.group());
        }
        return chineses;
    }

    public static String encode(Object o) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(o);
    }

    public static String encode(Object o, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.toJson(o);
    }

    public static <T> T decode(String json, Class<T> c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

    //Utils
    public static void mkdirs(String path) {
        File dirs = new File(path);
        dirs.mkdirs();
    }

    public static boolean fileOrDirExists(String path) {
        File entity = new File(path);
        return entity.exists();
    }

    public static String md5(String s) {
        char hexChar[] = {'0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        byte[] b = s.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(b);
            byte[] b2 = md.digest();
            char str[] = new char[b2.length << 1];
            int len = 0;
            for (int i = 0; i < b2.length; i++) {
                byte val = b2[i];
                str[len++] = hexChar[(val >>> 4) & 0xf];
                str[len++] = hexChar[val & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //sql datetime
    public static Timestamp timestamp(Date date){
        return new java.sql.Timestamp(date.getTime());
    }

    public static Timestamp current_timestamp(){
        return new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
    }

    //Http utils

    /**
     * 批量下载网页
     *
     * @param urls 网页路径集合
     * @param path x:/abc/
     * @return
     */
    public static boolean batchDownloadHtml(List<String> urls, final String path) {
        Log.info(path);
        mkdirs(path);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 1, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());
        for (final String url : urls) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CloseableHttpClient http = HttpClients.createDefault();
                    HttpGet httpget = new HttpGet(url);
                    httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/30.0.1084.52 Safari/536.5");
                    CloseableHttpResponse response;
                    try {
                        response = http.execute(httpget);
                        HttpEntity entity = response.getEntity();
                        FileOutputStream os = new FileOutputStream(path + md5(url) + ".html");
                        entity.writeTo(os);
                        response.close();
                        os.close();
                        Log.warning("完成:"+url);
                    } catch (IOException e) {
                        Log.warning("Download Failure!");
                    }
                }
            });
        }
        executor.shutdown();
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                Log.warning("Awaiting download completed.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.warning("Download completed.");
        return true;
    }

    //for cache manager

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

    public static <T> T getCache(String path) {
        Cache c = cacheManager.getCache("honeycomb");
        if (c == null) {
            return null;
        }
        Element e = c.get(path);
        if (e == null) {
            return null;
        }
        return (T) e.getObjectValue();
    }

    public static void setCache(String path, Object data) {
        Cache c = cacheManager.getCache("honeycomb");
        if (c != null) {
            c.put(new Element(path, data));
        }
    }

    public static void removeCache(String path) {
        Cache c = cacheManager.getCache("honeycomb");
        if (c == null) {
            return;
        }
        List keys = c.getKeys();
        for (Object key : keys) {
            if (String.valueOf(key).startsWith(path)) {
                c.remove(key);
            }
        }
    }

    public static void main(String[] args) {
        //List<String> urls = new ArrayList<String>();
        //urls.add("http://www.okooo.com/livecenter/zucai/?LotteryNo=13182");
        //batchDownloadHtml(urls, "e:/");
        //setCache("/a/b", "dsdsdsd");
    }
}
