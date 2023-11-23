package it.unipi.dii.aide.movetogether.Views;

import it.unipi.dii.aide.movetogether.Controllers.User.PathCellController;
import it.unipi.dii.aide.movetogether.Models.Path;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class PathCellFactory extends ListCell<Path> {

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/User/PathCell.fxml"));
            PathCellController controller = new PathCellController(path);
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

