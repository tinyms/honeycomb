package com.tinyms.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 14-1-29.
 */
public class Layout {
    private final static Logger Log = Logger.getAnonymousLogger();
    public static String getMasterTemplateFilePath(String template){
        Pattern pattern = Pattern.compile("\\{%\\s*extends\\s+.*%\\}");
        Matcher matcher = pattern.matcher(template);
        if (matcher.find()) {
            String extends_ = matcher.group();
            pattern = Pattern.compile("\".*\"");
            matcher = pattern.matcher(extends_);
            if(matcher.find()){
                return matcher.group().replace("\"","");
            }
        }
        return "";
    }

    public static List<Map<String,String>> parseBlocks(String template){
        List<Map<String, String>> blocks = new ArrayList<Map<String, String>>();
        Pattern pattern = Pattern.compile("\\{%\\s*block\\s+\\w+\\s*%\\}.*?\\{%\\s*end\\s*%\\}",Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        while(matcher.find()){
            String block = matcher.group();
            Pattern p1 = Pattern.compile("block\\s+\\w+");
            Matcher m1 = p1.matcher(block);
            if(m1.find()){
                String blockName = m1.group();
                Pattern p2 = Pattern.compile("%\\}(.*?)\\{%",Pattern.DOTALL);
                Matcher m2 = p2.matcher(block);
                String content = "";
                while(m2.find()){
                    content = m2.group();
                }
                Map<String,String> item = new HashMap<String, String>();
                item.put("blockName",blockName);
                item.put("blockBody",content);
                blocks.add(item);
                Log.info(blockName);
                Log.info(content);
            }
        }
        return blocks;
    }
}
