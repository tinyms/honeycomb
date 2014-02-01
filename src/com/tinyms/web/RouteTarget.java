package com.tinyms.web;

import java.lang.reflect.Method;

/**
 * Created by tinyms on 13-12-22.
 */
public class RouteTarget {
    private String className;
    private Method method;
    private String paramPatterns;
    private String paramExtractor;
    private boolean auth;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
