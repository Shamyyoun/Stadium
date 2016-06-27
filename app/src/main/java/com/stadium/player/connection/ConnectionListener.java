package com.stadium.player.connection;

public interface ConnectionListener<T> {
    void onSuccess(T response, String tag);
    void onFail(Exception ex, String tag);
}
