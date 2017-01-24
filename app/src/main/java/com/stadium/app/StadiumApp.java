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

        /*
        // init Parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .server(Const.PARSE_SERVER_URL)
                .applicationId(Const.PARSE_APP_ID)
                .clientKey(Const.PARSE_CLIENT_KEY)
                .build());

        ParseUser.logInInBackground(Const.PARSE_USERNAME, Const.PARSE_PASSWORD, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Utils.logE("Parse user login successful");

                    // install
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
//                    installation.put("UserId", "119");
                    installation.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Utils.logE("Parse installation is successful");
                            } else {
                                Utils.logE("Parse installation failed");
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Utils.logE("Parse user login failed");
                    e.printStackTrace();
                }
            }
        });
        */
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
}
