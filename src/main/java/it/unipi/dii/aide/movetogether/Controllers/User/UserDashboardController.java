package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.ActivityCellFactory;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import it.unipi.dii.aide.movetogether.Views.PathCellFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {

    public ImageView imageProfile_imageView;
    public ListView friends_listview;
    public Label userNickname_lbl;
    public Label nameSurname_lbl;
    public Label activityDone_lbl;
    public Label preferredTypeActivity_lbl;
    public Label preferredPath_lbl;
    public Label kcalLastMonth_lbl;
    public Label cityCountry_lbl;
    public Label age_lbl;
    public ListView suggestedFriends_listview;
    public ListView suggestedPaths_listview;
    public ListView lastActivities_listview;
    public User moverLogged;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        friends_listview.setItems(Model.getInstance().getFriends());
        friends_listview.setCellFactory(e -> new FriendCellFactory());

        suggestedFriends_listview.setItems(Model.getInstance().getSuggestedFriends());
        suggestedFriends_listview.setCellFactory(e -> new FriendCellFactory());

        suggestedPaths_listview.setItems(Model.getInstance().getSuggestedPaths());
        suggestedPaths_listview.setCellFactory(e -> new PathCellFactory());

        lastActivities_listview.setItems(Model.getInstance().getLastActivities());
        lastActivities_listview.setCellFactory(e -> new ActivityCellFactory());


    }


    //Ricorda: se non facciamo il controllo se la lista è vuota, ogni volta che facciamo
    // il refresh della pagine andremmo ad aggiungere gli stessi dati nella lista mostrata
    private void initData(){
        System.out.println("Dentro FriendController");

        Model.getInstance().setFriends();
        Model.getInstance().setSuggestedFriends();
        Model.getInstance().setSuggestedPaths();
        Model.getInstance().setActivities();

        moverLogged = Model.getInstance().getUserLogged();



        String imageProfilePath = moverLogged.imageProfileProperty().getValue();
        Image profileImage;
        //Casual image profile has "https://randomuser.me/api/portraits/women/1.jpg"

        try {
            profileImage = new Image(imageProfilePath,75, 75, false, false);

        }catch (NullPointerException | IllegalArgumentException e){
            profileImage = new Image("file:src/main/resources/Images/profilePhotoNotLoaded.jpg",75, 75, false, false);
        }
        imageProfile_imageView.setImage(profileImage);


        SimpleStringProperty helloNickname = new SimpleStringProperty("Hello " + moverLogged.nicknameProperty().getValue());
        userNickname_lbl.textProperty().bind(helloNickname);
        SimpleStringProperty nameSurname = new SimpleStringProperty(moverLogged.nameProperty().getValue() + ", " + moverLogged.surnameProperty().getValue() );
        nameSurname_lbl.textProperty().bind(nameSurname);
        age_lbl.textProperty().bind(new SimpleStringProperty(moverLogged.ageProperty().getValue() + " years old"));
        SimpleStringProperty cityCountry = new SimpleStringProperty(moverLogged.cityProperty().getValue() + ", " + moverLogged.countryProperty().getValue());
        cityCountry_lbl.textProperty().bind(cityCountry);
        String usrId = Model.getInstance().getUserLoggedId();
        int activityDone = Model.getInstance().getTotalActivitiesDone(usrId);
        activityDone_lbl.textProperty().bind(new SimpleStringProperty(Integer.toString(activityDone)));
        String preferredTypeActivity = Model.getInstance().getPreferredTypeActivity(usrId);
        preferredTypeActivity_lbl.textProperty().bind(new SimpleStringProperty(preferredTypeActivity));

        String namePath  = Model.getInstance().getPreferredPath(usrId);
        preferredPath_lbl.textProperty().bind(new SimpleStringProperty(namePath));
        int lastMonths = 3;
        int kcalLastMonth = Model.getInstance().getKcalConsumedLastPeriod(usrId,lastMonths);
        kcalLastMonth_lbl.textProperty().bind(new SimpleStringProperty(Integer.toString(kcalLastMonth) + " kcal/l.month"));





    }
}
