module it.unipi.dii.aide.movetogether {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    //TO ADD Mongo and Neo4j requires
    // ->
    // ->


    requires com.dlsc.formsfx;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires com.google.gson;
    requires org.neo4j.driver;
    requires javafaker;


    opens it.unipi.dii.aide.movetogether to javafx.fxml, com.google.gson;
    exports it.unipi.dii.aide.movetogether;
    exports it.unipi.dii.aide.movetogether.Controllers;
    exports it.unipi.dii.aide.movetogether.Controllers.Admin;
    exports it.unipi.dii.aide.movetogether.Controllers.User;
    exports it.unipi.dii.aide.movetogether.Controllers.Host;
    exports it.unipi.dii.aide.movetogether.Views;
    exports it.unipi.dii.aide.movetogether.Models;
}