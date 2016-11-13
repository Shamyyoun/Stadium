package com.stadium.app;

import android.content.Context;

import com.stadium.app.connection.ConnectionHandler;
import com.stadium.app.connection.ConnectionListener;
import com.stadium.app.models.bodies.AddMemberToTeamBody;
import com.stadium.app.models.bodies.CaptainBody;
import com.stadium.app.models.bodies.ConfirmPresentBody;
import com.stadium.app.models.bodies.EditTeamBody;
import com.stadium.app.models.bodies.ForgetPasswordBody;
import com.stadium.app.models.bodies.LeaveTeamBody;
import com.stadium.app.models.bodies.PlayerConfirmListBody;
import com.stadium.app.models.bodies.RatePlayerBody;
import com.stadium.app.models.bodies.ReservationActionBody;
import com.stadium.app.models.bodies.ReservationsOfTeamBody;
import com.stadium.app.models.bodies.TeamPlayerActionBody;
import com.stadium.app.models.entities.Attendant;
import com.stadium.app.models.entities.City;
import com.stadium.app.models.entities.Event;
import com.stadium.app.models.entities.Field;
import com.stadium.app.models.entities.Image;
import com.stadium.app.models.entities.Reservation;
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
        connectionHandler.setTimeout(4 * 60 * 1000);
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
        connectionHandler.setTimeout(4 * 60 * 1000);
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
        connectionHandler.setTimeout(4 * 60 * 1000);
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
        connectionHandler.setTimeout(4 * 60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler confirmPresent(Context context, ConnectionListener listener,
                                                   int userId, String userToken, int resId, int type) {
        // create the request body
        ConfirmPresentBody body = new ConfirmPresentBody();
        User player = new User();
        player.setId(userId);
        player.setToken(userToken);
        body.setPlayer(player);
        Reservation res = new Reservation();
        res.setId(resId);
        body.setRes(res);
        body.setType(type);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CONFIRM_PRESENT), null, listener, body, Const.API_CONFIRM_PRESENT);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<User> getPlayerInfo(Context context, ConnectionListener<User> listener, int id) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_GET_PLAYER_INFO) + "/" + id;

        // create & execute the request
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                url, User.class, listener, Const.API_GET_PLAYER_INFO);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<ServerResponse> ratePlayer(Context context, ConnectionListener<ServerResponse> listener,
                                                               int userId, String userToken, String userName,
                                                               int playerRatedId, double rate) {
        // create the request body
        RatePlayerBody body = new RatePlayerBody();
        body.setRate(rate);
        User playerRated = new User();
        playerRated.setId(playerRatedId);
        body.setPlayerRated(playerRated);
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        user.setName(userName);
        body.setUser(user);

        // create & execute the request
        ConnectionHandler<ServerResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_RATE_PLAYER), ServerResponse.class, listener, body, Const.API_RATE_PLAYER);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> listOfMyTeams(Context context, ConnectionListener<Team[]> listener,
                                                          String userToken, int playerId) {
        // create the request body
        User body = new User();
        body.setId(playerId);
        body.setToken(userToken);

        // create & execute the request
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_LIST_OF_MY_TEAMS), Team[].class, listener, body, Const.API_LIST_OF_MY_TEAMS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> captainTeams(Context context, ConnectionListener<Team[]> listener,
                                                         int userId, String userToken) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);

        // create & execute the request
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CAPTAIN_TEAMS), Team[].class, listener, body, Const.API_CAPTAIN_TEAMS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> addMemberToTeam(Context context, ConnectionListener<String> listener,
                                                            int userId, String userToken,
                                                            int teamId, String teamName,
                                                            int playerId, String playerName) {
        // create the request body
        AddMemberToTeamBody body = new AddMemberToTeamBody();
        User player = new User();
        player.setId(playerId);
        player.setName(playerName);
        body.setPlayer(player);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        User captain = new User();
        captain.setId(userId);
        captain.setToken(userToken);
        team.setCaptain(captain);
        body.setTeam(team);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_ADD_MEMBER_TO_TEAM), String.class, listener, body, Const.API_ADD_MEMBER_TO_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team> getTeamInfo(Context context, ConnectionListener<Team> listener, int id) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_GET_TEAM_INFO) + "/" + id;

        // create & execute the request
        ConnectionHandler<Team> connectionHandler = new ConnectionHandler(context,
                url, Team.class, listener, Const.API_GET_TEAM_INFO);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User[]> teamPlayers(Context context, ConnectionListener<User[]> listener,
                                                        int teamId) {
        // create the request body
        Team body = new Team();
        body.setId(teamId);

        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_TEAM_PLAYERS), User[].class, listener, body, Const.API_TEAM_PLAYERS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> deleteMemberFromTeam(Context context, ConnectionListener<String> listener,
                                                                 int userId, String userToken,
                                                                 int teamId, String teamName,
                                                                 int playerId, String playerName) {
        // create the request body
        TeamPlayerActionBody body = new TeamPlayerActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        captain.setHisTeam(team);
        body.setCaptain(captain);
        User player = new User();
        player.setId(playerId);
        player.setName(playerName);
        body.setPlayer(player);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_DELETE_MEMBER_FROM_TEAM), String.class, listener, body, Const.API_DELETE_MEMBER_FROM_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> chooseAssistant(Context context, ConnectionListener<String> listener,
                                                            int userId, String userToken,
                                                            int teamId, int playerId) {
        // create the request body
        TeamPlayerActionBody body = new TeamPlayerActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        Team team = new Team();
        team.setId(teamId);
        captain.setHisTeam(team);
        body.setCaptain(captain);
        User user = new User();
        user.setId(playerId);
        body.setUser(user);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_CHOOSE_ASSISTANT), String.class, listener, body, Const.API_CHOOSE_ASSISTANT);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> reservationsOfTeam(Context context, ConnectionListener<Reservation[]> listener,
                                                                      int userId, String userToken, int teamId) {
        // create the request body
        ReservationsOfTeamBody body = new ReservationsOfTeamBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        Team team = new Team();
        team.setId(teamId);
        body.setTeam(team);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_RESERVATIONS_OF_TEAM), Reservation[].class, listener, body, Const.API_RESERVATIONS_OF_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> changeCaptain(Context context, ConnectionListener<String> listener,
                                                          int userId, String userToken,
                                                          int teamId, int playerId) {
        // create the request body
        TeamPlayerActionBody body = new TeamPlayerActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        Team team = new Team();
        team.setId(teamId);
        captain.setHisTeam(team);
        body.setCaptain(captain);
        User user = new User();
        user.setId(playerId);
        body.setUser(user);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_CHANGE_CAPTAIN), String.class, listener, body, Const.API_CHANGE_CAPTAIN);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> leaveTeam(Context context, ConnectionListener<String> listener,
                                                      int userId, String userToken, String userName,
                                                      int teamId, String teamName) {
        // create the request body
        LeaveTeamBody body = new LeaveTeamBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        userInfo.setName(userName);
        body.setUserinfo(userInfo);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        body.setHisTeam(team);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_LEAVE_TEAM), String.class, listener, body, Const.API_LEAVE_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String> deleteReservation(Context context, ConnectionListener<String> listener,
                                                              int userId, String userToken,
                                                              int teamId, String teamName, int reservationId) {
        // create the request body
        ReservationActionBody body = new ReservationActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        captain.setHisTeam(team);
        body.setCaptain(captain);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler<String> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_DELETE_RESERVATION), String.class, listener, body, Const.API_DELETE_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Attendant[]> playerConfirmList(Context context,
                                                                   ConnectionListener<Attendant[]> listener,
                                                                   int userId, String userToken, int reservationId) {
        // create the request body
        PlayerConfirmListBody body = new PlayerConfirmListBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler<Attendant[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_PLAYER_CONFIRMED_LIST), Attendant[].class,
                listener, body, Const.API_PLAYER_CONFIRMED_LIST);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> myTeamsReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                       int userId, String userToken) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_MY_TEAMS_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_MY_TEAMS_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<User[]> allPlayers(Context context, ConnectionListener<User[]> listener) {
        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ALL_PLAYERS), User[].class, listener, Const.API_ALL_PLAYERS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User[]> checkListOfContact(Context context, ConnectionListener<User[]> listener,
                                                               String[] phoneNumbers) {
        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHECK_LIST_OF_CONTACT), User[].class, listener,
                phoneNumbers, Const.API_CHECK_LIST_OF_CONTACT);
        connectionHandler.setTimeout(3 * 60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<String[]> getPositions(Context context, ConnectionListener<String[]> listener) {
        // create & execute the request
        ConnectionHandler<String[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_GET_POSITIONS), String[].class, listener, Const.API_GET_POSITIONS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Team> editTeam(Context context, ConnectionListener<Team> listener,
                                                   int userId, String userToken,
                                                   int id, String name, String description,
                                                   String encodedImage, String imageName,
                                                   int captainId, int assistantId,
                                                   int favoriteStadiumId) {
        // create the request body
        EditTeamBody body = new EditTeamBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        body.setUserinfo(userInfo);
        Team hisTeam = new Team();
        hisTeam.setId(id);
        hisTeam.setName(name);
        hisTeam.setDescription(description);
        User captain = new User();
        captain.setId(captainId);
        hisTeam.setCaptain(captain);
        User assistant = new User();
        assistant.setId(assistantId);
        hisTeam.setAsstent(assistant);
        hisTeam.setPreferStadiumId(favoriteStadiumId);
        if (encodedImage != null) {
            Image image = new Image();
            image.setContentBase64(encodedImage);
            image.setName(imageName);
            hisTeam.setTeamImage(image);
        }
        body.setHisTeam(hisTeam);

        // create & execute the request
        ConnectionHandler<Team> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_EDIT_TEAM), Team.class, listener, body, Const.API_EDIT_TEAM);
        connectionHandler.setTimeout(4 * 60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium[]> listStadiumsAround(Context context, ConnectionListener<Stadium[]> listener,
                                                                  double lat, double lng) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_LIST_STADIUMS_AROUND) + "/" + lat + "/" + lng;

        // create & execute the request
        ConnectionHandler<Stadium[]> connectionHandler = new ConnectionHandler(context,
                url, Stadium[].class, listener, Const.API_LIST_STADIUMS_AROUND);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<String[]> fieldSizes(Context context, ConnectionListener<String[]> listener) {
        // create & execute the request
        ConnectionHandler<String[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_FIELD_SIZE), String[].class, listener, Const.API_FIELD_SIZE);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<String[]> allDurations(Context context, ConnectionListener<String[]> listener) {
        // create & execute the request
        ConnectionHandler<String[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ALL_DURATIONS), String[].class, listener, Const.API_ALL_DURATIONS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium[]> stadiumsFilters(Context context, ConnectionListener<Stadium[]> listener,
                                                               int cityId, String name, String fieldSize,
                                                               String date, String timeStart, String timeEnd) {
        // create the request body
        Reservation body = new Reservation();
        if (cityId != 0 || name != null) {
            Stadium stadium = new Stadium();
            if (cityId != 0) {
                City stadiumCity = new City();
                stadiumCity.setId(cityId);
                stadium.setStadiumCity(stadiumCity);
            }
            stadium.setName(name);
            body.setReservationStadium(stadium);
        }
        if (fieldSize != null) {
            Field field = new Field();
            field.setFieldSize(fieldSize);
            body.setField(field);
        }
        body.setDate(date);
        body.setTimeStart(timeStart);
        body.setTimeEnd(timeEnd);

        // create & execute the request
        ConnectionHandler<Stadium[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_STADIUMS_FILTERS), Stadium[].class, listener, body, Const.API_STADIUMS_FILTERS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium> getStadiumInfo(Context context, ConnectionListener<Stadium> listener, int id) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_GET_STADIUM_INFO) + "/" + id;

        // create & execute the request
        ConnectionHandler<Stadium> connectionHandler = new ConnectionHandler(context,
                url, Stadium.class, listener, Const.API_GET_STADIUM_INFO);
        connectionHandler.executeGet();
        return connectionHandler;
    }
}
