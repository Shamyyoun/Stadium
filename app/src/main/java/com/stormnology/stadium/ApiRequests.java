package com.stormnology.stadium;

import android.content.Context;

import com.stormnology.stadium.connection.ConnectionHandler;
import com.stormnology.stadium.connection.ConnectionListener;
import com.stormnology.stadium.models.bodies.AddMemberToTeamBody;
import com.stormnology.stadium.models.bodies.AddMonthlyReservationBody;
import com.stormnology.stadium.models.bodies.AdminBody;
import com.stormnology.stadium.models.bodies.AdminReservationActionBody;
import com.stormnology.stadium.models.bodies.AvailableReservationsBody;
import com.stormnology.stadium.models.bodies.CaptainBody;
import com.stormnology.stadium.models.bodies.CaptainReservationActionBody;
import com.stormnology.stadium.models.bodies.ChallengeActionBody;
import com.stormnology.stadium.models.bodies.ChangeDurationsBody;
import com.stormnology.stadium.models.bodies.CheckListOfContactsBody;
import com.stormnology.stadium.models.bodies.ConfirmPresentBody;
import com.stormnology.stadium.models.bodies.DurationsBody;
import com.stormnology.stadium.models.bodies.EditTeamBody;
import com.stormnology.stadium.models.bodies.ForgetPasswordBody;
import com.stormnology.stadium.models.bodies.LeaveTeamBody;
import com.stormnology.stadium.models.bodies.MonthlyReservationBody;
import com.stormnology.stadium.models.bodies.PlayerConfirmListBody;
import com.stormnology.stadium.models.bodies.RatePlayerBody;
import com.stormnology.stadium.models.bodies.TeamActionBody;
import com.stormnology.stadium.models.bodies.TeamInvitationsBody;
import com.stormnology.stadium.models.bodies.TeamPlayerActionBody;
import com.stormnology.stadium.models.bodies.UnblockTeamBody;
import com.stormnology.stadium.models.entities.Attendant;
import com.stormnology.stadium.models.entities.Challenge;
import com.stormnology.stadium.models.entities.ChallengeType;
import com.stormnology.stadium.models.entities.City;
import com.stormnology.stadium.models.entities.Duration;
import com.stormnology.stadium.models.entities.Event;
import com.stormnology.stadium.models.entities.Field;
import com.stormnology.stadium.models.entities.Image;
import com.stormnology.stadium.models.entities.Reservation;
import com.stormnology.stadium.models.entities.Stadium;
import com.stormnology.stadium.models.entities.Team;
import com.stormnology.stadium.models.entities.User;
import com.stormnology.stadium.models.responses.DurationsResponse;
import com.stormnology.stadium.models.responses.MonthlyReservationResponse;
import com.stormnology.stadium.models.responses.ServerResponse;
import com.stormnology.stadium.utils.AppUtils;
import com.stormnology.stadium.utils.Utils;

import java.util.List;

/**
 * Created by Shamyyoun on 5/31/16.
 */
public class ApiRequests {
    public static ConnectionHandler<User> login(Context context, ConnectionListener<User> listener,
                                                String phone, String password) {
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

    public static ConnectionHandler<ServerResponse> checkEmail(Context context, ConnectionListener<ServerResponse> listener,
                                                               String phone) {
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
                                                     String name, String birthdate, City city,
                                                     String phone, String password,
                                                     String encodedImage, int userType) {
        // create the request body
        User body = new User();
        body.setName(name);
        body.setDateOfBirth(birthdate);
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
                                                      String birthDate, City city, String phone,
                                                      String position, String email, String bio) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);
        body.setDateOfBirth(birthDate);
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
                                                   int userId, String userToken,
                                                   String playerName, int resId,
                                                   int teamId, int type) {
        // create the request body
        ConfirmPresentBody body = new ConfirmPresentBody();
        User player = new User();
        player.setId(userId);
        player.setToken(userToken);
        player.setName(playerName);
        body.setPlayer(player);
        Reservation res = new Reservation();
        res.setId(resId);
        Team team = new Team();
        team.setId(teamId);
        res.setReservationTeam(team);
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

    public static ConnectionHandler<User> ratePlayer(Context context, ConnectionListener<User> listener,
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
        ConnectionHandler<User> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_RATE_PLAYER), User.class, listener, body, Const.API_RATE_PLAYER);
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

    public static ConnectionHandler addMemberToTeam(Context context, ConnectionListener listener,
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
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_ADD_MEMBER_TO_TEAM), null, listener, body, Const.API_ADD_MEMBER_TO_TEAM);
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

    public static ConnectionHandler deleteMemberFromTeam(Context context, ConnectionListener listener,
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
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_DELETE_MEMBER_FROM_TEAM), null, listener, body, Const.API_DELETE_MEMBER_FROM_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler chooseAssistant(Context context, ConnectionListener listener,
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
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_CHOOSE_ASSISTANT), null, listener, body, Const.API_CHOOSE_ASSISTANT);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> reservationsOfTeam(Context context, ConnectionListener<Reservation[]> listener,
                                                                      int userId, String userToken, int teamId) {
        // create the request body
        TeamActionBody body = new TeamActionBody();
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

    public static ConnectionHandler changeCaptain(Context context, ConnectionListener listener,
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
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_CHANGE_CAPTAIN), null, listener, body, Const.API_CHANGE_CAPTAIN);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler leaveTeam(Context context, ConnectionListener listener,
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
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_LEAVE_TEAM), null, listener, body, Const.API_LEAVE_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler deleteReservation(Context context, ConnectionListener listener,
                                                      int userId, String userToken,
                                                      int teamId, String teamName,
                                                      int reservationId, int fieldId) {
        // create the request body
        CaptainReservationActionBody body = new CaptainReservationActionBody();
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
        Field field = new Field();
        field.setId(fieldId);
        reservation.setField(field);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_DELETE_RESERVATION), null, listener, body, Const.API_DELETE_RESERVATION);
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

    public static ConnectionHandler<User[]> allPlayers(Context context, ConnectionListener<User[]> listener,
                                                       int userId, int page, int order) {
        return allPlayers(context, listener, userId, page, order, 0, null, null, null);
    }

    public static ConnectionHandler<User[]> allPlayers(Context context, ConnectionListener<User[]> listener,
                                                       int userId, int page, int order,
                                                       int cityId, String name,
                                                       String position, String phone) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_ALL_PLAYERS) + "?";
        url = Utils.addQueryParam(url, Const.PARAM_ID, userId);
        url = Utils.addQueryParam(url, Const.PARAM_PAGE, page);
        url = Utils.addQueryParam(url, Const.PARAM_ORDER, order);
        if (cityId != 0) {
            url = Utils.addQueryParam(url, Const.PARAM_CITY, cityId);
        }
        if (name != null) {
            url = Utils.addQueryParam(url, Const.PARAM_NAME, name);
        }
        if (position != null) {
            url = Utils.addQueryParam(url, Const.PARAM_POSITION, position);
        }
        if (phone != null) {
            url = Utils.addQueryParam(url, Const.PARAM_PHONE, phone);
        }

        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context, url,
                User[].class, listener, Const.API_ALL_PLAYERS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<User[]> checkListOfContact(Context context, ConnectionListener<User[]> listener,
                                                               List<String> phoneNumbers, int userId) {
        // create the request body
        CheckListOfContactsBody body = new CheckListOfContactsBody();
        body.setId(userId);
        body.setContacts(phoneNumbers);

        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHECK_LIST_OF_CONTACT), User[].class,
                listener, body, Const.API_CHECK_LIST_OF_CONTACT);
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
        String url = AppUtils.getUserApiUrl(Const.API_LIST_STADIUMS_AROUND) + "/" + lng + "/" + lat;

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

    public static ConnectionHandler<Reservation[]> availableReservationSize(Context context, ConnectionListener<Reservation[]> listener,
                                                                            int stadiumId, String date, String fieldSize) {
        // create the request body
        AvailableReservationsBody body = new AvailableReservationsBody();
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setStadium(stadium);
        Reservation reservation = new Reservation();
        reservation.setDate(date);
        Field field = new Field();
        field.setFieldSize(fieldSize);
        reservation.setField(field);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_AVAILABLE_RESERVATION_SIZE), Reservation[].class,
                listener, body, Const.API_AVAILABLE_RESERVATION_SIZE);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation> captainAddReservation(Context context, ConnectionListener<Reservation> listener,
                                                                       int userId, String userToken,
                                                                       int teamId, String teamName,
                                                                       List<Integer> playersIds, int intervalNum,
                                                                       int price, int playersCount,
                                                                       int fieldId, String date,
                                                                       String timeStart, String timeEnd) {
        // create the request body
        CaptainReservationActionBody body = new CaptainReservationActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        Team hisTeam = new Team();
        hisTeam.setId(teamId);
        hisTeam.setName(teamName);
        captain.setHisTeam(hisTeam);
        body.setCaptain(captain);
        Reservation reservation = new Reservation();
        reservation.setPlayerId(playersIds);
        reservation.setIntervalNum(intervalNum);
        reservation.setPrice(price);
        reservation.setPlayerCounter(playersCount);
        Field field = new Field();
        field.setId(fieldId);
        reservation.setField(field);
        reservation.setDate(date);
        reservation.setTimeStart(timeStart);
        reservation.setTimeEnd(timeEnd);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler<Reservation> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_ADD_RESERVATION), Reservation.class, listener,
                body, Const.API_ADD_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> todayReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                     int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_TODAY_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_TODAY_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> getReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                   int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_GET_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_GET_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> newReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                   int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_NEW_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_NEW_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> lastReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                    int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_LAST_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_LAST_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> getMyReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                     int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_GET_MY_RESERVATIONS), Reservation[].class,
                listener, body, Const.API_GET_MY_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler confirmReservation(Context context, ConnectionListener listener,
                                                       int userId, String userToken,
                                                       int stadiumId, String stadiumName,
                                                       int reservationId, int teamId,
                                                       int confirmType) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        Team team = new Team();
        team.setId(teamId);
        reservation.setReservationTeam(team);
        body.setReservation(reservation);
        body.setB(confirmType);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_CONFIRM_RESERVATION), null, listener, body,
                Const.API_CONFIRM_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler absentReservation(Context context, ConnectionListener listener,
                                                      int userId, String userToken,
                                                      int stadiumId, String stadiumName,
                                                      int reservationId, int teamId) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        Team team = new Team();
        team.setId(teamId);
        reservation.setReservationTeam(team);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_ABSENT_RESERVATION), null, listener, body,
                Const.API_ABSENT_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler blockTeam(Context context, ConnectionListener listener,
                                              int userId, String userToken,
                                              int stadiumId, String stadiumName,
                                              int reservationId, int teamId) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        Team team = new Team();
        team.setId(teamId);
        reservation.setReservationTeam(team);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_BLOCK_TEAM), null, listener, body,
                Const.API_BLOCK_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler absentBlockReservation(Context context, ConnectionListener listener,
                                                           int userId, String userToken,
                                                           int stadiumId, String stadiumName,
                                                           int reservationId, int teamId) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        Team team = new Team();
        team.setId(teamId);
        reservation.setReservationTeam(team);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_ABSENT_BLOCK_RESERVATION), null, listener, body,
                Const.API_ABSENT_BLOCK_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler cancelReservation(Context context, ConnectionListener listener,
                                                      int userId, String userToken,
                                                      int stadiumId, int reservationId,
                                                      int teamId, String teamName) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        admin.setHisStadium(stadium);
        body.setUser(admin);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        reservation.setReservationTeam(team);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_CANCEL_RESERVATION), null, listener, body,
                Const.API_CANCEL_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> myBlockedList(Context context, ConnectionListener<Team[]> listener,
                                                          int userId, String userToken, int stadiumId) {
        // create the request body
        AdminBody body = new AdminBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUserinfo(user);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_MY_BLOCKED_LIST), Team[].class,
                listener, body, Const.API_MY_BLOCKED_LIST);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler unblockTeam(Context context, ConnectionListener listener,
                                                int userId, String userToken,
                                                int stadiumId, String stadiumName,
                                                int teamId) {
        // create the request body
        UnblockTeamBody body = new UnblockTeamBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        Team team = new Team();
        team.setId(teamId);
        body.setTeam(team);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_UNBLOCK_TEAM), null, listener, body,
                Const.API_UNBLOCK_TEAM);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation> adminAddReservation(Context context, ConnectionListener<Reservation> listener,
                                                                     int userId, String userToken,
                                                                     int stadiumId, String stadiumName,
                                                                     String customerName, String customerPhone,
                                                                     int intervalNum, int price,
                                                                     int fieldId, String fieldNumber,
                                                                     String date, String timeStart, String timeEnd) {
        // create the request body
        AdminReservationActionBody body = new AdminReservationActionBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium hisStadium = new Stadium();
        hisStadium.setId(stadiumId);
        hisStadium.setName(stadiumName);
        admin.setHisStadium(hisStadium);
        body.setAdmin(admin);
        Reservation reservation = new Reservation();
        reservation.setCustomerName(customerName);
        reservation.setCustomerPhone(customerPhone);
        reservation.setIntervalNum(intervalNum);
        reservation.setPrice(price);
        Field field = new Field();
        field.setId(fieldId);
        field.setFieldNumber(fieldNumber);
        reservation.setField(field);
        reservation.setDate(date);
        reservation.setTimeStart(timeStart);
        reservation.setTimeEnd(timeEnd);
        body.setReservation(reservation);

        // create & execute the request
        ConnectionHandler<Reservation> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_ADD_RESERVATION), Reservation.class, listener,
                body, Const.API_ADD_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> getAllTeams(Context context, ConnectionListener<Team[]> listener) {
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_GET_ALL_TEAMS), Team[].class, listener, Const.API_GET_ALL_TEAMS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Field[]> getMyFields(Context context, ConnectionListener<Field[]> listener, int stadiumId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_GET_MY_FIELDS, stadiumId);

        // create & execute the request
        ConnectionHandler<Field[]> connectionHandler = new ConnectionHandler(context,
                url, Field[].class, listener, Const.API_GET_MY_FIELDS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<DurationsResponse> getMyDurations(Context context, ConnectionListener<DurationsResponse> listener,
                                                                      int stadiumId) {
        return getMyDurations(context, listener, stadiumId, null, null);
    }

    public static ConnectionHandler<DurationsResponse> getMyDurations(Context context, ConnectionListener<DurationsResponse> listener,
                                                                      int stadiumId, String startDate, String endDate) {
        // create the request body
        DurationsBody body = new DurationsBody();
        body.setStadiumId(stadiumId);
        body.setStartDate(startDate);
        body.setEndDate(endDate);

        // create & execute the request
        ConnectionHandler<DurationsResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_GET_MY_DURATIONS), DurationsResponse.class, listener, body, Const.API_GET_MY_DURATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<MonthlyReservationResponse> monthlyReservation(Context context, ConnectionListener<MonthlyReservationResponse> listener,
                                                                                   int stadiumId, String startDate, String endDate,
                                                                                   int fieldId, String fieldSize,
                                                                                   int intervalNum, String dayName) {
        // create the request body
        MonthlyReservationBody body = new MonthlyReservationBody();
        body.setStadiumId(stadiumId);
        body.setStartDate(startDate);
        body.setEndDate(endDate);
        body.setFieldId(fieldId);
        body.setFieldSize(fieldSize);
        body.setIntervalNum(intervalNum);
        body.setDayName(dayName);

        // create & execute the request
        ConnectionHandler<MonthlyReservationResponse> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_MONTHLY_RESERVATION), MonthlyReservationResponse.class,
                listener, body, Const.API_MONTHLY_RESERVATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler addMonthlyReservations(Context context, ConnectionListener listener,
                                                           int userId, String userToken,
                                                           int stadiumId, String stadiumName,
                                                           MonthlyReservationResponse monthlyReservationResponse,
                                                           int teamId, String teamName, float price) {
        // create the request body
        AddMonthlyReservationBody body = new AddMonthlyReservationBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setName(stadiumName);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        monthlyReservationResponse.setTeamId(teamId);
        monthlyReservationResponse.setTeamName(teamName);
        monthlyReservationResponse.setPrice(price);
        body.setReservation(monthlyReservationResponse);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_ADD_MONTHLY_RESERVATIONS), null, listener,
                body, Const.API_ADD_MONTHLY_RESERVATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium> editStadium(Context context, ConnectionListener<Stadium> listener,
                                                         int userId, String userToken,
                                                         int stadiumId, String bio,
                                                         String encodedImage, String imageName) {
        // create the request body
        AdminBody body = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        body.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        stadium.setBio(bio);
        if (encodedImage != null) {
            Image image = new Image();
            image.setContentBase64(encodedImage);
            image.setName(imageName);
            stadium.setStadiumImage(image);
        }
        body.setHisStadium(stadium);

        // create & execute the request
        ConnectionHandler<Stadium> connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_STADIUM_PROFILE), Stadium.class, listener, body, Const.API_STADIUM_PROFILE);
        connectionHandler.setTimeout(4 * 60 * 1000);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler changeDuration(Context context, ConnectionListener listener,
                                                   int userId, String userToken, int stadiumId,
                                                   String startDate, List<Duration> times) {
        // create the request body
        ChangeDurationsBody body = new ChangeDurationsBody();
        AdminBody admin = new AdminBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        admin.setUserinfo(userInfo);
        Stadium stadium = new Stadium();
        stadium.setId(stadiumId);
        admin.setHisStadium(stadium);
        body.setAdmin(admin);
        body.setStartDate(startDate);
        body.setTimes(times);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getAdminApiUrl(Const.API_CHANGE_DURATION), null, listener, body, Const.API_CHANGE_DURATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Field[]> fieldSizes(Context context, ConnectionListener<Field[]> listener, int stadiumId) {
        // prepare url
        String url = AppUtils.getUserApiUrl(Const.API_FIELD_SIZE, stadiumId);

        // create & execute the request
        ConnectionHandler<Field[]> connectionHandler = new ConnectionHandler(context,
                url, Field[].class, listener, Const.API_FIELD_SIZE);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Boolean> phoneValidation(Context context, ConnectionListener<Boolean> listener,
                                                             int userId, String validationNumber) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setValidationNumber(validationNumber);

        // create & execute the request
        ConnectionHandler<Boolean> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_PHONE_VALIDATION), Boolean.class, listener, body, Const.API_PHONE_VALIDATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Boolean> resendValidation(Context context, ConnectionListener<Boolean> listener,
                                                              int userId) {
        // create the request body
        User body = new User();
        body.setId(userId);

        // create & execute the request
        ConnectionHandler<Boolean> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_RESEND_VALIDATION), Boolean.class, listener, body, Const.API_RESEND_VALIDATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler changePassword(Context context, ConnectionListener listener,
                                                   int userId, String userToken,
                                                   String password, String newPassword) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);
        body.setPassword(password);
        body.setNewPassword(newPassword);

        // create & execute the request
        ConnectionHandler connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHANGE_PASSWORD), null, listener, body, Const.API_CHANGE_PASSWORD);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> myInvitations(Context context, ConnectionListener<Team[]> listener,
                                                          int userId, String userToken) {
        // create the request body
        User body = new User();
        body.setId(userId);
        body.setToken(userToken);

        // create & execute the request
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_MY_INVITATIONS), Team[].class, listener, body, Const.API_MY_INVITATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Boolean> acceptInvitation(Context context, ConnectionListener<Boolean> listener,
                                                              int userId, String userToken, String userName,
                                                              int teamId, String teamName) {
        // create the request body
        TeamActionBody body = new TeamActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        user.setName(userName);
        body.setUser(user);
        Team team = new Team();
        team.setId(teamId);
        team.setName(teamName);
        body.setTeam(team);

        // create & execute the request
        ConnectionHandler<Boolean> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ACCEPT_INVITATION), Boolean.class, listener, body, Const.API_ACCEPT_INVITATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<User[]> teamInvitations(Context context, ConnectionListener<User[]> listener,
                                                            int userId, String userToken, int teamId) {
        // create the request body
        TeamInvitationsBody body = new TeamInvitationsBody();
        body.setId(teamId);
        User captain = new User();
        captain.setId(userId);
        captain.setToken(userToken);
        body.setCaptain(captain);

        // create & execute the request
        ConnectionHandler<User[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_TEAM_INVITATIONS), User[].class, listener, body, Const.API_TEAM_INVITATIONS);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Boolean> removeInvitation(Context context, ConnectionListener<Boolean> listener,
                                                              int userId, String userToken,
                                                              int teamId, int playerId) {
        // create the request body
        TeamActionBody body = new TeamActionBody();
        Team team = new Team();
        team.setId(teamId);
        User captain = new User();
        captain.setId(userId);
        captain.setToken(userToken);
        team.setCaptain(captain);
        body.setTeam(team);
        User player = new User();
        player.setId(playerId);
        body.setPlayer(player);

        // create & execute the request
        ConnectionHandler<Boolean> connectionHandler = new ConnectionHandler(context,
                AppUtils.getCaptainApiUrl(Const.API_REMOVE_INVITATION), Boolean.class, listener, body, Const.API_REMOVE_INVITATION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge[]> newChallenges(Context context, ConnectionListener<Challenge[]> listener,
                                                               int userId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_NEW_CHALLENGES);
        url += "?" + Const.PARAM_ID + "=" + userId;

        // create & execute the request
        ConnectionHandler<Challenge[]> connectionHandler = new ConnectionHandler(context,
                url, Challenge[].class, listener, Const.API_NEW_CHALLENGES);

        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge[]> acceptedChallenges(Context context, ConnectionListener<Challenge[]> listener,
                                                                    int userId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_ACCEPTED_CHALLENGES);
        url += "?" + Const.PARAM_ID + "=" + userId;

        // create & execute the request
        ConnectionHandler<Challenge[]> connectionHandler = new ConnectionHandler(context,
                url, Challenge[].class, listener, Const.API_ACCEPTED_CHALLENGES);

        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge[]> historicChallenges(Context context, ConnectionListener<Challenge[]> listener,
                                                                    int userId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_HISTORIC_CHALLENGES);
        url += "?" + Const.PARAM_ID + "=" + userId;

        // create & execute the request
        ConnectionHandler<Challenge[]> connectionHandler = new ConnectionHandler(context,
                url, Challenge[].class, listener, Const.API_HISTORIC_CHALLENGES);

        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge[]> myChallenges(Context context, ConnectionListener<Challenge[]> listener,
                                                              int userId) {
        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_MY_CHALLENGES);
        url += "?" + Const.PARAM_ID + "=" + userId;

        // create & execute the request
        ConnectionHandler<Challenge[]> connectionHandler = new ConnectionHandler(context,
                url, Challenge[].class, listener, Const.API_MY_CHALLENGES);

        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> acceptChallenge(Context context, ConnectionListener<Challenge> listener,
                                                               int userId, String userToken,
                                                               int challengeId) {

        // create the challenge
        Challenge challenge = new Challenge();
        challenge.setId(challengeId);

        return acceptChallenge(context, listener, userId, userToken, challenge);
    }

    public static ConnectionHandler<Challenge> acceptChallenge(Context context, ConnectionListener<Challenge> listener,
                                                               int userId, String userToken, Challenge challenge) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ACCEPT_CHALLENGE), Challenge.class, listener, body, Const.API_ACCEPT_CHALLENGE);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> leaveChallenge(Context context, ConnectionListener<Challenge> listener,
                                                              int userId, String userToken, Challenge challenge) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        CaptainBody captain = new CaptainBody();
        User userInfo = new User();
        userInfo.setId(userId);
        userInfo.setToken(userToken);
        captain.setUserinfo(userInfo);
        body.setCaptain(captain);
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_LEAVE_CHALLENGE), Challenge.class, listener, body, Const.API_LEAVE_CHALLENGE);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Reservation[]> challengeReservations(Context context, ConnectionListener<Reservation[]> listener,
                                                                         int teamId) {

        // prepare the url
        String url = AppUtils.getUserApiUrl(Const.API_CHALLENGE_RESERVATIONS);
        url += "?" + Const.PARAM_TEAM_ID + "=" + teamId;

        // create & execute the request
        ConnectionHandler<Reservation[]> connectionHandler = new ConnectionHandler(context,
                url, Reservation[].class, listener, Const.API_CHALLENGE_RESERVATIONS);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> addResToChallenge(Context context, ConnectionListener<Challenge> listener,
                                                                 int userId, String userToken,
                                                                 Challenge challenge, int reservationId) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        challenge.setReservation(reservation);
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ADD_RES_TO_CHALLENGE), Challenge.class, listener, body, Const.API_ADD_RES_TO_CHALLENGE);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> resultObjection(Context context, ConnectionListener<Challenge> listener,
                                                               int userId, String userToken, Challenge challenge) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_RESULT_OBJECTION), Challenge.class, listener, body, Const.API_RESULT_OBJECTION);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> addChallengeResult(Context context, ConnectionListener<Challenge> listener,
                                                                  int userId, String userToken, Challenge challenge,
                                                                  int hostGoals, int guestGoals) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        challenge.setHostGoals(hostGoals);
        challenge.setGuestGoals(guestGoals);
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ADD_CHALLENGE_RESULT), Challenge.class, listener, body, Const.API_ADD_CHALLENGE_RESULT);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<ChallengeType[]> challengeTypes(Context context, ConnectionListener<ChallengeType[]> listener) {
        // create & execute the request
        ConnectionHandler<ChallengeType[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_TYPES), ChallengeType[].class, listener, Const.API_CHALLENGE_TYPES);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Team[]> challengeAgainst(Context context, ConnectionListener<Team[]> listener) {
        // create & execute the request
        ConnectionHandler<Team[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_AGAINST), Team[].class, listener, Const.API_CHALLENGE_AGAINST);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Stadium[]> challengePlaces(Context context, ConnectionListener<Stadium[]> listener) {
        // create & execute the request
        ConnectionHandler<Stadium[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_PLACES), Stadium[].class, listener, Const.API_CHALLENGE_PLACES);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<String[]> challengeDay(Context context, ConnectionListener<String[]> listener) {
        // create & execute the request
        ConnectionHandler<String[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_DAY), String[].class, listener, Const.API_CHALLENGE_DAY);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<String[]> challengeTimes(Context context, ConnectionListener<String[]> listener) {
        // create & execute the request
        ConnectionHandler<String[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_TIMES), String[].class, listener, Const.API_CHALLENGE_TIMES);
        connectionHandler.executeGet();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge[]> challengeSearch(Context context, ConnectionListener<Challenge[]> listener,
                                                                 int typeId, int teamId,
                                                                 String place, String day,
                                                                 String time) {
        // create the request body
        Challenge body = new Challenge();
        ChallengeType type = new ChallengeType();
        type.setId(typeId);
        body.setType(type);
        Team team = new Team();
        team.setId(teamId);
        body.setHostTeam(team);
        body.setPlace(place);
        body.setDay(day);
        body.setTime(time);

        // create & execute the request
        ConnectionHandler<Challenge[]> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_CHALLENGE_SEARCH), Challenge[].class, listener, body, Const.API_CHALLENGE_SEARCH);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }

    public static ConnectionHandler<Challenge> addChallenge(Context context, ConnectionListener<Challenge> listener,
                                                            int userId, String userToken,
                                                            int hostTeamId, String hostTeamName,
                                                            int guestTeamId, String guestTeamName,
                                                            String hostComment, int reservationId,
                                                            String place, String day, String time) {
        // create the request body
        ChallengeActionBody body = new ChallengeActionBody();
        User user = new User();
        user.setId(userId);
        user.setToken(userToken);
        body.setUser(user);
        Challenge challenge = new Challenge();
        Team hostTeam = new Team();
        hostTeam.setId(hostTeamId);
        hostTeam.setName(hostTeamName);
        challenge.setHostTeam(hostTeam);
        Team guestTeam = new Team();
        guestTeam.setId(guestTeamId);
        guestTeam.setName(guestTeamName);
        challenge.setGuestTeam(guestTeam);
        challenge.setHostComment(hostComment);
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        challenge.setReservation(reservation);
        if (reservationId == 0) {
            challenge.setPlace(place);
            challenge.setDay(day);
            challenge.setTime(time);
        }
        body.setChallenge(challenge);

        // create & execute the request
        ConnectionHandler<Challenge> connectionHandler = new ConnectionHandler(context,
                AppUtils.getUserApiUrl(Const.API_ADD_CHALLENGE), Challenge.class, listener, body, Const.API_ADD_CHALLENGE);
        connectionHandler.executeRawJson();
        return connectionHandler;
    }
}
