package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Comment;
import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class CommentCellController implements Initializable {
  public Label name_lbl;
  public Label surname_lbl;
  public Label country_lbl;
  public Label title_lbl;
  public Label score_lbl;
  public TextArea content_textarea;
  public Label date_lbl;


    private final Comment comment;

    public CommentCellController(Comment comment){
        this.comment = comment;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dentro initialize di CommentCellController()");
        content_textarea.setWrapText(true);
        Date date = comment.commentTimestampProperty().getValue();
        String pattern = "MM/dd/yyyy-HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String stringDate = simpleDateFormat.format(date);
        date_lbl.textProperty().bind(new SimpleStringProperty(stringDate));

        name_lbl.textProperty().bind(comment.nameProperty());
        surname_lbl.textProperty().bind(comment.surnameProperty());
        country_lbl.textProperty().bind(comment.countryProperty());
        title_lbl.textProperty().bind(comment.titleProperty());
        content_textarea.textProperty().bind(comment.contentProperty());
        score_lbl.textProperty().bind(new SimpleStringProperty(comment.scoreProperty().getValue() + "/5"));

    }


}