package it.unipi.dii.aide.movetogether.Controllers.Host;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.HostMenuOptions;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HostMenuController implements Initializable {
    public Button home_btn;
    public Button find_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
                addListeners();
    }

    private void addListeners(){
        home_btn.setOnAction(event -> onHome());
        find_btn.setOnAction(event -> onFind());
        logout_btn.setOnAction(event -> onLogout());
    }
    private void onHome(){
        Model.getInstance().getViewFactory().getHostMenuOptions().set(HostMenuOptions.HOME);
    }
    private void onFind(){
        Model.getInstance().getViewFactory().getHostMenuOptions().set(HostMenuOptions.FIND);
    }

    private void onLogout(){

        Stage stage = (Stage) home_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().setUserLoginSuccessFlag(false);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

}
