package it.unipi.dii.aide.movetogether.Views;

import it.unipi.dii.aide.movetogether.Controllers.Admin.AdminController;
import it.unipi.dii.aide.movetogether.Controllers.Host.HostController;
import it.unipi.dii.aide.movetogether.Controllers.User.CommentPageController;
import it.unipi.dii.aide.movetogether.Controllers.User.ModifyActivityController;
import it.unipi.dii.aide.movetogether.Controllers.User.UserController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/*
Gestione centralizzata di tutta la GUI della applicazione
 */
public class ViewFactory {
    private AccountType loginAccountType;
    private FindType findType;
    //Client views
    //      Questo è un flag che cambia nel tempo e segnala a controller diversi
    //      Cambia in accordo al event listener inizializzato nel controller user_parent
    private final ObjectProperty<UserMenuOptions> userMenuOptions;


    private AnchorPane homeView;
    private AnchorPane dashboardView;
    private AnchorPane findView;
    private AnchorPane createPathView;
    private AnchorPane pathView;
    private AnchorPane userProfileView;
    private AnchorPane activityView;



    //Admin views
    private final ObjectProperty<AdminMenuOptions> adminMenuOptions;

    //Host views
    private final ObjectProperty<HostMenuOptions> hostMenuOptions;
    private AnchorPane admin_FindView;
    private AnchorPane admin_userProfileView;

    public ViewFactory(){
        loginAccountType = AccountType.USER;
        findType = FindType.USER;
        this.userMenuOptions = new SimpleObjectProperty<>();
        this.adminMenuOptions = new SimpleObjectProperty<>();
        this.hostMenuOptions = new SimpleObjectProperty<>();
    }

    public AccountType getLoginAccountType() {
        return loginAccountType;
    }

    public void setLoginAccountType(AccountType loginAccountType) {
        this.loginAccountType = loginAccountType;
    }

    public FindType getFindType() {return findType;}
    public void setFindType(FindType findType){this.findType = findType;}

    public ObjectProperty<UserMenuOptions> getUserMenuOptions() {
                return userMenuOptions;
    }

    public ObjectProperty<AdminMenuOptions> getAdminMenuOptions() {
        return adminMenuOptions;
    }

    public ObjectProperty<HostMenuOptions> getHostMenuOptions() {
        return hostMenuOptions;
    }

    public void showLoginWindow(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/Fxml/Login.fxml")));
        createStage(loader);
    }

    public void showRegistrationWindow(){
        FXMLLoader loader = new FXMLLoader((getClass().getResource("/Fxml/Registration.fxml")));
        createStage(loader);
    }

    public void showUserWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/UserGeneral.fxml"));
        UserController userController = new UserController();
        loader.setController(userController);
        createStage(loader);
    }

    public void showHostWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Host/HostGeneral.fxml"));
        HostController hostController = new HostController();
        loader.setController(hostController);
        createStage(loader);
    }

    public void showAdminWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/AdminGeneral.fxml"));
        AdminController adminController = new AdminController();
        loader.setController(adminController);
        createStage(loader);
    }

    public void showModifyActivityWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/ModifyActivityPage.fxml"));
        ModifyActivityController controller = new ModifyActivityController();
        loader.setController(controller);
        createStage(loader);
    }

    public void showCommentWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/CommentPage.fxml"));
        CommentPageController controller = new CommentPageController();
        loader.setController(controller);
        createStage(loader);
    }


    private void createStage(FXMLLoader loader){
        Scene scene = null;
        try{
            scene = new Scene(loader.load());
        } catch (Exception e){
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/icon.png"))));
        stage.setResizable(false);
        stage.setTitle("MoveTogether");
        stage.show();
    }

    public void closeLoginWindow(Stage stage){
        stage.close();
    }
    public void closeRegistrationWindow(Stage stage) {stage.close();}
    public void closeStage(Stage stage) {stage.close();}

    public AnchorPane getHomeView(){
        if(homeView == null){
            try {
                homeView = new FXMLLoader(getClass().getResource("/Fxml/User/Home.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return homeView;
    }

    public AnchorPane getDashboardView(){
        //if(dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource("/Fxml/User/UserDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
         //}
        return dashboardView;
    }

    public AnchorPane getFindView(){
        if(findView == null){
            try {
                findView = new FXMLLoader(getClass().getResource("/Fxml/User/FindPage.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return findView;
    }

    public AnchorPane getAddActivityView(){
        if(activityView == null){
            try {
                activityView = new FXMLLoader(getClass().getResource("/Fxml/User/Activity.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return activityView;
    }


    //Da correggere e capire se fare un addPathView e addActivityView separati!!
    public AnchorPane getCreatePathView(){
        if(createPathView == null){
            try {
                createPathView = new FXMLLoader(getClass().getResource("/Fxml/User/CreatePath.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return createPathView;
    }

    public AnchorPane getPathView(){
        //if(pathView == null){
            try {
                pathView = new FXMLLoader(getClass().getResource("/Fxml/User/PathPage.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
        return pathView;
    }

    public AnchorPane getUserProfileView(){
        //if(pathView == null){
        try {
            userProfileView = new FXMLLoader(getClass().getResource("/Fxml/User/MoverProfile.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        return userProfileView;
    }



    //Host view
    public AnchorPane getFindView_host(){
        //if(host_findView == null){
            try {
                findView = new FXMLLoader(getClass().getResource("/Fxml/Host/FindPage.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
        return findView;
    }


    public AnchorPane getFindView_admin(){
        //if(host_findView == null){
        try {
            findView = new FXMLLoader(getClass().getResource("/Fxml/Admin/FindPage.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        return findView;
    }


    public AnchorPane getUserProfileView_host(){
        //if(pathView == null){
        try {
            userProfileView = new FXMLLoader(getClass().getResource("/Fxml/Host/MoverProfile.fxml")).load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        return userProfileView;
    }

    public AnchorPane getHomeView_host(){
        if(homeView == null){
            try {
                homeView = new FXMLLoader(getClass().getResource("/Fxml/Host/Home.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return homeView;
    }

}
