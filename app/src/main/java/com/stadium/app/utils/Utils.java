package com.stadium.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.stadium.app.Const;

import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shamyyoun on 18/12/15.
 * A class, with general purpose utility methods (useful for many projects).
 */
public class Utils {
    public static final boolean DEBUGGABLE = true;
    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_FACEBOOK_MESSENGER = "com.facebook.orca";
    public static final String PACKAGE_GOOGLE_PLUS = "com.google.android.apps.plus";
    public static final String PACKAGE_WHATSAPP = "com.whatsapp";
    private static final String KEY_APP_VERSION_CODE = "app_version_code_key";

    /**
     * get the hash key
     */
    public static String getHashKey(Context context) {
        String hashKey = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {
            logE("Failed to get hash key");
        }

        return hashKey;
    }


    /**
     * Validate email address.
     *
     * @param email the email to validate
     * @return true if valid email or false.
     */
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email)) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    /**
     * Checks the given number as a valid Egyptian mobile number.
     *
     * @param number the number to check
     * @return true if valid Egyptian mobile number.
     */
    public static boolean isValidEgyptianMobileNumber(String number) {
        if (number.length() != 11) {
            return false;
        } else if (!number.startsWith("01")) {
            return false;
        } else {
            char operatorDifferentiator = number.charAt(2);

            if (operatorDifferentiator != '0' && operatorDifferentiator != '1' && operatorDifferentiator != '2') {
                return false;
            } else {
                try {
                    long l = Long.parseLong(number);
                } catch (NumberFormatException ex) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Get a shared preferences file named Const.SHARED_PREFERENCES_FILE_NAME, keys added to it must be unique
     *
     * @param ctx
     * @return the shared preferences
     */
    public static SharedPreferences getSharedPreferences(Context ctx) {
        return ctx.getSharedPreferences(Const.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void cacheBoolean(Context ctx, String k, Boolean v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putBoolean(k, v).apply();
    }

    public static Boolean getCachedBoolean(Context ctx, String k, Boolean defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getBoolean(k, defaultValue);
    }

    public static void cacheString(Context ctx, String k, String v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putString(k, v).apply();
    }

    public static String getCachedString(Context ctx, String k, String defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getString(k, defaultValue);
    }

    public static void cacheInt(Context ctx, String k, int v) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        prefs.edit().putInt(k, v).apply();
    }

    public static int getCachedInt(Context ctx, String k, int defaultValue) {
        SharedPreferences prefs = getSharedPreferences(ctx);
        return prefs.getInt(k, defaultValue);
    }

    public static void clearCachedKey(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).apply();
    }

    /**
     * Checks the device has a connection (not necessarily connected to the internet)
     *
     * @param ctx
     * @return
     */
    public static boolean hasConnection(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Gets a formatted % percent from numerator/denominator values.
     *
     * @param numerator
     * @param denominator
     * @param locale      the locale of the returned string.
     * @return the formatted percent.
     */
    public static String getPercent(int numerator, int denominator, Locale locale) {
        //http://docs.oracle.com/javase/tutorial/i18n/format/decimalFormat.html
        NumberFormat nf = NumberFormat.getNumberInstance(locale);  //Locale.US, .....
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("###.#");
        if (denominator == 0) {
            return df.format(0) + "%";
        }
        float percent = (numerator / (float) denominator) * 100;
        return df.format(percent) + "%";
    }


    /**
     * hide keyboard in edit text field
     */
    public static void hideKeyboard(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * hide keyboard in activity
     */
    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * hide keyboard in fragment
     */
    public static void hideKeyboard(Fragment fragment) {
        fragment.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    /**
     * show key board in edit text field
     */
    public void showKeyboard(Activity activity, EditText et) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInputFromInputMethod(et.getWindowToken(), 0);
    }


    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    public static void showShortToast(Context context, int textID) {
        Toast.makeText(context, textID, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static void showLongToast(Context context, int textID) {
        Toast.makeText(context, textID, Toast.LENGTH_LONG).show();
    }

    public static void logE(String msg) {
        if (DEBUGGABLE) Log.e(Const.LOG_TAG, msg == null ? "null" : msg);
    }


    /**
     * Executes the given AsyncTask Efficiently.
     *
     * @param task the task to execute.
     */
    public static void executeAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    /**
     * Remove '-' and other characters from mobile numbers and replace any + with 00
     *
     * @param oldNumber
     * @return the enhanced number
     */
    public static String enhanceMobileNumber(String oldNumber) {
        return oldNumber.replaceAll("[()\\s-]", "").replace("+", "00");
    }

    /**
     * Checks to see if EditText contains whitespace or no text
     *
     * @param et the EditText
     * @return true if EditText contains whitespace or no text otherwise false
     */
    public static boolean isEmpty(EditText et) {
        return TextUtils.isEmpty(et.getText().toString().trim());
    }

    /**
     * Checks to see if text is null or contains whitespace or no content
     *
     * @param charSequence
     * @return true if text contains whitespace or no content otherwise false
     */
    public static boolean isEmpty(CharSequence charSequence) {
        if (charSequence == null) return true;
        return TextUtils.isEmpty(charSequence.toString().trim());
    }

    /**
     * Get the EditText text trimmed
     *
     * @param et
     * @return the EditText text trimmed
     */
    public static String getText(EditText et) {
        return et.getText().toString().trim();
    }

    /**
     * converts the given timestamp to  readable date.
     *
     * @param timeStamp
     * @return
     */
    public static String timeStampToString(long timeStamp, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        return dateFormat.format(new Date(timeStamp));
    }


    /**
     * Gets the app version code.
     *
     * @param context
     * @return the version code.
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void cacheAppVersionCode(Context context) {
        cacheInt(context, KEY_APP_VERSION_CODE, getAppVersionCode(context));
    }


    public static int getCachedAppVersionCode(Context context) {
        return getCachedInt(context, KEY_APP_VERSION_CODE, Integer.MIN_VALUE);
    }

    /**
     * Get the application version name
     *
     * @param context
     * @return The app version name
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Gets the current app locale language like: ar, en, ....
     *
     * @return the current app locale language like: ar, en, ....
     */
    public static String getAppLanguage() {
        return Locale.getDefault().getLanguage().toLowerCase().substring(0, 2);
    }

    /**
     * Set the app language
     *
     * @param ctx  the application context
     * @param lang the language as ar, en, .....
     */
    public static void changeAppLocale(Context ctx, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
    }

    /**
     * method, used to check if gps  is enabled on the device or not
     *
     * @param context
     * @return boolean variable
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        } else {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
    }

    /**
     * method, used to format a double number as string without x.0
     *
     * @param number
     * @return the formatted double as string
     */
    public static String formatDouble(double number) {
        if (number == (long) number)
            return String.format("%d", (long) number);
        else
            return String.format("%s", number);
    }

    /**
     * Checks if a specified service is running or not.
     *
     * @param ctx          the context
     * @param serviceClass the class of the service
     * @return true if the service is running otherwise false
     */
    public static boolean isServiceRunning(Context ctx, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * method, used to format the url to prevent app from crash when open browser intent
     *
     * @param url
     * @return the formatted url
     */
    public static String formatUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }

    /**
     * method, used to check if string is null or empty
     *
     * @param str to check
     * @return boolean true if null or empty
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * method, used to check if list is null or empty
     *
     * @param list to check
     * @return boolean true if null or empty
     */
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }


    /**
     * method, used to prepare the url and open it in the browser
     *
     * @param context
     * @param url
     */
    public static void openBrowser(Context context, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * method, used to check if app is installed in the device or not
     *
     * @param context
     * @param appPackageName
     * @return
     */
    public static boolean isAppInstalledAndEnabled(Context context, String appPackageName) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(appPackageName, PackageManager.GET_ACTIVITIES);
            boolean installed = true;
            boolean enabled = info.applicationInfo.enabled;

            return installed && enabled;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * method, used to share text to specific app
     *
     * @param context
     * @param appPackageName
     * @param text
     * @return
     */
    public static boolean shareTextToApp(Context context, String appPackageName, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.setPackage(appPackageName);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // app is not installed
            return false;
        }
    }

    /**
     * method, used to convert string number to double number
     *
     * @param number
     * @return
     */
    public static double convertToDouble(String number) {
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * method, used to load url into imageview using picasso and make required validation
     *
     * @param context
     * @param url
     * @param defImageId
     * @param imageView
     */
    public static void loadImage(Context context, String url, int defImageId, ImageView imageView) {
        loadImage(context, url, defImageId, imageView, null);
    }

    /**
     * method, used to load url into imageview using picasso and make required validation
     *
     * @param context
     * @param url
     * @param defImageId
     * @param imageView
     * @param callback
     */
    public static void loadImage(Context context, String url, int defImageId, ImageView imageView, Callback callback) {
        if (isNullOrEmpty(url)) {
            imageView.setImageResource(defImageId);
        } else {
            Picasso.with(context).load(url).placeholder(defImageId)
                    .error(defImageId).into(imageView, callback);
        }
    }

    /**
     * method, used to concatenate array of strings with the passed divider
     *
     * @param divider
     * @param strings
     * @return
     */
    public static String getFullString(String divider, String... strings) {
        String finalString = "";
        for (String str : strings) {
            if (!isNullOrEmpty(str)) {
                if (finalString.isEmpty()) {
                    finalString += str;
                } else {
                    finalString += divider + str;
                }
            }
        }

        return finalString;
    }

    /**
     * method, used to match the regex on the passed text and return true or false
     *
     * @param regex
     * @param text
     * @return
     */
    public static boolean matchRegex(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.matches();
    }

    /**
     * method, used to override a font in all the app
     *
     * @param context
     * @param staticFontName
     * @param fontAssetName
     */
    public static void overrideFont(Context context, String staticFontName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticFontName);
            staticField.setAccessible(true);
            staticField.set(null, regular);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}