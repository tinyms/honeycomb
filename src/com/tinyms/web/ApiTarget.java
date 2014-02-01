package com.tinyms.web;

/**
 * Created by tinyms on 14-2-1.
 */
public class ApiTarget {
    private boolean auth;
    private Object instance;

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }
}
