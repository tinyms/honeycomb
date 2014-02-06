package com.tinyms.api;

import com.tinyms.data.Orm;
import tornadoj.web.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by tinyms on 14-2-5.
 */
@Api(name = "call")
public class CallOrderApi {
    private final static Logger Log = Logger.getAnonymousLogger();
    @Function()
    public Object company(HttpContext context) {
        int companyId = context.paramInt("id", 0);
        Object[] company = (Object[])Orm.self().createQuery("select name,address,businessCategory from Company where id=:id_")
                .setInteger("id_",companyId).uniqueResult();
        ApiResult r = new ApiResult();
        Map<String, Object> data = new HashMap<String, Object>();
        if(company!=null){
            data.put("name", company[0]);
            data.put("address", company[1]);
            data.put("categories", company[2]);
            r.setSuccess(true);
            r.setData(data);
        }else{
            r.setSuccess(false);
            r.setMessage("NoCompany");
            r.setData(data);
        }
        return r;
    }

    @Function()
    public Object current(HttpContext context) {
        int id = context.paramInt("id", 0);
        String category = context.param("category", "");
        Map<String, Object> data = new HashMap<String, Object>();
        ApiResult r = new ApiResult();
        if(id!=0){
            data.put("content", random());
            data.put("total", 97);
            data.put("avg", 24);
            r.setSuccess(true);
            r.setData(data);
        }else{
            r.setSuccess(false);
            r.setMessage("NoFound");
            r.setData(data);
        }
        return r;
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
        ApiResult r = new ApiResult();
        if(id!=0){
            List<String> ads = new ArrayList<String>();
            ads.add(TornadoJConfiguration.SiteUrl + "/adtest/1.gif");
            ads.add(TornadoJConfiguration.SiteUrl + "/adtest/2.gif");
            ads.add(TornadoJConfiguration.SiteUrl + "/adtest/3.gif");
            data.put("items", ads);
            r.setData(data);
            r.setSuccess(true);
        }else {
            r.setSuccess(false);
            r.setData(data);
            r.setMessage("NoFound");
        }
        return r;
    }

    @Function()
    public Object hello_ad(HttpContext context) {
        ApiResult r = new ApiResult();
        r.setSuccess(true);
        r.setData(TornadoJConfiguration.SiteUrl + "/hadtest/2014-02-05.png");
        return r;
    }

    @Function()
    public Object public_ad(HttpContext context) {
        List<String> imgs = new ArrayList<String>();
        imgs.add(TornadoJConfiguration.SiteUrl + "/pubadtest/1.gif");
        imgs.add(TornadoJConfiguration.SiteUrl + "/pubadtest/2.gif");
        imgs.add(TornadoJConfiguration.SiteUrl + "/pubadtest/3.gif");
        ApiResult r = new ApiResult();
        r.setSuccess(true);
        r.setData(imgs);
        return r;
    }

    @Function()
    public Object submit(HttpContext context) {
        String message = context.param("message", "");
        ApiResult r = new ApiResult();
        r.setSuccess(true);
        return r;
    }

    @Function(name = "serverurl")
    public Object serverUrl(HttpContext context){
        ApiResult r = new ApiResult();
        r.setData("http://219.136.251.99:8080");
        r.setSuccess(true);
        return r;
    }
}
