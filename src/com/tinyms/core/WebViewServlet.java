package com.tinyms.core;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

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
        if(path==null){
            path = "/index.html";
        }
        RouteTarget route = ClassLoaderUtil.getRouteObject(path);
        if(route!=null){
            HttpContext context = new HttpContext();
            context.request = request;
            context.response = response;
            try {
                route.getMethod().invoke(route.getTarget(),context);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else{
            pw.write("View not found.");
        }
        pw.close();
    }
}
