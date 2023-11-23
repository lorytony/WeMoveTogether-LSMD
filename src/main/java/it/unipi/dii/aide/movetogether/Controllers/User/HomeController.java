package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.PathCellFactory;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    public ListView trendPaths_listview;
    public ListView appreciatedPaths_listview;
    public ListView appreciatedDiscovers_listview;
    public Label helloUser_lbl;
    public Text firstSentence_fld;
    public Text secondSentence_fld;
    public ImageView home_imageview;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        trendPaths_listview.setItems(Model.getInstance().getTrendPaths());
        trendPaths_listview.setCellFactory(e -> new PathCellFactory());

        appreciatedPaths_listview.setItems(Model.getInstance().getAppreciatedPaths());
        appreciatedPaths_listview.setCellFactory(e -> new PathCellFactory());

        appreciatedDiscovers_listview.setItems(Model.getInstance().getAppreciatedDiscovers());
        appreciatedDiscovers_listview.setCellFactory(e -> new FriendCellFactory());
    }


    //Ricorda: se non facciamo il controllo se la lista è vuota, ogni volta che facciamo
    // il refresh della pagine andremmo ad aggiungere gli stessi dati nella lista mostrata
    private void initData(){
        System.out.println("Dentro HomeController");
        Image img = new Image("file:src/main/resources/Images/home3.jpg",512, 100, false, false);
        home_imageview.setImage(img);
        home_imageview.setFitHeight(100);
        home_imageview.setFitWidth(1000);


        int numRegUsers = Model.getInstance().getNumberRegisteredUsers();
        int numDiscPaths = Model.getInstance().getNumberDiscoveredPaths();
        User userLogged = Model.getInstance().getUserLogged();
        String nameUser = userLogged.nameProperty().getValue().toString();

        SimpleStringProperty welcome = new SimpleStringProperty("Welcome "+ nameUser +", thank to be part of our growing community");
        SimpleStringProperty firstSentence = new SimpleStringProperty("More than " + numRegUsers + " users are enrolled and many other will add with us.");
        SimpleStringProperty secondSentence = new SimpleStringProperty("More than " + numDiscPaths + " paths have been regitered to be praticated");

        helloUser_lbl.textProperty().bind(welcome);
        firstSentence_fld.textProperty().bind(firstSentence);
        secondSentence_fld.textProperty().bind(secondSentence);

        if(Model.getInstance().getTrendPaths().isEmpty()){
            Model.getInstance().setTrendPaths();
        }
        System.out.println("Prima di  getSuggestedFriends");
        if(Model.getInstance().getAppreciatedPaths().isEmpty()){
            Model.getInstance().setAppreciatedPaths();
        }

        if(Model.getInstance().getAppreciatedDiscovers().isEmpty()){
            Model.getInstance().setAppreciatedDiscovers();
        }

    }
}
