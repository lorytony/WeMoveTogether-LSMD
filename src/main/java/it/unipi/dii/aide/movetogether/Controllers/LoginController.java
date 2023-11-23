package it.unipi.dii.aide.movetogether.Controllers;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox<AccountType> accountSelector_box;
    public Label username_lbl;
    public TextField username_fld;
    public Label password_lbl;
    public TextField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Button signin_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        accountSelector_box.setItems(FXCollections.observableArrayList(AccountType.HOST,AccountType.USER,AccountType.ADMIN));
        accountSelector_box.setValue(Model.getInstance().getViewFactory().getLoginAccountType());
        login_btn.setOnAction(event ->onLogin());
        signin_btn.setOnAction(event ->onRegistration());
        accountSelector_box.valueProperty().addListener(observable -> changeLoginDescription());
        //DA RIMUOVERE SERVER SOLO PER NON INSERIRE I DATI A MANO OGNI VOLTA
        username_fld.setText("Lorenzo");
        password_fld.setText("1");
    }

    public void onRegistration(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeLoginWindow(stage);
        Model.getInstance().getViewFactory().showRegistrationWindow();
    }


    public void onLogin(){
        Stage stage = (Stage) error_lbl.getScene().getWindow();

        if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.USER){
            Model.getInstance().evaluateUserCred(username_fld.getText(), password_fld.getText());
            if(Model.getInstance().getUserLoginSuccessFlag()) {
                Model.getInstance().getViewFactory().closeLoginWindow(stage);
                Model.getInstance().getViewFactory().showUserWindow();
            }
            else{
                username_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        }
        else if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.HOST){
            Model.getInstance().getViewFactory().closeLoginWindow(stage);
            Model.getInstance().getViewFactory().showHostWindow();
        }
        else if(Model.getInstance().getViewFactory().getLoginAccountType() == AccountType.ADMIN){
            Model.getInstance().evaluateAdminCred(username_fld.getText(), password_fld.getText());
            if(Model.getInstance().getUserAdminSuccessFlag()) {
                Model.getInstance().getViewFactory().closeLoginWindow(stage);
                Model.getInstance().getViewFactory().showAdminWindow();
            }
            else{
                username_fld.setText("");
                password_fld.setText("");
                error_lbl.setText("No Such Login Credentials.");
            }
        }
    }
    //DUBBIO: Metodo grafico forse sarebbe meglio metterlo nel ViewFactory
    private void changeLoginDescription(){
        Model.getInstance().getViewFactory().setLoginAccountType((accountSelector_box.getValue()));
        if(accountSelector_box.getValue() == AccountType.USER){
            username_lbl.setVisible(true);
            password_lbl.setVisible(true);
            username_fld.setVisible(true);
            password_fld.setVisible(true);
            username_lbl.setText("Username:");
            login_btn.setText("Login");
        }
        else if(accountSelector_box.getValue() == AccountType.ADMIN){
            username_lbl.setVisible(true);
            password_lbl.setVisible(true);
            username_fld.setVisible(true);
            password_fld.setVisible(true);
            username_lbl.setText("AdminID:");
            login_btn.setText("Login");
        }
        else if(accountSelector_box.getValue() == AccountType.HOST){
            username_lbl.setVisible(false);
            password_lbl.setVisible(false);
            username_fld.setVisible(false);
            password_fld.setVisible(false);
            login_btn.setText("Enter");
        }
    }
}
