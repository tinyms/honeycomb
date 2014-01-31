package com.tinyms.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinyms on 13-12-18.
 */
public class HttpContext {
    private static Configuration freemarkerConfiguration = new Configuration();
    public HttpServletRequest request;
    public HttpServletResponse response;
    public List<String> plainParams = new ArrayList<String>();

    public void render(String tplPath, Object data) {
        try {
            String cacheTpl = Layout.compile(tplPath);
            Template tpl = freemarkerConfiguration.getTemplate(cacheTpl, "utf-8");
            tpl.process(data, response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public Object currentUser(){
        return request.getSession().getAttribute("__CURRENT_USER__");
    }

    public void render(String text) {
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Configuration getFreemarkerConfiguration() {
        return freemarkerConfiguration;
    }
}
