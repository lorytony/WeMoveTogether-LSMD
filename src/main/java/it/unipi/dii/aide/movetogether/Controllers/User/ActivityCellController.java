package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Activity;
import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ActivityCellController implements Initializable {
    public Button infoPath_btn;
    public Button modifyActivity_btn;
    public Button deleteActivity_btn;
    public Label date_lbl;
    public Label nameActivity_lbl;
    public Label namePath_lbl;
    public Label typeActivity_lbl;
    public Label distance_lbl;
    public Label timeElapsed_lbl;
    public Label kcal_lbl;
    public String activityId;
    public String pathId;

    private final Activity activity;

    public ActivityCellController(Activity activity){
        this.activity = activity;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dentro initialize di ActivityCellController()");
        infoPath_btn.setOnAction(event -> onPath());
        modifyActivity_btn.setOnAction(event -> onModify());
        deleteActivity_btn.setOnAction(event -> onDelete());
        activityId = activity.idActivityProperty().getValue().toString();



        pathId = activity.idPathProperty().getValue().toString();
        System.out.println("PathId ottenuto è: " + pathId);

        Date date = activity.dateProperty().getValue();
        String pattern = "yyyy/MM/dd-HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringDate = simpleDateFormat.format(date);
        date_lbl.textProperty().bind(new SimpleStringProperty(stringDate));
        nameActivity_lbl.textProperty().bind(activity.nameProperty());
        namePath_lbl.textProperty().bind(activity.namePathProperty());
        typeActivity_lbl.textProperty().bind(activity.typeActivityProperty());


        distance_lbl.textProperty().bind(new SimpleStringProperty(activity.distanceProperty().getValue() + " km"));
        timeElapsed_lbl.textProperty().bind(new SimpleStringProperty(activity.timeElapsedProperty().getValue() + " min."));
        kcal_lbl.textProperty().bind(new SimpleStringProperty(activity.kcalProperty().getValue() + "kcal"));
    }

    public void onPath(){
        Model.getInstance().setPathId(pathId);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.PATH);
    }

    public void onModify(){
        Model.getInstance().setActivity(activity);
        Model.getInstance().getViewFactory().showModifyActivityWindow();
    }

    public void onDelete(){
        Model.getInstance().deleteActivity(activityId);
        Model.getInstance().setActivities();
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.DASHBOARD);
    }
}