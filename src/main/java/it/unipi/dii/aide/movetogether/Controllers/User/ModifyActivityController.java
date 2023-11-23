package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Activity;
import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Views.CommentCellFactory;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyActivityController implements Initializable {

    public Activity oldActivity;
    public Activity newActivity;
    public TextField name_fld;
    public TextField type_fld;
    public TextField distance_fld;
    public Button modify_btn;
    public Button close_btn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        modify_btn.setOnAction(event ->onModify());
        close_btn.setOnAction(event ->onClose());

    }

    public void initData(){

        oldActivity = Model.getInstance().getActivityToModify();

        //Per ora si modificano solo questi campi di activity
        //valutare se modifcare qualche campo in più
        name_fld.setText(oldActivity.nameProperty().getValue());
        type_fld.setText(oldActivity.typeActivityProperty().getValue());
        distance_fld.setText(oldActivity.distanceProperty().getValue().toString());

    }

    public void onModify(){
        newActivity = oldActivity.copy();
        System.out.println("newActivity:" + newActivity.toString());
        System.out.println("oldActivity:" + oldActivity.toString());

        newActivity.setNameProperty(name_fld.getText());
        newActivity.setTypeProperty(type_fld.getText());
        System.out.println("distance: " + distance_fld.getText());
        newActivity.setDistanceProperty(Integer.valueOf(distance_fld.getText()));

        Model.getInstance().getDatabaseDriver().updateActivity(newActivity);


        Model.getInstance().setActivities();
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.DASHBOARD);


        Stage stage = (Stage) name_fld.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }

    public void onClose(){
        Stage stage = (Stage) name_fld.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
