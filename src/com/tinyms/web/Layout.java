package com.tinyms.web;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 14-1-29.
 */
public class Layout {
    private final static Logger Log = Logger.getAnonymousLogger();

    public static String compile(String tplPath) {
        //is cache?
        String cacheFileName = Utils.md5(tplPath) + ".layoutc";
        String cachePath = HoneycombConfiguration.TemplatePath + "cache/";

        //for output file
        File cacheFile = new File(cachePath + cacheFileName);
        if (HoneycombConfiguration.CacheLayout && cacheFile.exists()) {
            return String.format("cache/%s", cacheFileName);
        }
        //compile layout file to ftl

        String template_content = "";
        File tplFile = new File(HoneycombConfiguration.TemplatePath + tplPath);
        try {
            template_content = FileUtils.readFileToString(tplFile, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String masterTemplateFilePath = Layout.getMasterTemplateFilePath(template_content);
        String path = tplFile.getParentFile().getAbsolutePath();
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        try {
            String masterFileContent = FileUtils.readFileToString(new File(path + masterTemplateFilePath), "utf-8");
            Map<String, String> blocks = Layout.parseBlocks(template_content);
            for (String key : blocks.keySet()) {
                masterFileContent = masterFileContent.replaceAll("\\{%\\s*" + key + "\\s*%\\}", blocks.get(key));
            }
            FileUtils.writeStringToFile(cacheFile, masterFileContent, "utf-8");
            //Log.info(Utils.encode(blocks));
            //Log.info(masterFileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cacheFile.exists()) {
            return String.format("cache/%s", cacheFileName);
        }

        return "";
    }

    public static String getMasterTemplateFilePath(String template) {
        Pattern pattern = Pattern.compile("\\{%\\s*extends\\s+.*%\\}");
        Matcher matcher = pattern.matcher(template);
        if (matcher.find()) {
            String extends_ = matcher.group();
            pattern = Pattern.compile("\".*\"");
            matcher = pattern.matcher(extends_);
            if (matcher.find()) {
                return matcher.group().replace("\"", "");
            }
        }
        return "";
    }

    public static Map<String, String> parseBlocks(String template) {
        Map<String, String> blocks = new HashMap<String, String>();
        Pattern pattern = Pattern.compile("\\{%\\s*block\\s+\\w+\\s*%\\}.*?\\{%\\s*end\\s*%\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            String block = matcher.group();
            Pattern p1 = Pattern.compile("block\\s+\\w+");
            Matcher m1 = p1.matcher(block);
            if (m1.find()) {
                String blockName = m1.group();
                Pattern p2 = Pattern.compile("%\\}(.*?)\\{%", Pattern.DOTALL);
                Matcher m2 = p2.matcher(block);
                String content = "";
                while (m2.find()) {
                    content = m2.group().replace("%}", "").replace("{%", "");
                }
                blocks.put(blockName, content);
            }
        }
        return blocks;
    }
}
