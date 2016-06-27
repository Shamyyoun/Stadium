package com.stadium.player;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.FacebookSdk;
import com.stadium.player.utils.Utils;

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

        // init Facebook sdk
        FacebookSdk.sdkInitialize(this);

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
}
