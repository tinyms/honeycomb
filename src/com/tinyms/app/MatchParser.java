package com.tinyms.app;


import com.tinyms.core.Api;
import com.tinyms.core.HttpContext;
import com.tinyms.core.Orm;
import com.tinyms.core.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tinyms on 13-12-21.
 */
@Api(name = "com.tinyms.app.310win")
public class MatchParser {
    private static Thread thread = null;

    public Object run(HttpContext context) {
        MatchParseThread t = new MatchParseThread();
        t.setUrl(context.request.getParameter("url"));
        thread = new Thread(t);
        thread.start();
        return "True";
    }

    public Object get(HttpContext context) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("result", MatchParseThread.matches);
        return result;
    }

    public Object history(HttpContext context){
        String no = StringUtils.trimToEmpty(context.request.getParameter("no"));
        String json = (String)Orm.self().createQuery("select data from Match where no=:no_")
                .setParameter("no_", no).setMaxResults(1).uniqueResult();
        return Utils.decode(json,MatchItem.class);
    }
}
