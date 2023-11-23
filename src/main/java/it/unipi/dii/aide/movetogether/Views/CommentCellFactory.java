package it.unipi.dii.aide.movetogether.Views;

import it.unipi.dii.aide.movetogether.Controllers.User.CommentCellController;
import it.unipi.dii.aide.movetogether.Controllers.User.PathCellController;
import it.unipi.dii.aide.movetogether.Models.Comment;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class CommentCellFactory extends ListCell<Comment> {

@Override
    protected void updateItem(Comment comment, boolean empty){
        System.out.println("Dentro updateItem di CommentCellFactory()");
        super.updateItem(comment,empty);
        if(empty){
            setText(null);
            setGraphic(null);
            System.out.println("Dentro CommentCellFactory - parte empty");
        } else{
            System.out.println("Dentro CommentCellFactory");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/CommentCell.fxml"));
            CommentCellController controller = new CommentCellController(comment);
            loader.setController(controller);
            setText(null);
            try{
                setGraphic(loader.load());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

