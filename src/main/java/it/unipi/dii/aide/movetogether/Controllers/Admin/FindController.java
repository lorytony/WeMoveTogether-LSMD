package it.unipi.dii.aide.movetogether.Controllers.Admin;


import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.ActivityCellFactory;
import it.unipi.dii.aide.movetogether.Views.FindType;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import it.unipi.dii.aide.movetogether.Views.PathCellFactory;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class FindController implements Initializable {
    public ChoiceBox<FindType> searchType_box;
    public Label name_lbl;
    public Label location_lbl;
    public TextField name_fld;
    public TextField minDistance_fld;
    public TextField maxDistance_fld;
    public TextField minDuration_fld;
    public TextField maxDuration_fld;
    public TextField location_fld;
    public DatePicker startDate_picker;
    public DatePicker endDate_picker;
    //public ListView results_listview;
    public ListView paths_listview;
    public ListView users_listview;
    public Button find_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        searchType_box.setItems(FXCollections.observableArrayList(FindType.USER,FindType.PATH));
        find_btn.setOnAction(event ->onFind());
        searchType_box.valueProperty().addListener(observable -> changeFindDescription());
        searchType_box.setValue(Model.getInstance().getViewFactory().getFindType());
    }


    public void changeFindDescription(){
        Model.getInstance().getViewFactory().setFindType((searchType_box.getValue()));
        if(searchType_box.getValue() == FindType.USER){
            minDistance_fld.setVisible(false);
            maxDistance_fld.setVisible(false);
            minDuration_fld.setVisible(false);
            maxDuration_fld.setVisible(false);
            endDate_picker.setVisible(false);
            startDate_picker.setVisible(false);
            name_lbl.setText("Username: ");
            location_lbl.setText("City: ");


        }else if(searchType_box.getValue() == FindType.PATH){
            minDistance_fld.setVisible(true);
            maxDistance_fld.setVisible(true);
            minDuration_fld.setVisible(true);
            maxDuration_fld.setVisible(true);
            endDate_picker.setVisible(true);
            startDate_picker.setVisible(true);
            name_lbl.setText("Name Path:");
            location_lbl.setText("Location: ");

        }

    }

    public void onFind(){
        Model.getInstance().getViewFactory().setFindType((searchType_box.getValue()));
        if(searchType_box.getValue() == FindType.USER){
            String nameUser = name_fld.getText();
            System.out.println("Name passato: " + nameUser);
            String city = location_fld.getText();
            Model.getInstance().findUsers(nameUser,city);
            if(Model.getInstance().getFoundUsers() != null) {
                users_listview.getItems().clear();
                users_listview.refresh();
                users_listview.setItems(Model.getInstance().getFoundUsers());
                users_listview.setCellFactory(e -> new FriendCellFactory_admin());
                users_listview.refresh();
                error_lbl.setText("");
            }else {
                error_lbl.setText("Error: users not found!");
            }
        }

        else if(searchType_box.getValue() == FindType.PATH){
            String namePath = name_fld.getText();
            String minDur = minDuration_fld.getText();
            String maxDur = maxDuration_fld.getText();
            String minDist = minDistance_fld.getText();
            String maxDist = maxDistance_fld.getText();
            String loc = location_fld.getText();
            Model.getInstance().findPaths(namePath,minDist,maxDist,minDur,maxDur,loc);
            paths_listview.getItems().clear();
            paths_listview.refresh();
            paths_listview.setItems(Model.getInstance().getFoundPaths());
            paths_listview.setCellFactory(e -> new PathCellFactory_admin());
            paths_listview.refresh();

        }


    }

}