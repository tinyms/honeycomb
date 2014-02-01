package com.tinyms.web;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinyms on 13-12-20.
 */
@WebServlet(name = "WebViewServlet", urlPatterns = {"*.html"})
public class WebViewServlet extends HttpServlet {
    private static Logger Log = Logger.getAnonymousLogger();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doView(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doView(request, response);
    }

    private void doView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        String path = request.getServletPath();
        if (path == null) {
            path = "/index";
        }
        RouteTarget routeTarget = ClassLoaderUtil.getRouteObject(path);
        if (routeTarget != null) {
            boolean execute = true;
            String text = "";
            if (StringUtils.isNotBlank(routeTarget.getParamPatterns())) {
                text = getParamsStringByPatterns(path, routeTarget.getParamPatterns());
                if (StringUtils.isBlank(text)) {
                    execute = false;
                }
            }
            if (execute) {
                HttpContext context = new HttpContext();
                context.request = request;
                context.response = response;
                context.plainParams = getParams(text, routeTarget.getParamExtractor());
                try {
                    routeTarget.getMethod().invoke(routeTarget.getTarget(), context);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                pw.write("Params pattern err.");
            }
        } else {
            pw.write("Route not found.");
        }
        pw.close();
    }

    private static String getParamsStringByPatterns(String path, String re) {
        Pattern pattern = Pattern.compile(re + "(?=\\.html)");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private static List<String> getParams(String paramsString, String re) {
        List<String> params = new ArrayList<String>();
        if (StringUtils.isBlank(paramsString)) {
            return params;
        }
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(paramsString);
        while (matcher.find()) {
            params.add(matcher.group());
        }
        return params;
    }
}
