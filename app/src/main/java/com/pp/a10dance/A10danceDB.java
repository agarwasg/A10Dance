package com.pp.a10dance;

import android.util.Log;

import com.couchbase.lite.Database;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.helper.LogUtils;

/**
 * Created by saketagarwal on 5/18/15.
 */
public class A10danceDB {

    private static final String DB_NAME = "a10dence_application_database";
    private static final String TAG = LogUtils.getTag(A10danceDB.class);

    private static A10danceDB cblInstance;
    private Manager manager;
    private Database database;

    private A10danceDB() {
    }

    public static synchronized A10danceDB getInstance(AndroidContext context) {
        if (cblInstance == null) {
            cblInstance = new A10danceDB();
            try {
                cblInstance.manager = new Manager(context,
                        Manager.DEFAULT_OPTIONS);
                cblInstance.database = cblInstance.manager.getDatabase(DB_NAME);
            } catch (Exception e) {
                Log.e("CBL", "error initializing database", e);
                e.printStackTrace();
                return null;

            }
        }
        return cblInstance;
    }

    public Database getDatabase() {
        return database;
    }

    public Manager getManager() {
        return manager;
    }
}
