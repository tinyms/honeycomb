package com.tinyms.point;

import javax.servlet.http.HttpServlet;

/**
 * Created by tinyms on 13-12-25.
 */

public interface IServerStartup {
    public void doInStartup(HttpServlet servlet);
}
