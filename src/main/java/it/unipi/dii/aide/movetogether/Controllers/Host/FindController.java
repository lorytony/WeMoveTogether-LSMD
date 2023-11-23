package it.unipi.dii.aide.movetogether.Controllers.Host;


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
    public Label name_lbl;
    public Label location_lbl;
    public TextField name_fld;
    public TextField location_fld;
    public ListView users_listview;
    public Button find_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        find_btn.setOnAction(event ->onFind());
    }



    public void onFind(){

            String nameUser = name_fld.getText();
            System.out.println("Name passato: " + nameUser);
            String city = location_fld.getText();
            Model.getInstance().findUsers(nameUser,city);
            if(Model.getInstance().getFoundUsers() != null) {
                users_listview.getItems().clear();
                users_listview.refresh();
                users_listview.setItems(Model.getInstance().getFoundUsers());
                users_listview.setCellFactory(e -> new FriendCellFactory());
                users_listview.refresh();
                error_lbl.setText("");
            }else {
                error_lbl.setText("Error: users not found!");
            }
        }

    }

