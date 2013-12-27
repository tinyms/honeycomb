package com.tinyms.data;

import com.tinyms.core.ISession;
import com.tinyms.core.Orm;
import com.tinyms.core.Utils;
import com.tinyms.entity.Account;
import org.hibernate.Session;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-27.
 */
public class AccountHelper {
    private final static Logger Log = Logger.getAnonymousLogger();
    public static void createRoot(){
        Account root = new Account();
        root.setLoginId("admin");
        root.setLoginPwd(Utils.md5("admin"));
        root.setEmail("admin@domain.com");
        root.setCreateTime(Utils.current_timestamp());
        root.setStatus(1);
        root.setName("Administrator");
        create(root);
    }

    public static long login(String loginId,String loginPwd){
        Long id = (Long)Orm.self()
                .createQuery("select id from Account where loginId=:id and loginPwd=:pwd and status=1")
                .setParameter("id",loginId).setParameter("pwd",Utils.md5(loginPwd))
                .setMaxResults(1).uniqueResult();
        if(id==null){
            return 0;
        }
        return id.longValue();
    }

    public static Object col(String colName,long id){
        return Orm.self()
                .createQuery("select "+colName+" from Account where id=:id").setParameter("id",id)
                .setMaxResults(1).uniqueResult();
    }

    public static long create(final Account user){
        Long id = (Long)Orm.persist(new ISession() {
            @Override
            public Object doInSession(Session session) {
                session.save(user);
                return user.getId();
            }
        });
        return id.longValue();
    }

    public static boolean update(final Account user){
        Boolean b = (Boolean)Orm.persist(new ISession() {
            @Override
            public Object doInSession(Session session) {
                session.update(user);
                return Boolean.TRUE;
            }
        });
        return b.booleanValue();
    }

    public static boolean del(long id){
        int num = Orm.self().createQuery("delete from Account where id = :id").setParameter("id",id).executeUpdate();
        if(num>0){
            return true;
        }
        return false;
    }

    public static boolean exists(String LoginID){
        Long num = (Long)Orm.self().createQuery("select count(id) from Account where loginId = :loginId")
                .setParameter("loginId", LoginID).uniqueResult();
        if(num.longValue()>0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param page start 1
     * @param limit
     * @return
     */
    public static List list(int page, int limit){
        return Orm.self().createQuery("select a from Account a").setFetchSize((page-1)*limit).setMaxResults(limit).list();
    }

    public static void main(String[] args){
        Utils.log(col("email",1));
    }
}
