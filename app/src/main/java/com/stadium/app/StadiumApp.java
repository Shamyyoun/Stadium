package com.stadium.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.stadium.app.utils.Utils;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Shamyyoun on 4/23/16.
 */
public class StadiumApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // override default font
        Utils.overrideFont(this, "MONOSPACE", "app_font.ttf");

        // init Crashlytics disabled for debugging mode
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlytics, new Crashlytics());
    }

    /**
     * method used to return current application instance
     */
    public static StadiumApp getInstance(Context context) {
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
}
