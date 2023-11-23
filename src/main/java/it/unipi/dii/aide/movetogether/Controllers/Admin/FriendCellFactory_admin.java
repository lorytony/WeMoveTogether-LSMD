package it.unipi.dii.aide.movetogether.Controllers.Admin;

import it.unipi.dii.aide.movetogether.Controllers.User.FriendCellController;
import it.unipi.dii.aide.movetogether.Models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class FriendCellFactory_admin extends ListCell<User> {

    @Override
    protected void updateItem(User user, boolean empty){
        System.out.println("Dentro updateItem di FriendCellFactory()");
        super.updateItem(user,empty);
        if(empty){
            setText(null);
            setGraphic(null);
            System.out.println("Dentro FriendCellFactory - parte empty");
        } else{
            System.out.println("Dentro FriendCellFactory");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/FriendCell.fxml"));
            FriendCellController_admin controller = new FriendCellController_admin(user);
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
