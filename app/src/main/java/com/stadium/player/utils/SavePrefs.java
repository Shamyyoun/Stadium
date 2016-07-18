package com.stadium.player.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

/**
 * A utility class that saves serialized objects (using Gson) in the SharedPreferences.
 * @param <T> the type of the object to save.
 */
public class SavePrefs<T> {
    private Class<?> cls;
    private SharedPreferences prefs;

    public SavePrefs(Context context, Class<?> cls) {
        this.cls = cls;
        String USER_PREFS_FILE_NAME = cls.getName();
        prefs = context.getSharedPreferences(USER_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void save(T obj, String key) {
        Editor editor = prefs.edit();
        String data = new Gson().toJson(obj);
        editor.putString(key, data);
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public T load(String key) {
        String data = prefs.getString(key, "");
        if (data.isEmpty()) {
            return null;
        }
        return (T) new Gson().fromJson(data, cls);
    }

    //clear all preferences file with the name cls.getTitle()
    public void clear() {
        prefs.edit().clear().apply();
    }


    //removes a key from the preferences
    public void remove(String key){
        prefs.edit().remove(key).apply();
    }

}
