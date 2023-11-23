package it.unipi.dii.aide.movetogether.Models;

import it.unipi.dii.aide.movetogether.Views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static Model model;
    private boolean userLoginSuccessFlag;
    private boolean adminLoginSuccessFlag;

    private final ViewFactory viewFactory;
    private final DatabaseDriver databaseDriver;

    private User userLogged;
    private User adminLogged;
    private String pathId;          //pathId del path a cui abbiamo osservato la pagina
    private String moverId;         //userId dell'utente a cui abbiamo osservato il profilo
    private Activity activityToModify;
    private String userLoggedId;    //userId dell'utente che ha effettuato il login con successo
    private static ObservableList<Path> trendPaths;
    private static ObservableList<Path> appreciatedPaths;
    private static ObservableList<User> appreciatedDiscovers;
    private static ObservableList<User> friends;
    private static ObservableList<User> mutualFriends;
    private static ObservableList<User> suggestedFriends;
    private static ObservableList<Path> suggestedPaths;
    private static ObservableList<Path> mutualLikedPaths;
    private static ObservableList<Activity> activities;
    private static ObservableList<Path> foundPaths;
    private static ObservableList<Activity> foundActivities;
    private static ObservableList<User> foundUsers;
    private static ObservableList<Comment> comments;
    private static ObservableList<Path> discoveredPaths;


    private Model() {
        this.viewFactory = new ViewFactory();
        this.databaseDriver = new DatabaseDriver();
        System.out.println("Dentro Model()");
        //User data section
        this.userLoginSuccessFlag = false;

        this.friends = FXCollections.observableArrayList();
        this.mutualFriends = FXCollections.observableArrayList();
        this.suggestedFriends = FXCollections.observableArrayList();
        this.suggestedPaths = FXCollections.observableArrayList();
        this.activities = FXCollections.observableArrayList();
        this.trendPaths = FXCollections.observableArrayList();
        this.appreciatedPaths = FXCollections.observableArrayList();
        this.mutualLikedPaths = FXCollections.observableArrayList();
        this.appreciatedDiscovers = FXCollections.observableArrayList();
        this.foundPaths = FXCollections.observableArrayList();
        this.foundUsers = FXCollections.observableArrayList();
        this.foundActivities = FXCollections.observableArrayList();
        this.comments = FXCollections.observableArrayList();
        this.discoveredPaths = FXCollections.observableArrayList();
        //Admin data section
        this.adminLoginSuccessFlag = false;

    }

    public static synchronized Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }
    public ViewFactory getViewFactory(){return viewFactory;}

    public DatabaseDriver getDatabaseDriver() {return databaseDriver;}




    public ObservableList<Activity> getLastActivities(){
        return this.activities;
    }

    public void setActivities(){
        System.out.println("Dentro setActivities");
        List<Activity> act = databaseDriver.getLastActivities(userLoggedId,5,0);
        this.activities = FXCollections.observableArrayList(act);
    }


    //Ricorda: getFriends() è dal punto di vista del controller che lo può chiamare per ottenre nuovi dati
    public ObservableList<User> getFriends() {
        return this.friends;
    }

    public void setFriends(){
        System.out.println("Dentro setFriends");
        //List<User> fri = databaseDriver.getFriendsVersion3(5);
        List<User> fri = databaseDriver.getFriends(userLoggedId,5);
        this.friends = FXCollections.observableArrayList(fri);
    }

    public ObservableList<User> getMutualFriends() {return this.mutualFriends;}

    public void setMutualFriends(String userId){
        System.out.println("Dentro setMutualFriends");
        List<User> mutFri = databaseDriver.getMutualFriends(userLoggedId,userId,10);
        this.mutualFriends = FXCollections.observableArrayList(mutFri);
    }

    public ObservableList<Path> getMutualLikedPaths() {return this.mutualLikedPaths;}

    public void setMutualLikedPaths(String userId){
        System.out.println("Dentro setMutualLikedPaths");
        List<Path> paths = databaseDriver.getMutualLikedPaths(userLoggedId,userId,10);
        this.mutualLikedPaths = FXCollections.observableArrayList(paths);
    }


    public ObservableList<User> getSuggestedFriends(){
        return this.suggestedFriends;
    }

    public void setSuggestedFriends(){
        System.out.println("Dentro setSuggestedFriends()");
        List<User> fri = databaseDriver.getSuggestedFriends(userLoggedId,5);
        this.suggestedFriends = FXCollections.observableArrayList(fri);
    }

    public ObservableList<Path> getSuggestedPaths(){
        return this.suggestedPaths;
    }
    public void setSuggestedPaths(){
        System.out.println("Dentro setSuggestedPaths()");
        List<Path> paths = databaseDriver.getSuggestedPaths(userLoggedId,5);
        this.suggestedPaths = FXCollections.observableArrayList(paths);
    }

    public ObservableList<Path> getTrendPaths(){ return this.trendPaths;}
    public void setTrendPaths(){
        List<Path> paths = databaseDriver.getTrendingPaths(5);
        this.trendPaths = FXCollections.observableArrayList(paths);
    }

    public ObservableList<Path> getAppreciatedPaths(){ return this.appreciatedPaths;}
    public void setAppreciatedPaths(){
        List<Path> paths = databaseDriver.getBestRatingPath(5);
        this.appreciatedPaths = FXCollections.observableArrayList(paths);
    }
    public ObservableList<User> getAppreciatedDiscovers(){ return this.appreciatedDiscovers;}
    public void setAppreciatedDiscovers(){
        List<User> discovers = databaseDriver.getMostAppreciatedDiscovers(5);
        this.appreciatedDiscovers = FXCollections.observableArrayList(discovers);
    }

    public float getUserStatPerformance(String userId,String pathId,String typeActivity){
        return databaseDriver.getUserStatPerformance(userId,pathId,typeActivity);
    }


    public ObservableList<Comment> getComments(){ return this.comments;}

    public void synchronizeComments(){
        this.comments = null;
    }
    public void setComments(){
        List<Comment> comments = databaseDriver.getLastComments(pathId,5);
        this.comments = FXCollections.observableArrayList(comments);
    }

    public int getNumberRegisteredUsers(){
        return databaseDriver.getNumberRegisteredUsers() - 1;
        //There is a User registered as Admin e not as Mover
    }

    public User getUserLogged(){ return this.userLogged;}
    public String getUserLoggedId() { return this.userLoggedId;}
    public int getNumberDiscoveredPaths(){
        return databaseDriver.getNumberDiscoveredPaths();
    }

    public void setPathId(String pathId){this.pathId = pathId;}
    public String getPathId(){return this.pathId;}
    public void setMoverId(String moverId){this.moverId = moverId;}
    public String getMoverId(){return this.moverId;}
    public boolean getUserLoginSuccessFlag()   {return this.userLoginSuccessFlag;}
    public void setUserLoginSuccessFlag(boolean flag) {this.userLoginSuccessFlag = flag;}
    public boolean getUserAdminSuccessFlag() { return this.adminLoginSuccessFlag;}
    public void setAdminLoginSuccessFlag(boolean flag) {this.adminLoginSuccessFlag = flag;}
    public void evaluateUserCred(String username, String password) {

        User user = databaseDriver.checkCredUser(username,password);
        if(user != null){
            this.userLoginSuccessFlag = true;
            this.userLogged = user;
            this.userLoggedId = user.idUserProperty().getValue().toString();

        }
    }

    public void evaluateAdminCred(String admin_code, String password) {
        User admin = databaseDriver.checkCredUser(admin_code,password);
        if(admin != null){
            this.adminLoginSuccessFlag = true;
            this.adminLogged = admin;
        }
    }

    public void createFriendship(String moverId){
        System.out.println("Dentro Model ** createFriendship");
        Model.getInstance().databaseDriver.createFriendship(userLoggedId,moverId);
    }

    public boolean checkFriendship(String moverId){
        boolean result = databaseDriver.checkFriendship(userLoggedId,moverId);
        return result;
    }

    public void findUsers(String nameUser,String city) {
        List<User> users = databaseDriver.findUsers(nameUser,city,15);
        this.foundUsers = FXCollections.observableArrayList(users);
    }

    public ObservableList<User> getFoundUsers(){
        return this.foundUsers;
    }

    public void findActivities(String nameAct, String minDist, String maxDist, String minDur, String maxDur, String loc) {
        List<Activity> activities = databaseDriver.findActivities(nameAct,minDist,maxDist,minDur,maxDur,loc,15);
        this.foundActivities = FXCollections.observableArrayList(activities);
    }

    public void setDiscoveredPathsByMover(String moverId) {
        List<Path> discoverdPaths = databaseDriver.getDiscoveredPathsByUser(moverId,5);
        this.discoveredPaths = FXCollections.observableArrayList(discoverdPaths);
    }

    public ObservableList<Path> getDiscoveredPaths() {return this.discoveredPaths;}

    public ObservableList getFoundActivities() {
        return this.foundActivities;
    }

    public void findPaths(String namePath, String minDist, String maxDist, String minDur, String maxDur, String loc) {
        List<Path> paths = databaseDriver.findPaths(namePath,minDist,maxDist,minDur,maxDur,loc,15);
        this.foundPaths = FXCollections.observableArrayList(paths);
    }

    public ObservableList getFoundPaths() {
        return this.foundPaths;
    }

    public void setActivity(Activity act){
        this.activityToModify = act;
    }

    public void updateActivity(Activity activityModified){
        databaseDriver.updateActivity(activityModified);
    }

    public Activity getActivityToModify(){ return this.activityToModify;}
    public boolean deleteActivity(String idActivity){ return databaseDriver.deleteActivity(idActivity); }

    public User getMoverDetails(String moverId){
        User mover = databaseDriver.getUserDetails(moverId);
        return mover;
    }
    public int getKcalConsumedLastPeriod(String moverId, int months){
        return databaseDriver.getNumberKcalsBurnedLastPeriod(moverId,months);
    }
    public int getTotalActivitiesDone(String moverId){
        return databaseDriver.getNumberActivitiesPraticedByUser(moverId);
    }

    public String getPreferredTypeActivity(String moverId){
        return databaseDriver.getMostFrequentActivityTypeByUser(moverId);
    }

    public String getPreferredPath(String moverId){
        return databaseDriver.getMostPracticedPathByUser(moverId);
    }




}
