package com.tinyms.app;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Utils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 13-12-22.
 */
public class MatchParseThread implements Runnable {
    private static Logger Log = Logger.getAnonymousLogger();
    public static List<Match> matches = new ArrayList<Match>();
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private static String findNum(String text) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static String parseScript(String text) {
        Pattern pattern = Pattern.compile("\\[.*\\]", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            return StringUtils.replace(matcher.group(), "null", "''");
        }
        return "";
    }



    private static List<Match> parse(String url) {
        Log.warning(url);
        List<List<String>> items = new ArrayList<List<String>>();
        matches.clear();
        if(StringUtils.isBlank(url)){
            return matches;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements trs = doc.select("#livescore_table .tableborder tr");
            for (Element e : trs) {
                List<String> item = new ArrayList<String>();
                Elements linkElements = e.select("a");
                for (Element link : linkElements) {
                    if (item.size() == 1) {
                        String href = link.attr("href");
                        String num = findNum(href);
                        if (StringUtils.isNotBlank(num)) {
                            item.add(num);
                        }
                    }
                    if (item.size() == 5) {
                        break;
                    }
                    item.add(link.html());
                }
                if (item.size() > 0) {
                    items.add(item);
                }
            }

            for (List<String> item : items) {
                Match m = new Match();
                String url_odds = String.format("http://www.okooo.com/soccer/match/%s/odds/", item.get(1));
                Log.warning("Parse: "+url_odds);
                doc = Jsoup.connect(url_odds).get();
                Elements scripts = doc.select("script[type=text/javascript]:not([src~=[a-zA-Z0-9./\\s]+)");
                for (Element script : scripts) {
                    String data = script.data();
                    if (data.indexOf("var data_str='[{") != -1) {
                        item.add(parseScript(script.data()));
                        break;
                    }
                }
                m.setJf(parseJf(doc));
                m.setSeason(item.get(0));
                m.setId(Utils.parseInt(item.get(1), 0));
                m.setMain(item.get(2));
                m.setScore(item.get(3));
                m.setClient(item.get(4));
                m.setData(item.get(5));
                matches.add(m);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.warning("Completed!");
        return matches;
    }

    private static List<List<String>> parseJf(Document doc){
        List<List<String>> html = new ArrayList<List<String>>();
        Elements trs = doc.select("#jflist tr.topjfbg");
        for(Element tr : trs){
            List<String> td_content = new ArrayList<String>();
            Elements tds = tr.select("td");
            for(Element td : tds){
                td_content.add(td.html());
            }
            html.add(td_content);
        }
        return html;
    }

    public static void main(String[] args){
        try {
            Document doc = Jsoup.connect("http://www.okooo.com/soccer/match/608505/odds/").get();
            Elements trs = doc.select("#jflist tr.topjfbg");
            for(Element tr : trs){
                List<String> td_content = new ArrayList<String>();
                Elements tds = tr.select("td");
                for(Element td : tds){
                    td_content.add(td.html());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        parse(this.getUrl());
    }
}
