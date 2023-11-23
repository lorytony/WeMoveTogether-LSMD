package it.unipi.dii.aide.movetogether.Views;

import it.unipi.dii.aide.movetogether.Controllers.User.ActivityCellController;
import it.unipi.dii.aide.movetogether.Models.Activity;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class ActivityCellFactory extends ListCell<Activity> {

    @Override
    protected void updateItem(Activity activity, boolean empty){
        System.out.println("Dentro updateItem di ActivityCellFactory()");
        super.updateItem(activity,empty);
        if(empty){
            setText(null);
            setGraphic(null);
            System.out.println("Dentro ActivityCellFactory - parte empty");
        } else{
            System.out.println("Dentro ActivityCellFactory");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/ActivityCell.fxml"));
            ActivityCellController controller = new ActivityCellController(activity);
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
