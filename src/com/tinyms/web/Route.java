package com.tinyms.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tinyms on 13-12-20.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Route {
    String name() default "";
    String paramPatterns() default "";
    String paramExtractor() default "\\w+";
    boolean auth() default false;
}