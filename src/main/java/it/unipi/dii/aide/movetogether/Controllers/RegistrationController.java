package it.unipi.dii.aide.movetogether.Controllers;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable{
    public TextField name_fld;
    public TextField username_fld;
    public TextField password_fld;
    public TextField city_fld;
    public TextField country_fld;
    public DatePicker date_picker;
    public Button register_btn;
    public Button back_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        back_btn.setOnAction(event -> onLogin());
        register_btn.setOnAction(event-> onRegistration());
    }
    public void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeRegistrationWindow(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        }
    public void onRegistration(){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(date_picker.getValue().atStartOfDay(defaultZoneId).toInstant());
        //Purtroppo la classe Document di MongoDB usa la classe Data che è deprecata in Java
        User user = new User(new ObjectId("00"),name_fld.getText(),username_fld.getText(),password_fld.getText(),18,date,null,null,null,null,null);
        Model.getInstance().getDatabaseDriver().insertUser(user);
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeRegistrationWindow(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    }










