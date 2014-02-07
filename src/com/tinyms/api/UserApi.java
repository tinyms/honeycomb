package com.tinyms.api;

import com.tinyms.data.AccountHelper;
import com.tinyms.data.Orm;
import com.tinyms.entity.User;
import tornadoj.web.Api;
import tornadoj.web.Function;
import tornadoj.web.HttpContext;
import tornadoj.web.Utils;

/**
 * Created by tinyms on 13-12-29.
 */
@Api(name = "com.tinyms.api.user")
public class UserApi {
    @Function(auth = true)
    public Object list(HttpContext context) {
        int page = Utils.parseInt(context.request.getParameter("page"), 1);
        int limit = Utils.parseInt(context.request.getParameter("limit"), 20);
        return AccountHelper.list(page, limit);
    }

    @Function()
    public Object get(HttpContext context) {
        Long id = Long.valueOf(Utils.parseInt(context.request.getParameter("id"), 0));
        User user = (User) Orm.self().get(User.class, id);
        if (user == null) {
            user = new User();
        }
        return user;
    }
}
