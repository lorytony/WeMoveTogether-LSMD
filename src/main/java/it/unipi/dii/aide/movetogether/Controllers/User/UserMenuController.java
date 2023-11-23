package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMenuController implements Initializable {
    public Button home_btn;
    public Button dashboard_btn;
    public Button find_btn;
    public Button addActivity_btn;
    public Button createPath_btn;
    public Button logout_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    private void addListeners(){
        home_btn.setOnAction(event -> onHome());
        dashboard_btn.setOnAction(event -> onDashboard());
        find_btn.setOnAction(event -> onFind());
        addActivity_btn.setOnAction(event -> onAddActivity());
        createPath_btn.setOnAction(event -> onCreatePath());
        logout_btn.setOnAction(event -> onLogout());

    }

    private void onHome(){
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.HOME);
    }
    private void onDashboard(){
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.DASHBOARD);
    }
    private void onFind(){
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.FIND);
    }
    private void onAddActivity(){
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.ADD_ACTIVITY);
    }
    private void onCreatePath(){
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
    }
    private void onLogout(){

        Stage stage = (Stage) home_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().setUserLoginSuccessFlag(false);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

}
