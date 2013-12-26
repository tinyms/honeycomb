package com.tinyms.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by tinyms on 13-12-18.
 * http://commons.apache.org/proper/commons-dbutils/examples.html
 * http://tompig.iteye.com/blog/1450756
 */
public class Database {
    private Logger Log = Logger.getAnonymousLogger();
    private static QueryRunner queryRunner = null;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    public static QueryRunner self() {
        return queryRunner;
    }

    public static QueryRunner self(String dsName) {
        ComboPooledDataSource ds = new ComboPooledDataSource(dsName);
        return new QueryRunner(ds);
    }

    public static void init() {
        if (queryRunner == null) {
            queryRunner = new QueryRunner(ds);
        }
    }

    //便捷的方法
    public static int insert(String sql, Object[] params) {
        int affectedRows = 0;
        try {
            if (params == null) {
                affectedRows = self().update(sql);
            } else {
                affectedRows = self().update(sql, params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    /**
     * 插入数据库，返回自动增长的主键
     *
     * @param sql -
     *            执行的sql语句
     * @return 主键 注意；此方法没关闭资源
     */
    public static int insertForKeys(String sql, Object[] params) {
        int key = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = self().getDataSource().getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ParameterMetaData pmd = stmt.getParameterMetaData();
            if (params.length < pmd.getParameterCount()) {
                throw new SQLException("param num err:" + pmd.getParameterCount());
            }
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                key = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) { // 关闭记录集
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) { // 关闭声明
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) { // 关闭连接对象
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
    }

    public static long count(String sql, Object... params) {
        Number num = 0;
        try {
            if (params == null) {
                num = (Number) self().query(sql, new ScalarHandler() {
                    @Override
                    public Object handle(ResultSet rs) throws SQLException {
                        Object obj = super.handle(rs);
                        if (obj instanceof BigInteger)
                            return ((BigInteger) obj).longValue();
                        return obj;
                    }
                });
            } else {
                num = (Number) self().query(sql, new ScalarHandler() {
                    @Override
                    public Object handle(ResultSet rs) throws SQLException {
                        Object obj = super.handle(rs);
                        if (obj instanceof BigInteger)
                            return ((BigInteger) obj).longValue();
                        return obj;
                    }
                }, params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (num != null) ? num.longValue() : -1;
    }

    /**
     * 执行sql语句
     *
     * @param sql sql语句
     * @return 受影响的行数
     */
    public static int update(String sql) {
        return update(sql, null);
    }

    /**
     * 单条修改记录
     *
     * @param sql   sql语句
     * @param param 参数
     * @return 受影响的行数
     */
    public static int update(String sql, Object param) {
        return update(sql, new Object[]{param});
    }

    /**
     * 单条修改记录
     *
     * @param sql    sql语句
     * @param params 参数数组
     * @return 受影响的行数
     */
    public static int update(String sql, Object[] params) {
        int affectedRows = 0;
        try {
            if (params == null) {
                affectedRows = self().update(sql);
            } else {
                affectedRows = self().update(sql, params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    /**
     * 批量修改记录
     *
     * @param sql    sql语句
     * @param params 二维参数数组
     * @return 受影响的行数的数组
     */
    public static int[] batchUpdate(String sql, Object[][] params) {
        int[] affectedRows = new int[0];
        try {
            affectedRows = self().batch(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return affectedRows;
    }

    /**
     * 执行查询，将每行的结果保存到一个Map对象中，然后将所有Map对象保存到List中
     *
     * @param sql sql语句
     * @return 查询结果
     */
    public static List<Map<String, Object>> find(String sql) {
        return find(sql, null);
    }

    /**
     * 执行查询，将每行的结果保存到一个Map对象中，然后将所有Map对象保存到List中
     *
     * @param sql   sql语句
     * @param param 参数
     * @return 查询结果
     */
    public static List<Map<String, Object>> find(String sql, Object param) {
        return find(sql, new Object[]{param});
    }

    /**
     * 执行查询，将每行的结果保存到一个Map对象中，然后将所有Map对象保存到List中
     *
     * @param sql    sql语句
     * @param params 参数数组
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> findPage(String sql, int page, int count, Object... params) {
        sql = sql + " LIMIT ?,?";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (params == null) {
                list = (List<Map<String, Object>>) self().query(sql, new MapListHandler(), new Integer[]{page,
                        count});
            } else {
                list = (List<Map<String, Object>>) self().query(sql, new MapListHandler(), ArrayUtils.addAll(
                        params, new Integer[]{page, count}));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 执行查询，将每行的结果保存到一个Map对象中，然后将所有Map对象保存到List中
     *
     * @param sql    sql语句
     * @param params 参数数组
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> find(String sql, Object[] params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            if (params == null) {
                list = (List<Map<String, Object>>) self().query(sql, new MapListHandler());
            } else {
                list = (List<Map<String, Object>>) self().query(sql, new MapListHandler(), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 执行查询，将每行的结果保存到Bean中，然后将所有Bean保存到List中
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @return 查询结果
     */
    public static <T> List<T> find(Class<T> entityClass, String sql) {
        return find(entityClass, sql, null);
    }

    /**
     * 执行查询，将每行的结果保存到Bean中，然后将所有Bean保存到List中
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @param param       参数
     * @return 查询结果
     */
    public static <T> List<T> find(Class<T> entityClass, String sql, Object param) {
        return find(entityClass, sql, new Object[]{param});
    }

    /**
     * 执行查询，将每行的结果保存到Bean中，然后将所有Bean保存到List中
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @param params      参数数组
     * @return 查询结果
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> find(Class<T> entityClass, String sql, Object[] params) {
        List<T> list = new ArrayList<T>();
        try {
            if (params == null) {
                list = (List<T>) self().query(sql, new BeanListHandler(entityClass));
            } else {
                list = (List<T>) self().query(sql, new BeanListHandler(entityClass), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 查询出结果集中的第一条记录，并封装成对象
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @return 对象
     */
    public static <T> T findFirst(Class<T> entityClass, String sql) {
        return findFirst(entityClass, sql, null);
    }

    /**
     * 查询出结果集中的第一条记录，并封装成对象
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @param param       参数
     * @return 对象
     */
    public static <T> T findFirst(Class<T> entityClass, String sql, Object param) {
        return findFirst(entityClass, sql, new Object[]{param});
    }

    /**
     * 查询出结果集中的第一条记录，并封装成对象
     *
     * @param entityClass 类名
     * @param sql         sql语句
     * @param params      参数数组
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T findFirst(Class<T> entityClass, String sql, Object[] params) {
        Object object = null;
        try {
            if (params == null) {
                object = self().query(sql, new BeanHandler(entityClass));
            } else {
                object = self().query(sql, new BeanHandler(entityClass), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (T) object;
    }

    /**
     * 查询出结果集中的第一条记录，并封装成Map对象
     *
     * @param sql sql语句
     * @return 封装为Map的对象
     */
    public static Map<String, Object> findFirst(String sql) {
        return findFirst(sql, null);
    }

    /**
     * 查询出结果集中的第一条记录，并封装成Map对象
     *
     * @param sql   sql语句
     * @param param 参数
     * @return 封装为Map的对象
     */
    public static Map<String, Object> findFirst(String sql, Object param) {
        return findFirst(sql, new Object[]{param});
    }

    /**
     * 查询出结果集中的第一条记录，并封装成Map对象
     *
     * @param sql    sql语句
     * @param params 参数数组
     * @return 封装为Map的对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> findFirst(String sql, Object[] params) {
        Map<String, Object> map = null;
        try {
            if (params == null) {
                map = (Map<String, Object>) self().query(sql, new MapHandler());
            } else {
                map = (Map<String, Object>) self().query(sql, new MapHandler(), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql     sql语句
     * @param colName 列名
     * @return 结果对象
     */
    public static Object findBy(String sql, String colName) {
        return findBy(sql, colName, null);
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql        sql语句
     * @param columnName 列名
     * @param param      参数
     * @return 结果对象
     */
    public static Object findBy(String sql, String columnName, Object param) {
        return findBy(sql, columnName, new Object[]{param});
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql        sql语句
     * @param columnName 列名
     * @param params     参数数组
     * @return 结果对象
     */
    public static Object findBy(String sql, String columnName, Object[] params) {
        Object object = null;
        try {
            if (params == null) {
                object = self().query(sql, new ScalarHandler(columnName));
            } else {
                object = self().query(sql, new ScalarHandler(columnName), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql         sql语句
     * @param columnIndex 列索引
     * @return 结果对象
     */
    public static Object findBy(String sql, int columnIndex) {
        return findBy(sql, columnIndex, null);
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql         sql语句
     * @param columnIndex 列索引
     * @param param       参数
     * @return 结果对象
     */
    public static Object findBy(String sql, int columnIndex, Object param) {
        return findBy(sql, columnIndex, new Object[]{param});
    }

    /**
     * 查询某一条记录，并将指定列的数据转换为Object
     *
     * @param sql         sql语句
     * @param columnIndex 列索引
     * @param params      参数数组
     * @return 结果对象
     */
    public static Object findBy(String sql, int columnIndex, Object[] params) {
        Object object = null;
        try {
            if (params == null) {
                object = self().query(sql, new ScalarHandler(columnIndex));
            } else {
                object = self().query(sql, new ScalarHandler(columnIndex), params);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * @param <T>分页查询
     * @param beanClass
     * @param sql
     * @param page
     * @param pageSize
     * @param params
     * @return
     */
    public static <T> List<T> findPage(Class<T> beanClass, String sql, int page, int pageSize, Object... params) {
        if (page <= 1) {
            page = 0;
        }
        return query(beanClass, sql + " LIMIT ?,?", ArrayUtils.addAll(params, new Integer[]{page, pageSize}));
    }

    public static <T> List<T> query(Class<T> beanClass, String sql, Object... params) {
        try {
            return (List<T>) self().query(sql, isPrimitive(beanClass) ? columnListHandler : new BeanListHandler(
                    beanClass), params);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
        {
            add(Long.class);
            add(Integer.class);
            add(String.class);
            add(java.util.Date.class);
            add(java.sql.Date.class);
            add(java.sql.Timestamp.class);
        }
    };
    // 返回单一列时用到的handler
    private final static ColumnListHandler columnListHandler = new ColumnListHandler() {
        @Override
        protected Object handleRow(ResultSet rs) throws SQLException {
            Object obj = super.handleRow(rs);
            if (obj instanceof BigInteger)
                return ((BigInteger) obj).longValue();
            return obj;
        }

    };

    // 判断是否为原始类型
    private static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || PrimitiveClasses.contains(cls);
    }
    // map
}
