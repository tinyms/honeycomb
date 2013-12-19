package com.tinyms.core;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbutils.QueryRunner;

/**
 * Created by tinyms on 13-12-18.
 * http://commons.apache.org/proper/commons-dbutils/examples.html
 *
 */
public class Database {
    private static QueryRunner queryRunner = null;
    private static ComboPooledDataSource ds = new ComboPooledDataSource();
    public static QueryRunner self(){
        return queryRunner;
    }

    public static void init(){
        if(queryRunner==null){
            queryRunner = new QueryRunner(ds);
        }
    }
}
