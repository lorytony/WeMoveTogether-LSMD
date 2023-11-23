package it.unipi.dii.aide.movetogether.Controllers.Admin;

import it.unipi.dii.aide.movetogether.Controllers.User.PathCellController;
import it.unipi.dii.aide.movetogether.Models.Path;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class PathCellFactory_admin extends ListCell<Path> {

@Override
    protected void updateItem(Path path, boolean empty){
        System.out.println("Dentro updateItem di PathCellFactory()");
        super.updateItem(path,empty);
        if(empty){
            setText(null);
            setGraphic(null);
            System.out.println("Dentro PathCellFactory - parte empty");
        } else{
            System.out.println("Dentro PathCellFactory");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/PathCell.fxml"));
            PathCellController_admin controller = new PathCellController_admin(path);
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

