package com.tinyms.view;

import tornadoj.web.HttpContext;
import tornadoj.web.Route;
import tornadoj.web.WebModule;

/**
 * Created by tinyms on 14-2-7.
 */
@WebModule(name = "callorder")
public class CallOrderRoute {
    @Route(name = "/index")
    public void index(HttpContext context){
        context.render("main.ftl");
    }

    @Route(name = "/users")
    public void users(HttpContext context){
        context.render("main.ftl");
    }

    @Route(name = "/companys")
    public void companys(HttpContext context){
        context.render("main.ftl");
    }

    @Route(name = "/feedback")
    public void feedback(HttpContext context){
        context.render("main.ftl");
    }
}
