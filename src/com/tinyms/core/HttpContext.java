package com.tinyms.core;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tinyms on 13-12-18.
 */
public class HttpContext {
    private static Configuration freemarkerConfiguration = new Configuration();
    public HttpServletRequest request;
    public HttpServletResponse response;
    public String realpath;
    public void render(String tplPath,Object data){
        try {
            Template tpl = freemarkerConfiguration.getTemplate(tplPath, "utf-8");
            tpl.process(data,response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
    public void render(String text){
        try {
            response.getWriter().write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Configuration getFreemarkerConfiguration(){
        return freemarkerConfiguration;
    }
}
