package com.tinyms.web;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-17.
 */
@WebServlet(name = "ApiServlet", urlPatterns = "/api/*")
public class ApiServlet extends HttpServlet {
    private Logger Log = Logger.getAnonymousLogger();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doApi(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doApi(request, response);
    }

    private void doApi(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        response.setContentType("application/json");
        PrintWriter pw = response.getWriter();
        String path = request.getPathInfo();

        if (StringUtils.isNotBlank(path)) {
            String[] items = StringUtils.split(path, "/");
            List<String> methods = new ArrayList<String>();
            for (String item : items) {
                if (StringUtils.isNotBlank(item)) {
                    methods.add(item);
                }
            }
            if (methods.size() == 2) {
                String key = methods.get(0);
                String method = methods.get(1);
                ApiTarget apiTarget = ClassLoaderUtil.getApiObject(key);
                if (apiTarget != null) {
                    //valid user auth.
                    boolean next = true;

                    if(apiTarget.isAuth()){
                        if(request.getSession().getAttribute(HttpContext.SESSION_CURRENT_USER)==null){
                            ApiResult r = new ApiResult();
                            r.setSuccess(false);
                            r.setMessage("UnAuth");
                            r.setData("");
                            pw.println(Utils.encode(r));
                            next = false;
                        }
                    }
                    if(next){
                        try {
                            FunctionTarget functionTarget = ClassLoaderUtil.getApiFunction(String.format("%s.%s",key,method));
                            if(functionTarget!=null){
                                if(functionTarget.isAuth()){
                                    if(request.getSession().getAttribute(HttpContext.SESSION_CURRENT_USER)==null){
                                        ApiResult r = new ApiResult();
                                        r.setSuccess(false);
                                        r.setMessage("UnAuth");
                                        r.setData("");
                                        pw.println(Utils.encode(r));
                                        next = false;
                                    }
                                }
                                if(next){
                                    HttpContext context = new HttpContext();
                                    context.request = request;
                                    context.response = response;
                                    Object result = functionTarget.getMethod().invoke(apiTarget.getInstance(), context);
                                    pw.println(Utils.encode(result));
                                }

                            }
                        }catch (InvocationTargetException e) {
                            Log.warning("InvocationTargetException");
                            ErrorMessage(pw);
                        } catch (IllegalAccessException e) {
                            Log.warning("IllegalAccessException");
                            ErrorMessage(pw);
                        }
                    }

                } else {
                    ErrorMessage(pw);
                }
            } else {
                ErrorMessage(pw);
            }
        } else {
            ErrorMessage(pw);
        }
        pw.close();
    }

    private static void ErrorMessage(PrintWriter pw) {
        ApiResult r = new ApiResult();
        r.setSuccess(false);
        r.setMessage("Error");
        r.setData("");
        pw.println(Utils.encode(r));
    }
}