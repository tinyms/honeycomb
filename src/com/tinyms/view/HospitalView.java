package com.tinyms.view;

import com.tinyms.core.HttpContext;
import com.tinyms.core.Route;
import com.tinyms.core.WebView;

/**
 * Created by tinyms on 13-12-27.
 */
@WebView(name = "hospital")
public class HospitalView {
    @Route(name = "index")
    public void index(HttpContext context){
        context.render("hospital/index.ftl",null);
    }
    @Route(name = "users")
    public void users(HttpContext context){
        context.render("hospital/users.ftl",null);
    }
}
