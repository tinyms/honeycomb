package com.tinyms.app;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Route;
import com.tinyms.core.WebView;

/**
 * Created by tinyms on 13-12-22.
 */
@WebView(name = "match")
public class MatchsView {
    @Route(name = "index")
    public void render(HttpContext context) {
        context.render("match.ftl", null);
    }
}
