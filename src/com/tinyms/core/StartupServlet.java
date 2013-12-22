package com.tinyms.core;

import com.tinyms.app.Hospital;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;

/**
 * Created by tinyms on 13-12-18.
 */
@WebServlet(name = "ArchXStartupServlet", loadOnStartup = 1, urlPatterns = "/ArchXStartupServlet")
public class StartupServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        HttpContext.realpath = config.getServletContext().getRealPath("/");
        Configuration cfg = HttpContext.getFreemarkerConfiguration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(HttpContext.realpath+"WEB-INF/templates"));
            cfg.setObjectWrapper(new DefaultObjectWrapper());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassLoaderUtil.loadAnnotationPresentObjects();
        Database.init();
        Hospital.create_indexer();
    }
}
