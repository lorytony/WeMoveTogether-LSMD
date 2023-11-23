package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Activity;
import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.ActivityCellFactory;
import it.unipi.dii.aide.movetogether.Views.CommentCellFactory;
import it.unipi.dii.aide.movetogether.Views.FriendCellFactory;
import javafx.beans.Observable;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PathController implements Initializable {
    public Path current_path;
    public Label namePath_lbl;
    public Label distance_lbl;
    public Label altitudeVariation_lbl;
    public Label avgScore_lbl;
    public Label performance_lbl;
    public Label nameDiscover_lbl;
    public ListView friends_listview;
    public ListView seniors_listview;
    public ListView comments_listview;
    public ImageView path_imageview;
    public Button comment_btn;
    public Label description_lbl;
    public User moverLogged;
    public String usrId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();

        comment_btn.setOnAction(event ->onComment());


        friends_listview.setItems(Model.getInstance().getFriends());
        friends_listview.setCellFactory(e -> new FriendCellFactory());

        //DA SISTEMARE - METODO INVOCATO NON CORRETTO
        //non è da chiamare getSuggestedFriends() ma quello idoneo
        seniors_listview.setItems(Model.getInstance().getSuggestedFriends());
        seniors_listview.setCellFactory(e -> new FriendCellFactory());

        comments_listview.setItems(Model.getInstance().getComments());
        comments_listview.setCellFactory(e -> new CommentCellFactory());

    }

    public void initData(){
        int min = 1;
        int max = 3;
        int rn = (int)Math.floor(Math.random() * (max - min + 1) + min);

        Image img = new Image("file:src/main/resources/Images/path" + Integer.toString(rn) + ".jpg",512, 512, false, false);
        path_imageview.setImage(img);
        //path_imageview.setFitHeight(512);
        //path_imageview.setFitWidth(512);
        moverLogged = Model.getInstance().getUserLogged();
        usrId = Model.getInstance().getUserLoggedId();
        String pathId = Model.getInstance().getPathId();


        //Non vanno bene tutte queste invocazioni, ci dovrebbe essere un metodo in Model tipo
        //String pathId = Model.getInstance().getPathId();
        //Path path = Model.getInstance().getInfoPath(pathId);
        Path path = Model.getInstance().getDatabaseDriver().getPathDetail(Model.getInstance().getPathId());
        namePath_lbl.textProperty().bind(path.descriptionProperty());
        SimpleStringProperty var = new SimpleStringProperty(path.nameProperty().getValue() + "CIAONE");
        namePath_lbl.textProperty().bind(path.nameProperty());
        distance_lbl.textProperty().bind(path.distanceProperty().asString());
        altitudeVariation_lbl.textProperty().bind(path.altitudeVariationProperty().asString());
        avgScore_lbl.textProperty().bind(path.avgScoreProperty().asString());

        String typeActivity = "running";
        float result = Model.getInstance().getUserStatPerformance(usrId,pathId,typeActivity);

        String text1 = "Your performance is better than " + String.format("%.2f", result*100) + "% of movers in the community. ";
        String text2 = "You haven't done any activity here";
        SimpleStringProperty performanceText;
        if(result>0.0)
            performanceText = new SimpleStringProperty(text1);
        else {
            performanceText = new SimpleStringProperty(text2);
            comment_btn.setDisable(true);
        }

        performance_lbl.textProperty().bind(performanceText);
        nameDiscover_lbl.textProperty().bind(path.nameCreatorProperty());


        System.out.println("Dentro PathController");
        //if(Model.getInstance().getFriends().isEmpty()){
            Model.getInstance().setFriends();
        //}
        System.out.println("Prima di  getSuggestedFriends");
        //if(Model.getInstance().getSuggestedFriends().isEmpty()){
            Model.getInstance().setSuggestedFriends();
        //}

        //if(Model.getInstance().getComments().isEmpty()){
            Model.getInstance().setComments();
        //}
    }

    public void onComment(){
        Model.getInstance().getViewFactory().showCommentWindow();
    }
}
