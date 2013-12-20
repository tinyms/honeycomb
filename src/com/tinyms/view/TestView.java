package com.tinyms.view;

import com.tinyms.core.HttpContext;
import com.tinyms.core.IWebView;
import com.tinyms.core.Route;

/**
 * Created by tinyms on 13-12-20.
 */
@Route(url = "/hi/123/kao")
public class TestView implements IWebView {
    @Override
    public void render(HttpContext context) {
        context.render("index.ftl",null);
    }
}
