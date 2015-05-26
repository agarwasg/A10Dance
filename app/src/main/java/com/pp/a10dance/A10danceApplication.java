package com.pp.a10dance;

import android.app.Application;
import android.content.Context;

import com.couchbase.lite.Manager;

/**
 * Created by saketagarwal on 4/4/15.
 */
public class A10danceApplication extends Application {
    private static final String TAG = "A10denceApplication";

    private Manager mManager;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getA10denceAppplicationInstance() {
        return mContext;
    }

}
