package it.unipi.dii.aide.movetogether.Controllers.User;

import it.unipi.dii.aide.movetogether.Models.Model;
import it.unipi.dii.aide.movetogether.Views.UserMenuOptions;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public BorderPane user_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Questo listener ascolta se varia il valore della variabile userSelectedMenuItem;
        //questo valore cambia in accordo a quale bottone viene pigiato; Il UserMenuController ha un listener per ogni bottone
        //appena un bottone viene pigiato viene modificato il valore di userselectedmenuItem nel ViewFactory. Che a sua volta
        // innesca il listener del UserController.
        //Praticamente la variabile selectedMenutItem fa da ponte tra due controller.
        Model.getInstance().getViewFactory().getUserMenuOptions().addListener(((observableValue, oldVal, newVal) -> {
            switch (newVal){
                case HOME -> user_parent.setCenter((Model.getInstance().getViewFactory().getHomeView()));
                case DASHBOARD -> user_parent.setCenter((Model.getInstance().getViewFactory().getDashboardView()));
                case FIND -> user_parent.setCenter((Model.getInstance().getViewFactory().getFindView()));
                case ADD_ACTIVITY -> user_parent.setCenter((Model.getInstance().getViewFactory().getAddActivityView()));
                case CREATE_PATH -> user_parent.setCenter((Model.getInstance().getViewFactory().getCreatePathView()));
                case USER_PROFILE -> user_parent.setCenter((Model.getInstance().getViewFactory().getUserProfileView()));
               //case LOGOUT -> user_parent.setCenter((Model.getInstance().getViewFactory().getLogoutView()));
                case PATH -> user_parent.setCenter((Model.getInstance().getViewFactory().getPathView()));
                default ->  user_parent.setCenter((Model.getInstance().getViewFactory().getHomeView()));
            }
        }));
    }
}
