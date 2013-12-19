package com.tinyms.api;


import com.tinyms.core.Api;
import com.tinyms.core.HttpContext;

/**
 * Created by tinyms on 13-12-18.
 */
@Api(name="com.tinyms.api.test")
public class Test {
    public Object say(HttpContext context){
        System.out.println(context.realpath);
        return new String[]{"1111","2222"};
    }
}
