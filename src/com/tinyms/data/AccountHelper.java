package com.tinyms.data;

import com.tinyms.entity.Account;
import com.tinyms.entity.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import tornadoj.web.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-27.
 */
public class AccountHelper {
    private final static Logger Log = Logger.getAnonymousLogger();

    public static void createRoot() {
        if(!exists("admin")){
            User root = new User();
            root.setLoginId("admin");
            root.setLoginPwd(Utils.md5("admin"));
            root.setEmail("admin@domain.com");
            root.setCreateTime(Utils.current_timestamp());
            root.setStatus(1);
            root.setRole("super");
            create(root);
        }
    }

    public static long login(String loginId, String loginPwd) {
        Long id = (Long) Orm.self()
                .createQuery("select id from User where loginId=:id and loginPwd=:pwd and status=1")
                .setParameter("id", loginId).setParameter("pwd", Utils.md5(loginPwd)).uniqueResult();
        if (id == null) {
            return 0;
        }
        return id.longValue();
    }

    public static Object col(String colName, long id) {
        return Orm.self()
                .createQuery("select " + colName + " from User where id=:id").setParameter("id", id).uniqueResult();
    }

    public static long create(final User user) {
        Long id = (Long) Orm.persist(new ISession() {
            @Override
            public Object doInSession(Session session) {
                session.save(user);
                return user.getId();
            }
        });
        return id.longValue();
    }

    public static boolean update(final User user) {
        Boolean b = (Boolean) Orm.persist(new ISession() {
            @Override
            public Object doInSession(Session session) {
                session.update(user);
                return Boolean.TRUE;
            }
        });
        return b.booleanValue();
    }

    public static boolean del(long id) {
        int num = Orm.self().createQuery("delete from User where id = :id").setParameter("id", id).executeUpdate();
        if (num > 0) {
            return true;
        }
        return false;
    }

    public static boolean exists(String LoginID) {
        Long num = (Long) Orm.self().createQuery("select count(id) from User where loginId = :loginId")
                .setParameter("loginId", LoginID).uniqueResult();
        if (num.longValue() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @param page  start 1
     * @param limit
     * @return
     */
    public static PaginationResult list(int page, int limit) {
        PaginationResult r = new PaginationResult();
        Long total = (Long) Orm.self().createQuery("select count(id) from User ").uniqueResult();
        r.setTotal(total.longValue());
        List result = Orm.self().createQuery("select a from User a")
                .setFetchSize((page - 1) * limit).setMaxResults(limit).list();
        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        for (Object row : result) {
            Account u = (Account) row;
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", u.getId());
            item.put("name", u.getName());
            item.put("loginId", u.getLoginId());
            item.put("status", u.getStatus());
            item.put("email", u.getEmail());
            item.put("createTime", DateFormatUtils.format(u.getCreateTime().getTime(), "yyyy-MM-dd hh:mm"));
            if (u.getLogonTime() != null) {
                item.put("logonTime", DateFormatUtils.format(u.getLogonTime().getTime(), "yyyy-MM-dd hh:mm"));
            } else {
                item.put("logonTime", "");
            }
            users.add(item);
        }
        r.setResult(users);
        return r;
    }
}
