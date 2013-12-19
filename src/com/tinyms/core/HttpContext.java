package com.tinyms.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tinyms on 13-12-18.
 */
public class HttpContext {
    public HttpServletRequest request;
    public HttpServletResponse response;
    public String realpath;
}
