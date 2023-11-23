package it.unipi.dii.aide.movetogether.Utility;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;


import org.bson.Document;
import org.bson.types.ObjectId;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.internal.InternalNode;
import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.*;


import java.util.*;

public class DistributingIds {
    private MongoClient mongoClient;
    private String mongoPort = "27017";
    private static MongoDatabase database;

    private Driver driver; //neo4j driver
    private String neo4jIp = "localhost";
    private String neo4jPort = "7688";//"7687";
    private String neo4jUsername = "neo4j";
    private String neo4jPassword = "password";//"neo4j";

    public void distributeIdsOnMongoDB(){


        ConnectionString uri = new ConnectionString("mongodb://localhost:27018,localhost:27019,localhost:27020/");

        try {
            MongoClientSettings mcs = MongoClientSettings.builder()
                    .applyConnectionString(uri)
                    .readPreference(ReadPreference.nearest())
                    .retryWrites(true)
                    .writeConcern(WriteConcern.ACKNOWLEDGED).build();
            this.mongoClient = MongoClients.create(mcs);
            this.database = mongoClient.getDatabase("WeMoveTogether");


        }
        catch(Exception e){
            e.printStackTrace();

        }

        MongoCollection<Document> usersCollection = database.getCollection("Users");
        MongoCollection<Document> activitiesCollection = database.getCollection("Activities");
        MongoCollection<Document> pathsCollection = database.getCollection("Paths");

        // Ottieni una lista di tutti gli _id dalla collezione 'users'
        List<Object> userIds = new ArrayList<>();
        List<Object> userNames = new ArrayList<>();
        System.out.println("** DistributingId ** -- raccolta User Ids");
        usersCollection.find().forEach(document -> {
            userIds.add(document.get("_id"));
            userNames.add(document.get("nickname"));
        });

        List<Object> pathIds = new ArrayList<>();
        List<Object> pathNames = new ArrayList<>();
        System.out.println("** DistributingId ** -- raccolta Path Ids");
        pathsCollection.find().forEach(document ->{
            pathIds.add(document.get("_id"));
            pathNames.add(document.get("name"));
        });

        System.out.println("pathNames size: " + pathNames.size());

        System.out.println("stampa ObjectId");
        System.out.println(userIds.get(0)); //stampa '64aacd61b827175ac271ac03'
        System.out.println(userIds.get(0).toString()); //stampa '64aacd61b827175ac271ac03'

        // Crea un oggetto Random per selezionare casualmente gli _id utente
        Random random = new Random();

        // Crea un contatore per tenere traccia delle assegnazioni per ciascun _id utente
        int maxAssignments = 20;
        int[] userAssignments = new int[userIds.size()];

        System.out.println("** DistributingId ** -- inserimento Id_user e id_path alle activity");
        // Aggiungi il campo 'id_user' in modo casuale ai documenti della collezione 'activities'
        activitiesCollection.find().forEach(activityDocument -> {
            int randomIndexUsers,randomIndexPaths;
            Object randomUserId,randomPathId;
            Object randomPathName;
            do {
                randomIndexUsers = random.nextInt(userIds.size());
                randomUserId = userIds.get(randomIndexUsers);
            } while (userAssignments[randomIndexUsers] >= maxAssignments);

            System.out.println("pathIds.size(): " + pathIds.size());
            randomIndexPaths = random.nextInt(pathIds.size());
            System.out.println("randomIndex: " + randomIndexPaths);
            randomPathId = pathIds.get(randomIndexPaths);
            System.out.println("randomPathId: " + randomPathId);
            randomPathName = pathNames.get(randomIndexPaths);
            System.out.println(randomPathName);
            // Assegna l'id utente al documento
            activitiesCollection.updateOne(
                    Filters.eq("_id", activityDocument.get("_id")),
                    new Document("$set", new Document("id_user", randomUserId).append("id_path",randomPathId)
                            .append("name_path",randomPathName)),
                    new UpdateOptions().upsert(true)
            );

            // Incrementa il contatore delle assegnazioni per l'id utente selezionato
            userAssignments[randomIndexUsers]++;
        });


        System.out.println("** DistributingId ** -- inseriemnto id_creator in id_path");
        pathsCollection.find().forEach(pathDocument -> {
            int randomIndex;
            Object randomUserId,randomNickname;
            randomIndex = random.nextInt(userIds.size());
            randomUserId = userIds.get(randomIndex);
            randomNickname = userNames.get(randomIndex);
            pathsCollection.updateOne(
                    Filters.eq("_id", pathDocument.get("_id")),
                    new Document("$set", new Document("id_creator", randomUserId)
                            .append("name_creator",randomNickname)),
                    new UpdateOptions().upsert(true)
            );
        });

    }


    public void createRelationsOnNeo4j(){

        ConnectionString uri = new ConnectionString("mongodb://localhost:27018,localhost:27019,localhost:27020/");

        try {
            MongoClientSettings mcs = MongoClientSettings.builder()
                    .applyConnectionString(uri)
                    .readPreference(ReadPreference.nearest())
                    .retryWrites(true)
                    .writeConcern(WriteConcern.ACKNOWLEDGED).build();
            this.mongoClient = MongoClients.create(mcs);
            this.database = mongoClient.getDatabase("WeMoveTogether");


            System.out.println("Dentro create DatabaseDriver");
        }
        catch(Exception e){
            e.printStackTrace();

        }
        MongoCollection<Document> usersCollection = database.getCollection("Users");
        MongoCollection<Document> pathsCollection = database.getCollection("Paths");


        try {
            driver = GraphDatabase.driver("bolt://" + neo4jIp + ":" + neo4jPort, AuthTokens.basic( neo4jUsername, neo4jPassword));
            driver.verifyConnectivity();
            System.out.println("**** Connected to Neo4j ****");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        try(Session session = driver.session())
        {
            // Recupera tutti gli utenti da MongoDB
            FindIterable<Document> userDocuments = usersCollection.find();
            List<NodeValue> userNodes = new ArrayList<>();

            List<String> userIds = new ArrayList<>();


            System.out.println("** DistributingId ** -- Creazione UserNode");
            for (Document userDocument : userDocuments) {
                String userId = userDocument.getObjectId("_id").toString();
                String userName = userDocument.getString("name");
                String surname = userDocument.getString("surname");
                int age = userDocument.getInteger("age");
                String imgProfile = userDocument.getString("img_profile");

                userIds.add(userDocument.get("_id").toString());

                session.run("CREATE (u:User {id: $id, name: $name, surname: $surname, " +
                                "age: $age,img_profile: $img_profile})",
                        Values.parameters("id", userId, "name", userName, "surname", surname,
                                "age",age,"img_profile",imgProfile));
            }

            Random random = new Random();
            /*
            System.out.println("** DistributingId ** -- Creazione friendship");
            for (String userId : userIds){
                int randomIndex;
                int numFriends = random.nextInt(8) + 1; // Numero casuale di amici
                List<String> alreadyFriends = new ArrayList<>();

                for (int i = 0; i < numFriends; i++) {
                    String newFriendId = null;
                    boolean found = false;

                    do{
                        randomIndex = random.nextInt(userIds.size());
                        newFriendId = userIds.get(randomIndex);

                        found = false;
                        for (String s : alreadyFriends) {
                            if (s.equals(newFriendId)) {
                                found = true;
                                break; // Esci dal ciclo una volta trovata la corrispondenza
                            }
                        }
                    }
                    while (newFriendId.equals(userId) == true || found == true);

                    alreadyFriends.add(newFriendId);


                    session.run("MATCH (u1:User {id: $id1}), (u2:User {id: $id2}) CREATE (u1)-[:FRIEND]->(u2)",
                            Values.parameters("id1", userId, "id2", newFriendId));
                    session.run("MATCH (u1:User {id: $id1}), (u2:User {id: $id2}) CREATE (u1)<-[:FRIEND]-(u2)",
                            Values.parameters("id1", userId, "id2", newFriendId));
                }
            }*/
            // *************************** PATHS SECTION **************************
            //1)creare un nodo per ogni path nella collezione mongoDB paths
            //2)per ogni userId si creano dai [2,5] score di random value [1,5]
            //      andando a crare una relation di tipo Score tra User e Path

            // Recupera tutti i path da MongoDB
            FindIterable<Document> pathDocuments = pathsCollection.find();
            //List<NodeValue> pathNodes = new ArrayList<>(); non necessari
            List<String> pathIds = new ArrayList<>(); //necessario più avanti!
            System.out.println("** DistributingId ** -- Creazione PathNode");

            for (Document pathDocument : pathDocuments) {

                String pathId = pathDocument.getObjectId("_id").toString();
                String pathName = pathDocument.getString("name");
                pathIds.add(pathDocument.get("_id").toString());

                session.run("CREATE (p:Path {id: $id, name: $name})",
                        Values.parameters("id", pathId, "name", pathName));

            }

            //Associazioni User - discovered - Path
            //MANCA

            //Associazioni User - performed - activity - on - Path
            MongoCollection<Document> activitiesCollection = database.getCollection("Activities");
            FindIterable<Document> actDocuments = activitiesCollection.find();
            System.out.println("** DistributingId ** -- Creazione User - performed - activity - on - Path");
            int counter = 0;
            for (Document actDoc : actDocuments) {
                System.out.println("-- -- -- Creating " + counter + "° activity relation");
                counter++;
                String actId = actDoc.getObjectId("_id").toString();
                String userId = actDoc.getObjectId("id_user").toString();
                String pathId = actDoc.getObjectId("id_path").toString();
                String actName = actDoc.getString("name");
                //String distance = actDoc.getString("distance");

                session.run(  "MATCH (u:User{id: $id_user}), (p:Path{id:$id_path})\n" +
                                "CREATE (act:Activity{id:$id_activity,name:$name})\n" +
                                "CREATE (u)-[:PERFORMED]->(act)\n" +
                                "CREATE (act)-[:ON]->(p)",
                        Values.parameters("id_user", userId, "id_path", pathId,
                                "id_activity", actId,"name", actName));

            }

            //recuperare tutte le attività
            //per ogni attività creare nodo activity
            //creare relazioine





            System.out.println("** DistributingId ** -- Associazione casuale User - Evaluate - Path");
            //Associazione casuale User - Evaluate - Path
            counter = 0;
            for(String userId: userIds) {
                System.out.println("-- -- -- Creating " + counter + "° evaluate relation");
                counter++;
                int numEvaluations = random.nextInt(4) + 2; // Numero casuale di valutazioni path

                Set<Integer> distinctPathIds = new HashSet<>();

                // Genera n valori casuali distinti
                while (distinctPathIds.size() < numEvaluations) {
                    int numeroCasuale = random.nextInt(pathIds.size() );
                    distinctPathIds.add(numeroCasuale);
                }

                for (int index : distinctPathIds) {
                    String pathId = pathIds.get(index);
                    //int score = random.nextInt(maxRange - minRange + 1) + minRange;
                    int score = random.nextInt(4)+2; //valori casuali tra 2 e 5 inclusi
                    session.run("MATCH (u:User {id: $userId}), (p:Path {id: $pathId}) CREATE (u)-[:EVALUATION{score:$score}]->(p)",
                            Values.parameters("userId", userId, "score", score,"pathId", pathId));
                }
            }

        }

    }
}
