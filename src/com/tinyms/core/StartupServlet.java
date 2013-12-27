package com.tinyms.core;

import com.tinyms.data.AccountHelper;
import com.tinyms.point.IServerStartup;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-18.
 */
@WebServlet(name = "StartupServlet", loadOnStartup = 1, urlPatterns = "/StartupServlet")
public class StartupServlet extends HttpServlet {
    private static Logger Log = Logger.getAnonymousLogger();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Log.info("Honeycomb initing.");
        String path = config.getServletContext().getRealPath("/");
        if (!StringUtils.endsWith(path, "/")) {
            path = path + "/";
        }
        com.tinyms.core.Configuration.PluginPackageNames.clear();
        com.tinyms.core.Configuration.WebAbsPath = path;
        Configuration cfg = HttpContext.getFreemarkerConfiguration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(path + "WEB-INF/templates"));
            cfg.setObjectWrapper(new DefaultObjectWrapper());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassLoaderUtil.loadPlugins("com.tinyms");
        Orm.self().close();
        Database.init();
        AccountHelper.createRoot();
        //custom web server startup
        List<IServerStartup> iServerStartups = ClassLoaderUtil.getPlugin(IServerStartup.class);
        for (IServerStartup iServerStartup : iServerStartups) {
            iServerStartup.doInStartup(this);
        }
        for (String packageName : com.tinyms.core.Configuration.PluginPackageNames) {
            ClassLoaderUtil.loadPlugins(packageName);
        }
        Log.info("Honeycomb init completed.");
    }

    @Override
    public void destroy() {
        super.destroy();
        Utils.getCacheManager().shutdown();
    }
}
