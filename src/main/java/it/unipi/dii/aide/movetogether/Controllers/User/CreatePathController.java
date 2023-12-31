package it.unipi.dii.aide.movetogether.Controllers.User;

/*
import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.MapComponentInitializedListener;
import com.dlsc.gmapsfx.javascript.object.DirectionsPane;
import com.dlsc.gmapsfx.javascript.object.GoogleMap;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import com.dlsc.gmapsfx.javascript.object.MapOptions;
import com.dlsc.gmapsfx.javascript.object.MapTypeIdEnum;
import com.dlsc.gmapsfx.service.directions.DirectionStatus;
import com.dlsc.gmapsfx.service.directions.DirectionsRenderer;
import com.dlsc.gmapsfx.service.directions.DirectionsRequest;
import com.dlsc.gmapsfx.service.directions.DirectionsResult;
import com.dlsc.gmapsfx.service.directions.DirectionsService;
import com.dlsc.gmapsfx.service.directions.DirectionsServiceCallback;
import com.dlsc.gmapsfx.service.directions.TravelModes;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class CreatePathController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    public DirectionsService directionsService;
    public DirectionsPane directionsPane;

    public StringProperty from = new SimpleStringProperty();
    public StringProperty to = new SimpleStringProperty();
    public DirectionsRenderer directionsRenderer = null;

    @FXML
    public GoogleMapView mapView;

    @FXML
    public TextField fromTextField;

    @FXML
    public TextField toTextField;

    @FXML
    public void toTextFieldAction(ActionEvent event) {
        DirectionsRequest request = new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING, true);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }

    @FXML
    public void clearDirections(ActionEvent event) {
        directionsRenderer.clearDirections();
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInitializedListener(this);
        to.bindBidirectional(toTextField.textProperty());
        from.bindBidirectional(fromTextField.textProperty());
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        options.center(new LatLong(47.606189, -122.335842))
                .zoomControl(true)
                .zoom(12)
                .overviewMapControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);
        GoogleMap map = mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }

}*/