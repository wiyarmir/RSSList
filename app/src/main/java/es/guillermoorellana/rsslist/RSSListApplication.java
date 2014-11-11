package es.guillermoorellana.rsslist;

import android.app.Application;
import android.content.Context;

/**
 * Allows to fetch a static global Context
 *
 * Created by wiyarmir on 11/11/14.
 */
public class RSSListApplication extends Application{
    private static Context context;

    public void onCreate(){
        super.onCreate();
        RSSListApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return RSSListApplication.context;
    }
}
