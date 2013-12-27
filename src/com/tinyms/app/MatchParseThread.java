package com.tinyms.app;

import com.tinyms.core.Configuration;
import com.tinyms.core.Database;
import com.tinyms.core.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private static boolean isHistory = true;

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
        if (StringUtils.isBlank(url)) {
            return matches;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements trs = doc.select("#livescore_table .tableborder tr");
            boolean first = true;
            for (Element e : trs) {
                if(first){
                    first = false;
                    continue;
                }
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
            List<String> urls = new ArrayList<String>();
            for (List<String> item : items) {
                urls.add(String.format("http://www.okooo.com/soccer/match/%s/odds/", item.get(1)));
            }
            String path = Configuration.WebAbsPath + "download/odds/";
            boolean b = Utils.batchDownloadHtml(urls, path);
            if (b) {
                for (List<String> item : items) {
                    Match m = new Match();
                    String f_name = String.format("http://www.okooo.com/soccer/match/%s/odds/", item.get(1));
                    doc = Jsoup.parse(new File(path + Utils.md5(f_name) + ".html"), "gb2312");
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
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.warning("Completed!");
        return matches;
    }

    private static List<List<String>> parseJf(Document doc) {
        List<List<String>> html = new ArrayList<List<String>>();
        Elements trs = doc.select("#jflist tr.topjfbg");
        for (Element tr : trs) {
            List<String> td_content = new ArrayList<String>();
            Elements tds = tr.select("td");
            for (Element td : tds) {
                td_content.add(td.html());
            }
            html.add(td_content);
        }
        return html;
    }

    public static void main(String[] args) {

    }

    @Override
    public void run() {
        if(isHistory){
            Date now = Calendar.getInstance().getTime();
            for(int k=1;k<=100;k++){
                Date next = DateUtils.addDays(now,-1*k);
                String no = DateFormatUtils.format(next,"yyyy-MM-dd");
                String url = String.format("http://www.okooo.com/livecenter/jingcai/?LotteryNo=%s",no);
                List<Match> matches1 = parse(url);
                Database.insert("insert into match(no,data)values(?,?)",new Object[]{no,Utils.encode(matches1)});
            }

        }else{
            parse(this.getUrl());
        }
    }
}
