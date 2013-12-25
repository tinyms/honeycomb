package com.tinyms.app;


import com.tinyms.core.Api;
import com.tinyms.core.HttpContext;

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
}
