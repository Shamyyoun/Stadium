package com.stormnology.stadium;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.stormnology.stadium.utils.Utils;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Shamyyoun on 4/23/16.
 */
public class StadiumApp extends Application {
    private Tracker tracker;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        // set the static context
        context = getApplicationContext();

        // override default font
        Utils.overrideFont(this, "MONOSPACE", "app_font.ttf");

        // init Crashlytics disabled for debugging mode
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlytics, new Crashlytics());

        // init Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .server(Const.PARSE_SERVER_URL)
                .applicationId(Const.PARSE_APP_ID)
                .clientKey(Const.PARSE_CLIENT_KEY)
                .build());

        // init google analytics
        getDefaultTracker();
    }

    /**
     * method used to return current application instance
     */
    public static synchronized StadiumApp getInstance(Context context) {
        return (StadiumApp) context.getApplicationContext();
    }

    /**
     * method, used to change the app local language
     *
     * @param lang
     */
    private void changeAppLanguage(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang.toLowerCase());
        res.updateConfiguration(conf, dm);
    }

    /**
     * static method, used to get the default analytics tracker
     *
     * @param context
     * @return
     */
    public static Tracker getDefaultTracker(Context context) {
        return getInstance(context).getDefaultTracker();
    }

    /**
     * method, used to get the default analytics tracker
     *
     * @return
     */
    synchronized private Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(Const.ANALYTICS_PROPERTY_ID);
            tracker.enableAutoActivityTracking(true);
        }
        return tracker;
    }

    /**
     * static method, used to get the static context from the application class
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }
}
