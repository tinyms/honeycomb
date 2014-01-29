package com.tinyms.view;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Route;
import com.tinyms.core.Utils;
import com.tinyms.core.WebModule;

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
        context.render("hospital/index.ftl", null);
    }

    @Route(name = "users")
    public void users(HttpContext context) {
        context.render("hospital/users.ftl", null);
    }
}
