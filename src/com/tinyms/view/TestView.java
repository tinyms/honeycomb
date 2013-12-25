package com.tinyms.view;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Route;
import com.tinyms.core.WebView;

/**
 * Created by tinyms on 13-12-20.
 */
@WebView(name = "test")
public class TestView {
    @Route(name = "ftl")
    public void render(HttpContext context) {
        context.render("index.ftl", null);
    }
}
