package com.tinyms.api;

import com.tinyms.core.Api;
import com.tinyms.core.HttpContext;
import com.tinyms.core.Orm;
import com.tinyms.core.Utils;
import com.tinyms.data.AccountHelper;
import com.tinyms.entity.Account;

/**
 * Created by tinyms on 13-12-29.
 */
@Api(name = "com.tinyms.api.user")
public class UserApi {
    public Object list(HttpContext context){
        int page = Utils.parseInt(context.request.getParameter("page"), 1);
        int limit = Utils.parseInt(context.request.getParameter("limit"),20);
        return AccountHelper.list(page, limit);
    }

    public Object get(HttpContext context){
        Long id = Long.valueOf(Utils.parseInt(context.request.getParameter("id"),0));
        Account user = (Account)Orm.self().get(Account.class,id);
        if(user==null){
            user = new Account();
        }
        return user;
    }
}
