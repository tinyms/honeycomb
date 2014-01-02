package com.tinyms.app;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Orm;
import com.tinyms.core.Route;
import com.tinyms.core.WebView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by tinyms on 13-12-22.
 */
@WebView(name = "match")
public class MatchsView {
    @Route(name = "index")
    public void index(HttpContext context) {
        context.render("match.ftl", null);
    }
    @Route(name = "history")
    public void history(HttpContext context) {
        List noArr = Orm.self().createQuery("select distinct(no) from Match").list();
        Map<String,Object> opt = new HashMap<String, Object>();
        opt.put("noArr",noArr);
        context.render("history.ftl", opt);
    }
}
