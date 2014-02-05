package com.tinyms.api;

import tornadoj.web.Api;
import tornadoj.web.Function;
import tornadoj.web.HttpContext;
import tornadoj.web.TornadoJConfiguration;

import java.util.*;

/**
 * Created by tinyms on 14-2-5.
 */
@Api(name = "call")
public class CallOrderApi {
    @Function()
    public Object company(HttpContext context) {
        int companyId = context.paramInt("id", 0);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "粤扬信息");
        data.put("category", new String[]{"vip", "common"});
        return data;
    }

    @Function()
    public Object current(HttpContext context) {
        int id = context.paramInt("id", 0);
        String category = context.param("category", "");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("no", random());
        data.put("total", 97);
        data.put("avg", 24);
        return data;
    }

    private static String random() {
        String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] c = s.toCharArray();
        Random random = new Random();
        return String.format("%s", c[random.nextInt(c.length)]);
    }

    @Function()
    public Object ad(HttpContext context) {
        int id = context.paramInt("id", 0);
        String category = context.param("category", "");
        Map<String, Object> data = new HashMap<String, Object>();
        List<String> ads = new ArrayList<String>();
        ads.add(TornadoJConfiguration.SiteUrl + "/1.png");
        ads.add(TornadoJConfiguration.SiteUrl + "/2.png");
        data.put("items", ads);
        return data;
    }

    @Function()
    public Object hello_ad(HttpContext context) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("imgUrl", TornadoJConfiguration.SiteUrl + "/hello_ad.png");
        return data;
    }

    @Function()
    public Object submit(HttpContext context) {
        String message = context.param("message", "");
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("success", true);
        return data;
    }
}
