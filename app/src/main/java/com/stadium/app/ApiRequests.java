package com.stadium.app;

import android.content.Context;

import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.models.bodies.ForgetPasswordBody;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.responses.ParentResponse;
import com.stadium.app.utils.AppUtils;

/**
 * Created by Shamyyoun on 5/31/16.
 */
public class ApiRequests {
    public static ConnectionHandler<User> login(Context context, ConnectionListener<User> listener, String phone, String password) {
        // create the request body
        User body = new User();
        body.setPhone(phone);
        body.setPassword(password);

        // create & execute the request
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_LOGIN), User.class, listener, body, Const.API_LOGIN);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ParentResponse> checkEmail(Context context, ConnectionListener<ParentResponse> listener, String phone) {
        // create the request body
        User body = new User();
        body.setPhone(phone);

        // create & execute the request
        ConnectionHandler<ParentResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_CHECK_EMAIL), ParentResponse.class, listener, body, Const.API_CHECK_EMAIL);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ParentResponse> forgetPassword(Context context, ConnectionListener<ParentResponse> listener,
                                                                   int resetType, String phone) {
        // create the request body
        ForgetPasswordBody body = new ForgetPasswordBody();
        body.setRestType(resetType);
        User user = new User();
        user.setPhone(phone);
        body.setUser(user);

        // create & execute the request
        ConnectionHandler<ParentResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_FORGET_PASSWORD), ParentResponse.class, listener, body, Const.API_FORGET_PASSWORD);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }
}
