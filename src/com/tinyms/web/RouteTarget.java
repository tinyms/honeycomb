package com.tinyms.web;

import java.lang.reflect.Method;

/**
 * Created by tinyms on 13-12-22.
 */
public class RouteTarget {
    private Object target;
    private Method method;
    private String paramPatterns;
    private String paramExtractor;
    private boolean auth;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getParamPatterns() {
        return paramPatterns;
    }

    public void setParamPatterns(String paramPatterns) {
        this.paramPatterns = paramPatterns;
    }

    public String getParamExtractor() {
        return paramExtractor;
    }

    public void setParamExtractor(String paramExtractor) {
        this.paramExtractor = paramExtractor;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }
}
