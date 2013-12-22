package com.tinyms.core;

import java.lang.reflect.Method;

/**
 * Created by tinyms on 13-12-22.
 */
public class RouteTarget {
    private Object target;
    private Method method;

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
}
