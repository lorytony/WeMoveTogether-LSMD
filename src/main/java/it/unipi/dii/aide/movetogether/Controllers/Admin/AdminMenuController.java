package it.unipi.dii.aide.movetogether.Controllers.Admin;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.AdminMenuOptions;
import it.unipi.dii.aide.movetogether.Views.HostMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    public Button find_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
                addListeners();
    }

    private void addListeners(){

        find_btn.setOnAction(event -> onFind());
        logout_btn.setOnAction(event -> onLogout());
    }

    private void onFind(){
        Model.getInstance().getViewFactory().getAdminMenuOptions().set(AdminMenuOptions.FIND);
    }

    private void onLogout(){

        Stage stage = (Stage) find_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().setUserLoginSuccessFlag(false);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

}
