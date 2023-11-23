package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FriendCellController implements Initializable {
    public Button info_btn;
    public ImageView userPhoto_imageView;
    public Label name_lbl;
    public Label surname_lbl;
    public Label date_lbl;
    public Label age_lbl;
    public String userId;

    private final User friend;

    public FriendCellController(User friend){
        this.friend = friend;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dentro initialize di FriendCellController()");
        userId = friend.idUserProperty().getValue().toString();
        info_btn.setOnAction(event -> onUserProfile());
        name_lbl.textProperty().bind(friend.nameProperty());
        surname_lbl.textProperty().bind(friend.surnameProperty());
        SimpleStringProperty age = new SimpleStringProperty(friend.ageProperty().getValue() + "years");
        age_lbl.textProperty().bind(age);
        String imageProfilePath = friend.imageProfileProperty().getValue();
        Image profileImage;
        //Casual image profile has "https://randomuser.me/api/portraits/women/1.jpg"

        try {
            profileImage = new Image(imageProfilePath,25, 25, false, false);
        }catch (NullPointerException | IllegalArgumentException e){
            profileImage = new Image("file:src/main/resources/Images/profilePhotoNotLoaded.jpg",25, 25, false, false);
        }

        userPhoto_imageView.setImage(profileImage);
        //age_lbl.textProperty().bind(friend.ageProperty().asString());
        //date_lbl.textProperty().bind(friend.dateProperty().asString());
    }

    public void onUserProfile(){
        if(userId.equals(Model.getInstance().getUserLoggedId())){
            Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.DASHBOARD);
        }
        else {
            //Model.getInstance().setUserProfileId(userId); non sicuro di questo codice
            Model.getInstance().setMoverId(userId);
            Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
            Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.USER_PROFILE);
        }
    }
}
