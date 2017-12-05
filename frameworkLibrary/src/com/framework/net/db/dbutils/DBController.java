package com.framework.net.db.dbutils;


import com.framework.net.db.utils.JniApi;

/**
 * Created by 2015-4-10
 * 
 * 
 *
 */
public class DBController {

    private static final int DB_VERSION =3;
    private static DatabaseHelper db = null;
    private static final String DB_NAME="cache.db";

    public static void createDB(){
        initialDB();
    }

    private static synchronized void initialDB(){
        if(db==null){
            db=new DatabaseHelper(JniApi.appcontext,DB_NAME, null, DB_VERSION);
        }
    }

    public static DatabaseHelper getDB() throws DBNotInitializeException {
        initialDB();
        if (db == null) {
            throw new DBNotInitializeException("DB not created.");
        }
        return db;
    }


    public static synchronized void destoryDB() {
        if (db != null) {
            db.close();
//			OpenHelperManager.releaseHelper();
            db = null;
            
        }
    }

    public static synchronized void clearAllData(Class<?> entity){
        if (db != null) {
            db.clearAllData(entity);
        }
    }
}
