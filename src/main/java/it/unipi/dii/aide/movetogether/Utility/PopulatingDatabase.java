package it.unipi.dii.aide.movetogether.Utility;
import com.github.javafaker.Faker;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PopulatingDatabase {

    private PopulatingDatabase popdb;

    /*
    public PopulatingDatabase(){
        this.popdb = this;
    }

     */



    public void run(){
        MongoClient mongoClient;
        String mongoPort = "27017";
        MongoDatabase database = null;
        int numberOfUsers = 5; // Numero di documenti utente da generare
        int numberOfPaths = 20;

        ConnectionString uri = new ConnectionString("mongodb://localhost:"+mongoPort);
        try {
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("mydb");
            System.out.println("**** MongoDB Driver Invocato ****");
        }
        catch(Exception e){
            e.printStackTrace();
        }

        Faker faker = new Faker();
        //List<Document> userDocuments = new ArrayList<>();
        MongoCollection<Document> usersCollection = database.getCollection("users");

        //String out = faker.name().fullName();
        for (int i = 0; i < numberOfUsers; i++) {
            Document user = new Document()
                    //.append("name",faker.name().fullName())
                    //.append("username",faker.name().username())
                    //.append("nickname",faker.pokemon().name())
                    .append("age",faker.number().numberBetween(18, 60))
                    .append("data_birth",faker.date().birthday())
                    .append("email",faker.internet().emailAddress())
                    .append("password",faker.internet().password())
                    .append("city",faker.address().city())
                    .append("country",faker.address().country())
                    .append("faker",1);

            usersCollection.insertOne(user);

        }

    }

}
