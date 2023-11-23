package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Activity;
import it.unipi.dii.aide.movetogether.Models.Comment;
import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.User;
import it.unipi.dii.aide.movetogether.Views.AccountType;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CommentPageController implements Initializable {



    public TextField title_fld;
    public TextArea content_textarea;
    public ChoiceBox<Integer> scoreSelector_box;
    public Button comment_btn;
    public int score;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
        scoreSelector_box.getItems().addAll(1,2,3,4,5);
        scoreSelector_box.valueProperty().addListener(observable -> updateScore());
        scoreSelector_box.getSelectionModel().selectLast();
        comment_btn.setOnAction(event ->onComment());


    }

    public void initData(){

       // activity = Model.getInstance().getActivityToModify();



    }

    public void updateScore(){
        this.score = scoreSelector_box.getValue();
        System.out.println("valore preso di score: " + Integer.toString(score));
    }

    public void onComment(){

        String usrId = Model.getInstance().getUserLoggedId();
        System.out.println("Vediamo profileId: " + usrId);
        ObjectId id_user = new ObjectId(usrId);
        User user = Model.getInstance().getDatabaseDriver().getUserDetails(usrId);
        String name = user.nameProperty().getValue();
        String surname = user.surnameProperty().getValue();
        String title = title_fld.getText();
        String content = content_textarea.getText();
        String country = "Italy(Fixed)";
        Date comment_timestamp = new Date(); //Dovrebbe fare automaticamnte Date.NOW()
        //String pattern = "MM/dd/yyyy-HH:mm";
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //String stringDate = simpleDateFormat.format(comment_timestamp);

        Comment com = new Comment(id_user,name,surname,score, content,country,title,comment_timestamp);

        String pathId = Model.getInstance().getPathId();
        Model.getInstance().getDatabaseDriver().addComment(pathId,com);
        //Modo temporaneo per obbligare il refresh della pagina, ci sarà sicuramente un modo migliore
        Model.getInstance().setComments();
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.CREATE_PATH);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.PATH);
        onClose();
    }


    public void onClose(){
        Stage stage = (Stage) title_fld.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
    }
}
