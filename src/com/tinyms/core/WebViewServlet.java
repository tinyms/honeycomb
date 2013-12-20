package com.tinyms.core;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        IWebView view = ClassLoaderUtil.getRouteObject(path);
        if(view!=null){
            HttpContext context = new HttpContext();
            context.realpath = request.getServletContext().getRealPath("/");
            context.request = request;
            context.response = response;
            view.render(context);
        }else{
            pw.write("View not found.");
        }
        pw.close();
    }
}
