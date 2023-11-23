package it.unipi.dii.aide.movetogether.Controllers.Admin;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Models.Path;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PathCellController_admin implements Initializable {
    public Button infoPath_btn;
    public Label namePath_lbl;
    public Label location_lbl;
    public Label distance_lbl;
    public Label indicativeDuration_lbl;
    public Label avgRate_lbl;
    public Label discoverName_lbl;
    public Label date_lbl;
    public String pathId;

    private final Path path;

    public PathCellController_admin(Path path){
        this.path = path;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dentro initialize di PathCellController()");
        infoPath_btn.setOnAction(event -> onPath());
        pathId = path.idPathProperty().getValue().toString();
        System.out.println("PathId ottenuto è (getValue): " + path.idPathProperty().getValue());
        System.out.println("PathId ottenuto è (toString): " + pathId);

        //Date date = path.dateProperty().getValue();
        //String pattern = "MM/dd/yyyy-HH:mm:ss";
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        //String stringDate = simpleDateFormat.format(date);
        //date_lbl.textProperty().bind(new SimpleStringProperty(stringDate));

        namePath_lbl.textProperty().bind(path.nameProperty());
        System.out.println("Nome del path ottenuto è: " + path.nameProperty().getValue());
        SimpleStringProperty var = new SimpleStringProperty(path.nameProperty().getValue() + "CIAONE");
        distance_lbl.textProperty().bind(new SimpleStringProperty(path.distanceProperty().getValue() + "km"));
        indicativeDuration_lbl.textProperty().bind(new SimpleStringProperty(path.indicativeDurationProperty().getValue() + "min"));
        discoverName_lbl.textProperty().bind(path.nameCreatorProperty());
        avgRate_lbl.textProperty().bind(new SimpleStringProperty(path.avgScoreProperty().getValue() + "/5"));

    }

    public void onPath(){
        System.out.println("Dentro onPath vado a settare il seguente path_id: " + pathId);
        Model.getInstance().setPathId(pathId);
        Model.getInstance().getViewFactory().getUserMenuOptions().set(UserMenuOptions.PATH);
    }
}