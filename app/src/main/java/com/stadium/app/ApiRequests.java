package com.stadium.app;

import android.content.Context;

import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.models.bodies.ForgetPasswordBody;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.User;
import com.stadium.app.models.responses.ServerResponse;
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

    public static ConnectionHandler<ServerResponse> checkEmail(Context context, ConnectionListener<ServerResponse> listener, String phone) {
        // create the request body
        User body = new User();
        body.setPhone(phone);

        // create & execute the request
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_CHECK_EMAIL), ServerResponse.class, listener, body, Const.API_CHECK_EMAIL);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> forgetPassword(Context context, ConnectionListener<ServerResponse> listener,
                                                                   int resetType, String phone) {
        // create the request body
        ForgetPasswordBody body = new ForgetPasswordBody();
        body.setRestType(resetType);
        User user = new User();
        user.setPhone(phone);
        body.setUser(user);

        // create & execute the request
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_FORGET_PASSWORD), ServerResponse.class, listener, body, Const.API_FORGET_PASSWORD);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<City[]> getCities(Context context, ConnectionListener<City[]> listener) {
        // create & execute the request
        ConnectionHandler<City[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_GET_CITIES), City[].class, listener, Const.API_GET_CITIES);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User> createUser(Context context, ConnectionListener<User> listener,
                                                     String name, int age, City city,
                                                     String phone, String password, int userType) {
        // create the request body
        User body = new User();
        body.setName(name);
        body.setAge(age);
        body.setCity(city);
        body.setPhone(phone);
        body.setPassword(password);
        body.setTypeID(userType);

        // create & execute the request
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getApiUrl(Const.API_CREATE_USER), User.class, listener, body, Const.API_CREATE_USER);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }
}
