package com.stadium.app;

import android.content.Context;

import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.models.bodies.LoginBody;
import com.stadium.app.models.entities.User;
import com.stadium.app.utils.AppUtils;

/**
 * Created by Shamyyoun on 5/31/16.
 */
public class ApiRequests {
    public static ConnectionHandler<User> login(Context context, ConnectionListener<User> listener, LoginBody body) {
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_LOGIN), User.class, listener, body);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }
}
