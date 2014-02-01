package com.tinyms.view;

import com.tinyms.web.HttpContext;
import com.tinyms.web.Route;
import com.tinyms.web.Utils;
import com.tinyms.web.WebModule;

import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-27.
 */
@WebModule(name = "hospital")
public class HospitalView {
    private final static Logger Log = Logger.getAnonymousLogger();
    @Route(name = "index")
    public void index(HttpContext context) {
        context.render("hospital/index.ftl", null);
    }

    @Route(name = "test", paramPatterns = "\\w+-\\d+", paramExtractor = "\\w+")
    public void test(HttpContext context) {
        Log.info(Utils.encode(context.plainParams));
        context.render("hospital/test.ftl", null);
    }

    @Route(name = "users")
    public void users(HttpContext context) {
        context.render("hospital/users.ftl", null);
    }
}
