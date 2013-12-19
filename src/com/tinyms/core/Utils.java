package com.tinyms.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 13-12-18.
 */
public class Utils {
    private static Logger Log = Logger.getAnonymousLogger();
    //Re valid
    public static int parseInt(String text, int defalutValue){
        return NumberUtils.toInt(text, defalutValue);
    }
    public static float parseFloat(String text, float defalutValue){
        return NumberUtils.toFloat(text, defalutValue);
    }
    public static double parseDouble(String text, double defalutValue){
            return NumberUtils.toDouble(text,defalutValue);
    }
    public static List<String> parseChinese(String text){
        List<String> chineses = new ArrayList<String>();
        Pattern pattern = Pattern.compile("[\u4E00-\u9FA5]+");
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            chineses.add(matcher.group());
        }
        return chineses;
    }
    public static String encode(Object o){
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(o);
    }
    public static String encode(Object o, String dateFormat){
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.toJson(o);
    }
    public static <T> T decode(String json, Class<T> c){
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }
    //Utils
    public static void mkdirs(String path){
        File dirs = new File(path);
        dirs.mkdirs();
    }

    public static boolean fileOrDirExists(String path){
        File entity = new File(path);
        return entity.exists();
    }

    public static void main(String[] args){
        double n = parseDouble("2323.111", 0);
        System.out.println(n);
        float f = NumberUtils.toFloat("11",0);
        System.out.println(parseChinese("感冒 AND (广州 OR 江苏) +咽痛 AND 口 生疮"));
    }
}
