package com.tinyms.web;

import com.tinyms.data.AccountHelper;
import com.tinyms.data.Database;
import com.tinyms.data.Orm;
import com.tinyms.point.IServerStartup;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateModelException;
import org.apache.commons.io.FileUtils;
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
        if (!StringUtils.endsWith(path, File.separator)) {
            path = path + File.separator;
        }
        HoneycombConfiguration.PluginPackageNames.clear();
        HoneycombConfiguration.WebAbsPath = path;
        HoneycombConfiguration.TemplatePath = path + "WEB-INF/templates/";
        try {
            String tplCachePath = String.format("%s%s", HoneycombConfiguration.TemplatePath, "cache");
            FileUtils.deleteDirectory(new File(tplCachePath));
            Utils.mkdirs(tplCachePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration cfg = HttpContext.getFreemarkerConfiguration();
        try {
            cfg.setDirectoryForTemplateLoading(new File(HoneycombConfiguration.TemplatePath));
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            cfg.setDefaultEncoding("utf-8");
            cfg.setDateFormat("yyyy-MM-dd");
            cfg.setDateTimeFormat("yyyy-MM-dd hh:mm:ss");
            cfg.setSharedVariable("SiteUrl", HoneycombConfiguration.SiteUrl);
            cfg.setSharedVariable("Version", HoneycombConfiguration.Ver);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateModelException e) {
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
        for (String packageName : HoneycombConfiguration.PluginPackageNames) {
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
