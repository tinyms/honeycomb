package com.tinyms.data;

import org.hibernate.Session;

/**
 * Created by tinyms on 13-12-27.
 */
public interface ISession {
    public Object doInSession(Session session);
}
