package com.tinyms.web;

import java.lang.reflect.Method;

/**
 * Created by tinyms on 14-2-1.
 */
public class FunctionTarget {
    private boolean auth;
    private Method method;

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
