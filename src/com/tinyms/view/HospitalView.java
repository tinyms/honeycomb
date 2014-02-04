package com.tinyms.view;

import tornadoj.web.HttpContext;
import tornadoj.web.Route;
import tornadoj.web.Utils;
import tornadoj.web.WebModule;

import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-27.
 */
@WebModule(name = "/hospital")
public class HospitalView {
    private final static Logger Log = Logger.getAnonymousLogger();

    @Route(name = "index")
    public void index(HttpContext context) {
        context.render("hospital/index.ftl");
    }

    @Route(name = "/test/mutilanguage", paramPatterns = "\\w+-\\d+", paramExtractor = "\\w+")
    public void test(HttpContext context) {
        Log.info(Utils.encode(context.plainParams));
        context.render("hospital/test.ftl");
    }

    @Route(name = "users")
    public void users(HttpContext context) {
        context.render("hospital/users.ftl");
    }
}
