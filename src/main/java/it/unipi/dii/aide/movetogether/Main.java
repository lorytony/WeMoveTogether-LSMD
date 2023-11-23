package it.unipi.dii.aide.movetogether;

import it.unipi.dii.aide.movetogether.Models.DatabaseDriver;
import it.unipi.dii.aide.movetogether.Utility.DistributingIds;
import it.unipi.dii.aide.movetogether.Views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("MAAAAAAAIN");
        /* esperimenti veloci
        DatabaseDriver databaseDriver = new DatabaseDriver(); //PAY ATTENTION is yet defined in Model.class!!
        User user = databaseDriver.getUserDetail("64aacd61b827175ac271ac03");
        System.out.println(user.toString());
        Path path = databaseDriver.getPathDetail("64ae8c9bc2aaecba1a86766c");
        System.out.println(path.toString());
        List<Activity> activities = databaseDriver.getLastActivities(5,0);
        System.out.println(activities.toString());

        List<Path> paths = databaseDriver.getTrendingPaths(5);
        databaseDriver.getFriends("64aacd61b827175ac271ac03");
                 */


        System.out.println("TEST SCRIPT ASSOCIATING ACTIVITIES TO USERS");
        DistributingIds ass = new DistributingIds();
        //ass.distributeIdsOnMongoDB();
        //ass.createRelationsOnNeo4j();

        System.out.println("TEST getNumberActivitiesLastPeriod");
        DatabaseDriver databaseDriver = new DatabaseDriver();
        //databaseDriver.testWithIndex();
        //databaseDriver.test_userPerformance();


        databaseDriver.getNumberActivitiesLastPeriodByUser("64fdd2fb0881223807a31bfa",3);
        databaseDriver.getNumberActivitiesPraticedByUser("64fdd2fb0881223807a31bfa");
        databaseDriver.getMostFrequentActivityTypeByUser("64fdd2fb0881223807a31bfa");
        databaseDriver.getNumberKcalsBurnedLastPeriod("64fdd2fb0881223807a31bfa",3);

        //Codice della applicazione
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showLoginWindow();
        System.out.println(System.getProperties());

    }

    public static void main(String[] args) {
        launch();
    }
}