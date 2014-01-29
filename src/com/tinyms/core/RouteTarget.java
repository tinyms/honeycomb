package com.tinyms.core;

import java.lang.reflect.Method;

/**
 * Created by tinyms on 13-12-22.
 */
public class RouteTarget {
    private Object target;
    private Method method;
    private String paramPatterns;
    private String paramExtractor;

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
}
