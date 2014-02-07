package com.tinyms.app;

import com.tinyms.data.AccountHelper;
import com.tinyms.data.Database;
import com.tinyms.data.Orm;
import com.tinyms.entity.Account;
import tornadoj.points.IServerStartup;

import javax.servlet.http.HttpServlet;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-25.
 */
public class HospitalServerStartup implements IServerStartup {
    private static Logger Log = Logger.getAnonymousLogger();

    @Override
    public void doInStartup(HttpServlet servlet) {
        Hospital.create_indexer();
        Orm.self().close();
        Database.init();
        AccountHelper.createRoot();
    }
}
