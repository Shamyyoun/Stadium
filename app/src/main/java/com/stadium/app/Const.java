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
    public static final int STADIUMS_SEARCH_MAX_DATE_DAYS_FROM_NOW = 14;
    public static final int DEFAULT_ITEM_ID = -1; // this is used to add a default item in lists used in adapter

    // APIs:------------------------------
    public static final String ROUTE_USER = "user";
    public static final String ROUTE_CAPTAIN = "Captain";
    public static final String ROUTE_ADMIN = "Admin";
    public static final String API_LOGIN = "LOGIN";
    public static final String API_CHECK_EMAIL = "CheckEmail";
    public static final String API_FORGET_PASSWORD = "forgetPassword";
    public static final String API_GET_CITIES = "getCities";
    public static final String API_CREATE_USER = "CreateUser";
    public static final String API_LIST_OF_STADIUMS = "ListOfStadiums";
    public static final String API_GET_EVENT = "getEvent";
    public static final String API_EDIT_PROFILE = "editProfile";
    public static final String API_CREATE_TEAM = "CreateTeam";
    public static final String API_UPLOAD_IMAGE = "UploadImage";
    public static final String API_CONFIRM_PRESENT = "confirmPresent";
    public static final String API_GET_PLAYER_INFO = "getPlayerInfo";
    public static final String API_RATE_PLAYER = "ratePlayer";
    public static final String API_LIST_OF_MY_TEAMS = "ListOfMyTeams";
    public static final String API_CAPTAIN_TEAMS = "captainTeams";
    public static final String API_ADD_MEMBER_TO_TEAM = "AddMemeberToTeam";
    public static final String API_GET_TEAM_INFO = "getTeamInfo";
    public static final String API_TEAM_PLAYERS = "teamPlayers";
    public static final String API_DELETE_MEMBER_FROM_TEAM = "DeleteMemberFromTeam";
    public static final String API_CHOOSE_ASSISTANT = "ChooseAssistant";
    public static final String API_RESERVATIONS_OF_TEAM = "ReservationsOfTeam";
    public static final String API_CHANGE_CAPTAIN = "ChangeCaptain";
    public static final String API_LEAVE_TEAM = "leaveTeam";
    public static final String API_CANCEL_RESERVATION = "deleteReservation";
    public static final String API_DELETE_RESERVATION = "DeleteReservation";
    public static final String API_PLAYER_CONFIRMED_LIST = "playerConfirmedList";
    public static final String API_MY_TEAMS_RESERVATIONS = "myTeamsReservations";
    public static final String API_ALL_PLAYERS = "allPlayers";
    public static final String API_CHECK_LIST_OF_CONTACT = "checkListOfContact";
    public static final String API_GET_POSITIONS = "getPositions";
    public static final String API_EDIT_TEAM = "EditTeam";
    public static final String API_LIST_STADIUMS_AROUND = "ListstaduimsAround";
    public static final String API_FIELD_SIZE = "fieldSizes";
    public static final String API_ALL_DURATIONS = "allDurations";
    public static final String API_STADIUMS_FILTERS = "stadiumsFilters";
    public static final String API_GET_STADIUM_INFO = "getStadiumInfo";
    public static final String API_AVAILABLE_RESERVATION_SIZE = "AvailbleReservation_Size";
    public static final String API_ADD_RESERVATION = "AddReservation";

    // SP Constants:----------------------
    public static final String SP_USER = "user";

    // Server Constants:--------------------
    public static final int SER_CODE_200 = 200;
    public static final String SER_DATE_FORMAT = "yyyy/MM/dd";
    public static final String SER_TIME_FORMAT = "hh:mm:ss";

    // Images uploading:-------------------
    public static final int IMG_ASPECT_X_PROFILE = 1;
    public static final int IMG_ASPECT_Y_PROFILE = 1;
    public static final int MAX_IMG_DIMEN_PROFILE = 500;
    public static final int IMG_ASPECT_X_TEAM = 1;
    public static final int IMG_ASPECT_Y_TEAM = 1;
    public static final int MAX_IMG_DIMEN_TEAM = 500;

    // Activity requests:-------------------
    public static final int REQ_UPDATE_PROFILE = 1;
    public static final int REQ_UPDATE_PROFILE_IMAGE = 2;
    public static final int REQ_CREATE_TEAM = 3;
    public static final int REQ_SEARCH_PLAYERS = 4;
    public static final int REQ_UPDATE_TEAM = 5;
    public static final int REQ_ADD_PLAYERS = 6;
    public static final int REQ_VIEW_CONTACTS = 7;
    public static final int REQ_SEARCH_STADIUMS = 8;
    public static final int REQ_ADD_RESERVATIONS = 9;

    // Keys:----------------------------------
    public static final String KEY_ID = "id";
    public static final String KEY_TEAM = "team";
    public static final String KEY_FILTER = "filter";
    public static final String KEY_RESERVATION = "reservation";
    public static final String KEY_TOOLBAR_TITLE = "toolbar_title";

    // Permission request:---------------------
    public static final int PERM_REQ_CONTACTS = 1;
    public static final int PERM_REQ_LOCATION = 2;
}
