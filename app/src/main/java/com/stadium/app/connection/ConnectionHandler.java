package com.stadium.app.connection;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.stadium.app.utils.Utils;
import com.google.gson.Gson;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import java.io.File;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;

public class ConnectionHandler<T> {

    private static final String LOG_TAG = "ConnectionHandler";
    private int timeout = 45 * 1000; // Ion's default timeout is 30 seconds.
    private Context context;
    private String url;
    private Class<?> cls;
    private ConnectionListener<T> listener;
    private String tag = ""; // default value to avoid null pointer exception
    private Map<String, List<String>> params; //Ion accepts parameters as a map of key value pair of String and List<String>
    private Map<String, File> files;

    private Future<String> future;

    private long startTime, finishTime;


    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener) {
        init(context, url, cls, listener, "");
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, String tag) {
        init(context, url, cls, listener, tag);
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, Map<String, String> params) {
        this(context, url, cls, listener, params, "");
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, Map<String, String> params, String tag) {
        this(context, url, cls, listener, tag);
        if (params != null) {
            this.params = new HashMap<>();
            for (String key : params.keySet()) {
                this.params.put(key, Collections.singletonList(params.get(key)));
            }
        }
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, Map<String, String> params, Map<String, File> files) {
        this(context, url, cls, listener, null, params, files);
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, String tag, Map<String, String> params, Map<String, File> files) {
        this(context, url, cls, listener, params, tag);
        this.files = files;
    }

    private void init(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, String tag) {
        this.context = context;
        this.url = url;
        this.cls = cls;
        this.listener = listener;
        if (Utils.isNullOrEmpty(tag)) {
            this.tag = url;
        } else {
            this.tag = tag;
        }
    }


    private void printLogs(int level) {
        switch (level) {
            case 0:
                startTime = System.currentTimeMillis();
                Log.e(LOG_TAG,  url + "\nrequest started. time=" + Calendar.getInstance().getTime());
                break;
            case 1:
                finishTime = System.currentTimeMillis();
                Log.e(LOG_TAG, url + "\nrequest finished and parsing started. time=" + Calendar.getInstance().getTime() + ", Time diff: " + (finishTime - startTime) + " MS");
                break;

        }
    }

    /**
     * Execute get request. (requires url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<String> executeGet() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            printLogs(0);  // request started

            future = Ion.with(context)
                    .load(url)
                    .setTimeout(timeout)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute x-www-form-urlencoded post request with string parameters.
     * (requires at least url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<String> executePost() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(context)
                    .load("POST", url)
                    .setTimeout(timeout);

            if (params != null) {
                for (String key : params.keySet()) {
                    Log.e(LOG_TAG, "Request Parameter: " + key + "=" + params.get(key));
                }
                ionBuilder.setBodyParameters(params);
            }

            future = ionBuilder.asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute a multipart post request with text and image parameters.
     * (requires at least baseUrl & url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<String> executeMultiPart() {
        if (url == null || (params == null && files == null)) {
            throw new IllegalArgumentException("No url, params or files found.");
        } else {
            printLogs(0);  // request started

            Builders.Any.B ionBuilder =
                    Ion.with(context)
                            .load("POST", url)
                            .setTimeout(timeout);

            if (params != null) {
                for (String key : params.keySet()) {
                    Log.e(LOG_TAG, "Request Parameter: " + key + "=" + params.get(key).get(0));
                }

                ionBuilder.setMultipartParameters(params);
            }

            for (String key : files.keySet()) {
                Log.e(LOG_TAG, "Multipart Parameter: " + key + "=" + files.get(key).getAbsolutePath());
                ionBuilder.setMultipartFile(key, "image/jpeg", files.get(key));
            }

            future = ionBuilder.asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Handles the completion of the request if (success or fail) and deserialize the string response using the given Class object and executes the  onFail or onSuccess method.
     *
     * @param e      the exception object (may be null if request failed).
     * @param result the string response.
     */
    @SuppressWarnings("unchecked")
    private void handleOnCompleted(Exception e, String result) {
        printLogs(1);  // request finished

        if (e != null) { //on request failure
            e.printStackTrace();
            if (!(e instanceof CancellationException))
                if (listener != null) {
                    listener.onFail(e, tag);
                }
        } else if (result != null) {
            Log.e(LOG_TAG, "Response: " + result);

            if (cls == null && listener != null) { //T must be of type: Object or String
                listener.onSuccess((T) result, tag);
            } else if (listener != null) {
                try {
                    listener.onSuccess((T) new Gson().fromJson(result, cls), tag);
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Parsing Exception: " + ex.toString());
                    listener.onFail(ex, tag);
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Cancels the request.
     *
     * @param interruptThread true if the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     * @return false if the task could not be cancelled, typically because it has already completed normally; true otherwise.
     */
    public boolean cancel(boolean interruptThread) {
        return !(future.isCancelled() || future.isDone()) && future.cancel(interruptThread);
    }

    /**
     * method, used to set the time out of the future requests
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
