package com.tinyms.core;

import com.tinyms.app.Hospital;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Created by tinyms on 13-12-18.
 */
@WebServlet(name = "ArchXStartupServlet", loadOnStartup = 1, urlPatterns = "/ArchXStartupServlet")
public class StartupServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ClassLoaderUtil.loadAnnotationPresentObjects();
        Database.init();
        Hospital.create_indexer();
    }
}
