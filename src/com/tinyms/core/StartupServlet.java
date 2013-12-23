package com.tinyms.core;

import com.tinyms.app.Hospital;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import org.apache.commons.lang3.StringUtils;

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
        String path = config.getServletContext().getRealPath("/");
        if(!StringUtils.endsWith(path,"/")){
            path = path + "/";
        }
        HttpContext.realpath = path;
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
