package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import it.unipi.dii.aide.movetogether.Views.PathCellFactory;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MoverProfileController implements Initializable {
    public User moverProfile;
    public Label name_lbl;
    public Label messageFriend_lbl;
    public Button friend_btn;
    public Label nameSurnameYears_lbl;
    public Label cityCountry_lbl;
    public Label registeredDate_lbl;
    public Label typeActivity_lbl;
    public Label numberAllActivity_lbl;
    public Label numberActivityRecently_lbl;
    public Label kcalBurnedRecently_lbl;
    public Label mostPraticedPath_lbl;

    public ListView mutualFriends_listview;
    public ListView mutualLikedPaths_listview;
    public ListView discoveredPaths_listview;

    public ImageView userPhoto_imageView;
    public ImageView mostPracticedPath_imageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dentro MoverProfileController");
        initData();



        mutualFriends_listview.setItems(Model.getInstance().getMutualFriends());
        mutualFriends_listview.setCellFactory(e -> new FriendCellFactory());

        mutualLikedPaths_listview.setItems(Model.getInstance().getMutualLikedPaths());
        mutualLikedPaths_listview.setCellFactory(e -> new PathCellFactory());

        discoveredPaths_listview.setItems(Model.getInstance().getDiscoveredPaths());
        discoveredPaths_listview.setCellFactory(e -> new PathCellFactory());


    }

    public void initData(){

        //NON VANNO BENE TUTTE QUESTE CHIAMATE da rivedere
        this.moverProfile = Model.getInstance().getDatabaseDriver().getUserDetails(Model.getInstance().getMoverId());

        String imageProfilePath = moverProfile.imageProfileProperty().getValue();
        Image profileImage;
        //Casual image profile has "https://randomuser.me/api/portraits/women/1.jpg"

        try {
            profileImage = new Image(imageProfilePath,77, 77, false, false);
        }catch (NullPointerException | IllegalArgumentException e){
            profileImage = new Image("file:src/main/resources/Images/profilePhotoNotLoaded.jpg",25, 25, false, false);
        }

        userPhoto_imageView.setImage(profileImage);




        String nameSurnameAge = moverProfile.nameProperty().getValue() + " "
                + moverProfile.surnameProperty().getValue() + ","
                + moverProfile.ageProperty().getValue().toString() + " years";
        SimpleStringProperty nsa = new SimpleStringProperty(nameSurnameAge);
        nameSurnameYears_lbl.textProperty().bind(nsa);

        String cityCountry = moverProfile.cityProperty().getValue() + ", " + moverProfile.countryProperty().getValue();
        SimpleStringProperty cc = new SimpleStringProperty(cityCountry);
        cityCountry_lbl.textProperty().bind(cc);

        Date date = moverProfile.dateProperty().getValue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String stringDate = simpleDateFormat.format(date);
        String dateFormatted = "Date of birth: " + stringDate;
        SimpleStringProperty df = new SimpleStringProperty(dateFormatted);
        registeredDate_lbl.textProperty().bind(df);


        SimpleStringProperty friendshipMessage = new SimpleStringProperty("He/She is your friend!");
        String gender = moverProfile.genderProperty().getValue();
        if(gender.equals("male")){
            friendshipMessage = new SimpleStringProperty("He is your friend!");
        }else if(gender.equals("female")){
            friendshipMessage = new SimpleStringProperty("She is your friend!");
        }
        else{
            friendshipMessage = new SimpleStringProperty("This mover is your friend!");
        }

        boolean isFriend = Model.getInstance().checkFriendship(Model.getInstance().getMoverId());
        System.out.println("Dentro MoverProfileController - isFriend value: " + isFriend);

        if(isFriend){
            messageFriend_lbl.textProperty().bind(friendshipMessage);
            friend_btn.setVisible(false);
        }
        else{
            messageFriend_lbl.setVisible(false);
            friend_btn.setVisible(true);
        }




        String mvrId = Model.getInstance().getMoverId();
        //TO DO -- non vanno bene queste chiamate getDatabaseDriver().metodo...
        int numbActRecently = Model.getInstance().getDatabaseDriver().getNumberActivitiesLastPeriodByUser(mvrId,3);
        int numbAllActivities = Model.getInstance().getDatabaseDriver().getNumberActivitiesPraticedByUser(mvrId);
        String typeActivity = Model.getInstance().getDatabaseDriver().getMostFrequentActivityTypeByUser(mvrId);
        int kcalBurnedRecently = Model.getInstance().getDatabaseDriver().getNumberKcalsBurnedLastPeriod(mvrId,3);

        typeActivity_lbl.textProperty().bind(new SimpleStringProperty("Prefered type of activity: " + typeActivity));
        numberAllActivity_lbl.textProperty().bind(new SimpleStringProperty("Total activities done: " + Integer.toString(numbAllActivities)));
        numberActivityRecently_lbl.textProperty().bind(new SimpleStringProperty("Activities done last month: " + Integer.toString(numbActRecently)));
        kcalBurnedRecently_lbl.textProperty().bind(new SimpleStringProperty("Kcals burned last month: " + Integer.toString(kcalBurnedRecently)));
        String namePath  = Model.getInstance().getPreferredPath(mvrId);
        mostPraticedPath_lbl.textProperty().bind(new SimpleStringProperty(namePath));



        int min = 1;
        int max = 3;
        int rn = (int)Math.floor(Math.random() * (max - min + 1) + min);

        Image img = new Image("file:src/main/resources/Images/path" + Integer.toString(rn) + ".jpg",225, 180, false, false);
        mostPracticedPath_imageView.setImage(img);

        friend_btn.setOnAction(event ->onFriend());

        //if(Model.getInstance().getMutualFriends().isEmpty()){
            Model.getInstance().setMutualFriends(Model.getInstance().getMoverId());
        //}

        //if(Model.getInstance().getMutualLikedPaths().isEmpty()){
            Model.getInstance().setMutualLikedPaths(Model.getInstance().getMoverId());
        //}

            Model.getInstance().setDiscoveredPathsByMover(Model.getInstance().getMoverId());
    }

    public void onFriend(){
        Model.getInstance().createFriendship(Model.getInstance().getMoverId());
        Model.getInstance().setMutualFriends(Model.getInstance().getMoverId());
        //Modo temporaneo per obbligare il refresh della pagina, ci sarà sicuramente un modo migliore
        //Model.getInstance().setComments();

        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.USER_PROFILE);
    }
}