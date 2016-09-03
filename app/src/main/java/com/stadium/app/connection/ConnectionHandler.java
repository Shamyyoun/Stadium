package com.stadium.app.connection;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders;
import com.stadium.app.utils.Utils;

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
    private Object body;

    private Future<Response<String>> future;
    private long startTime, finishTime;
    private Gson gson;


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

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, Object body) {
        this(context, url, cls, listener, body, "");
    }

    public ConnectionHandler(Context context, String url, @Nullable Class<?> cls, ConnectionListener<T> listener, Object body, String tag) {
        this(context, url, cls, listener, tag);
        this.body = body;
        gson = new Gson();
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
        String tag;
        if (this.tag != null) {
            tag = "[" + this.tag + "] ";
        } else {
            tag = "[" + url + "]\n";
        }

        switch (level) {
            case 0:
                startTime = System.currentTimeMillis();
                Log.e(LOG_TAG, tag + "request started. time=" + Calendar.getInstance().getTime());
                break;
            case 1:
                finishTime = System.currentTimeMillis();
                Log.e(LOG_TAG, tag + "request finished and parsing started. time=" + Calendar.getInstance().getTime() + ", Time diff: " + (finishTime - startTime) + " MS");
                break;

        }
    }

    /**
     * Execute get request. (requires url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeGet() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            printLogs(0);  // request started

            future = Ion.with(context)
                    .load(url)
                    .setTimeout(timeout)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
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
    public Future<Response<String>> executePost() {
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
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
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
    public Future<Response<String>> executeMultiPart() {
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
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            handleOnCompleted(e, result);
                        }
                    });

            return future;
        }
    }

    /**
     * Execute raw post request with json parameters.
     * (requires at least url)
     *
     * @return Future object for cancelling the request.
     */
    public Future<Response<String>> executeRawJson() {
        if (url == null) {
            throw new IllegalArgumentException("No url found.");
        } else {
            printLogs(0);

            Builders.Any.B ionBuilder = Ion.with(context)
                    .load(url)
                    .setTimeout(timeout)
                    .addHeader("content-type", "application/json")
                    .addHeader("Accept-Language", "ar");

            if (body != null) {
                String bodyJson = gson.toJson(body);
                Log.e(LOG_TAG, "Request Body: " + bodyJson);
                ionBuilder.setJsonPojoBody(body);
            }

            future = ionBuilder.asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
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
    private void handleOnCompleted(Exception e, Response<String> result) {
        printLogs(1);  // request finished

        // prepare the result code string
        int statusCode = 0;
        if (result != null && result.getHeaders() != null) {
            statusCode = result.getHeaders().code();
        }

        if (e != null) { //on request failure
            Log.e(LOG_TAG, "Error(" + statusCode + "): " + e.getMessage());
            if (!(e instanceof CancellationException))
                if (listener != null) {
                    listener.onFail(e, statusCode, tag);
                }
        } else if (result.getResult() != null) {
            Log.e(LOG_TAG, "Response(" + statusCode + "): " + result.getResult());

            if (cls == null && listener != null) { //T must be of type: Object or String
                listener.onSuccess((T) result.getResult(), statusCode, tag);
            } else if (listener != null) {
                try {
                    listener.onSuccess((T) new Gson().fromJson(result.getResult(), cls), statusCode, tag);
                } catch (Exception ex) {
                    Log.e(LOG_TAG, "Error(" + statusCode + "): " + ex.getMessage());
                    listener.onFail(ex, statusCode, tag);
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
