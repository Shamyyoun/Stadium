package com.stadium.app;

import android.content.Context;

import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.models.bodies.ForgetPasswordBody;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.Image;
import com.stadium.app.models.entities.Stadium;
import com.stadium.app.models.entities.Team;
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
                AppUtils.getUserApiUrl(Const.API_LOGIN), User.class, listener, body, Const.API_LOGIN);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> checkEmail(Context context, ConnectionListener<ServerResponse> listener, String phone) {
        // create the request body
        User body = new User();
        body.setPhone(phone);

        // create & execute the request
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHECK_EMAIL), ServerResponse.class, listener, body, Const.API_CHECK_EMAIL);
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
                AppUtils.getUserApiUrl(Const.API_FORGET_PASSWORD), ServerResponse.class, listener, body, Const.API_FORGET_PASSWORD);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<City[]> getCities(Context context, ConnectionListener<City[]> listener) {
        // create & execute the request
        ConnectionHandler<City[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_GET_CITIES), City[].class, listener, Const.API_GET_CITIES);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User> createUser(Context context, ConnectionListener<User> listener,
                                                     String name, int age, City city,
                                                     String phone, String password,
                                                     String encodedImage, int userType) {
        // create the request body
        User body = new User();
        body.setName(name);
        body.setAge(age);
        body.setCity(city);
        body.setPhone(phone);
        body.setPassword(password);
        body.setTypeID(userType);
        if (encodedImage != null) {
            Image image = new Image();
            image.setContentBase64(encodedImage);
            image.setName("");
            body.setUserImage(image);
        }

        // create & execute the request
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CREATE_USER), User.class, listener, body, Const.API_CREATE_USER);
        connectionHandler.setTimeout(60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium[]> listOfStadiums(Context context, ConnectionListener<Stadium[]> listener,
                                                              int userId, String userToken) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);

        // create & execute the request
        ConnectionHandler<Stadium[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_LIST_OF_STADIUMS), Stadium[].class, listener, body, Const.API_LIST_OF_STADIUMS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Event[]> getEvent(Context context, ConnectionListener<Event[]> listener, int userId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_GET_EVENT) + "/" + userId;

        // create & execute the request
        ConnectionHandler<Event[]> connectionHandler = new ConnectionHandler(context, url, Event[].class, listener, Const.API_GET_EVENT);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User> editProfile(Context context, ConnectionListener<User> listener,
                                                      int userId, String userToken,
                                                      int age, City city, String phone,
                                                      String position, String email, String bio) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);
        body.setAge(age);
        body.setCity(city);
        body.setPhone(phone);
        body.setPosition(position);
        body.setEmail(email);
        body.setBio(bio);

        // create & execute the request
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_EDIT_PROFILE), User.class, listener, body, Const.API_EDIT_PROFILE);
        connectionHandler.setTimeout(30 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team> createTeam(Context context, ConnectionListener<Team> listener,
                                                     int userId, String userToken,
                                                     String title, String desc,
                                                     int favStadiumId, String encodedImage) {
        // create the request body
        Team body = new Team();
        User captain = new User();
        captain.setId(userId);
        captain.setToken(userToken);
        body.setCaptain(captain);
        body.setName(title);
        body.setDescription(desc);
        body.setPreferStadiumId(favStadiumId);
        if (encodedImage != null) {
            Image image = new Image();
            image.setContentBase64(encodedImage);
            image.setName("");
            body.setTeamImage(image);
        }

        // create & execute the request
        ConnectionHandler<Team> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CREATE_TEAM), Team.class, listener, body, Const.API_CREATE_TEAM);
        connectionHandler.setTimeout(60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> uploadImage(Context context, ConnectionListener<String> listener,
                                                     int userId, String userToken, String encodedImage) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);
        Image image = new Image();
        image.setContentBase64(encodedImage);
        image.setName("");
        body.setUserImage(image);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_UPLOAD_IMAGE), String.class, listener, body, Const.API_UPLOAD_IMAGE);
        connectionHandler.setTimeout(60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }
}
