package com.tinyms.data;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

/**
 * Created by tinyms on 13-12-27.
 */
public class Orm {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;
    private static ComboPooledDataSource c3p0DataSource = new ComboPooledDataSource();

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();
            Properties prop = configuration.getProperties();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(prop).buildServiceRegistry();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
            //for jdbc c3p0 datasource
            c3p0DataSource.setJdbcUrl(prop.getProperty("connection.url"));
            c3p0DataSource.setDriverClass(prop.getProperty("connection.driver_class"));
            c3p0DataSource.setUser(prop.getProperty("connection.username"));
            c3p0DataSource.setPassword(prop.getProperty("connection.password"));
            c3p0DataSource.setMaxPoolSize(Integer.parseInt(prop.getProperty("c3p0.max_size")));
            c3p0DataSource.setMinPoolSize(Integer.parseInt(prop.getProperty("c3p0.min_size")));
            c3p0DataSource.setMaxIdleTime(Integer.parseInt(prop.getProperty("c3p0.timeout")));
            c3p0DataSource.setAcquireIncrement(Integer.parseInt(prop.getProperty("c3p0.acquire_increment")));
            c3p0DataSource.setMaxStatements(Integer.parseInt(prop.getProperty("c3p0.max_statements")));
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static DataSource getDataSource() {
        return c3p0DataSource;
    }

    public static Connection getConnection() {
        return ((SessionImpl) ourSessionFactory.openSession()).connection();
    }

    public static Session self() {
        return ourSessionFactory.openSession();
    }

    public static Object persist(ISession iSession) {
        Session ss = self();
        Object result = null;
        Transaction tx = null;
        try {
            tx = ss.beginTransaction();
            result = iSession.doInSession(ss);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            ss.close();
        }
        return result;
    }

    public static void main(final String[] args) throws Exception {
        final Session session = self();
        try {
            System.out.println("querying all the managed entities...");
            final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
            for (Object key : metadataMap.keySet()) {
                final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
                final String entityName = classMetadata.getEntityName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }
    }
}
