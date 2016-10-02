package com.stadium.app;

/**
 * Created by Shamyyoun on 18/12/15.
 */
public class Const {
    // App level constants:--------------
    public static final String LOG_TAG = "Stadium";
    public static final String SHARED_PREFERENCES_FILE_NAME = "Stadium";
    public static final String APP_FILES_DIR = "/.stadium";
    public static final String END_POINT = "http://ec2-52-33-173-21.us-west-2.compute.amazonaws.com/Staduim/test/Service1.svc";

    // APIs:------------------------------
    public static final String ROUTE_USER = "user";
    public static final String API_LOGIN = "LOGIN";
    public static final String API_CHECK_EMAIL = "CheckEmail";
    public static final String API_FORGET_PASSWORD = "forgetPassword";
    public static final String API_GET_CITIES = "getCities";
    public static final String API_CREATE_USER = "CreateUser";
    public static final String API_LIST_OF_STADIUMS = "ListOfStadiums";
    public static final String API_GET_EVENT = "getEvent";
    public static final String API_EDIT_PROFILE = "editProfile";

    // SP Constants:----------------------
    public static final String SP_USER = "user";

    // Response Codes:--------------------
    public static final int SER_CODE_200 = 200;

    // Images uploading:-------------------
    public static final int IMG_ASPECT_X_PROFILE = 1;
    public static final int IMG_ASPECT_Y_PROFILE = 1;
    public static final int MAX_IMG_DIMEN_PROFILE = 400;

    // Activity requests:-------------------
    public static final int REQ_UPDATE_PROFILE = 1;
}
