package it.unipi.dii.aide.movetogether.Models;

import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Projections.*;
import static org.neo4j.driver.Values.parameters;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.internal.value.NodeValue;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class DatabaseDriver implements AutoCloseable{
    private MongoClient mongoClient;
    private String mongoPort = "27017";
    //A causa di alcuni problemi con le macchine virtuali dell'università,
    // i DBMS MongoDB ed Neo4j sono stati eseguiti in locale
    private String mongoFirstIp = "10.1.1.23";  //TO DEFINE
    private String mongoFirstPort = "27017";    //TO DEFINE
    private String mongoSecondIp = "10.1.1.22"; //TO DEFINE
    private String mongoSecondPort = "27017";   //TO DEFINE
    private String mongoThirdIp = "10.1.1.21";  //TO DEFINE
    private String mongoThirdPort = "27017";    //TO DEFINE
    private String mongoUsername;
    private String mongoPassword;
    private String mongoDbName = "MoveTogether";
    private static MongoDatabase database;
    private static Gson gsonBuilder = new Gson();


    private Driver driver; //neo4j driver
    private String neo4jIp = "localhost";
    private String neo4jPort = "7688";//"7687";
    private String neo4jUsername = "neo4j";
    private String neo4jPassword = "password";//"neo4j";



    public DatabaseDriver(){
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
        try {
            driver = GraphDatabase.driver("bolt://" + neo4jIp + ":" + neo4jPort, AuthTokens.basic( neo4jUsername, neo4jPassword));
            driver.verifyConnectivity();
            System.out.println("Dentro GraphDatabase");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MongoDatabase getDatabase() {
        return database;
    }


    public void testWithIndex(){
        List<Path> paths = new ArrayList<>();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDay = localDateTime.toLocalDate().atStartOfDay().minusMonths(3);
        //Nota LocalDateTime fornisce il tempo nella stesso formato usato da MongoDB
        //String filterDate = startDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); NON USARE
        System.out.println("Dentro getTRENDPATHS()");
        MongoCollection<Document> collection = database.getCollection("Activities");
        Bson filter = match(gte("date",startDay));

        Document cursor2 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")

        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("DENTRO getTrendingPaths - explain: " + cursor2.toJson());

        database.getCollection("Activities").createIndex(new Document("date",1));

        Document cursor3 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")

        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("createIndex()");
        System.out.println("DENTRO getTrendingPaths - explain: " + cursor3.toJson());

        database.getCollection("Activities").createIndex(new Document("date",1).append("id_path",-1));
        Document cursor4 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")

        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("createIndex()");
        System.out.println("DENTRO getTrendingPaths - explain: " + cursor4.toJson());
    }
    public void test_userPerformance(){
        String idPath = "655f2999cb563fca79f895f6";//"655d35135403deaf4d8ff19e";

        MongoCollection<Document> collection = database.getCollection("Activities");

        Document cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_path", new ObjectId(idPath))),
                new Document("$group", new Document("_id",new Document("id_user","$id_user"))
                        .append("bestTime", new Document("$min","time_elapsed"))),
                new Document("$sort", new Document("time_elapsed",1)),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")
        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("DENTRO test_userPerformance without index - explain: " + cursor.toJson());

        database.getCollection("Activities").createIndex(new Document("id_path",-1));
        Document cursor2 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_path", new ObjectId(idPath))),
                new Document("$group", new Document("_id",new Document("id_user","$id_user"))
                        .append("bestTime", new Document("$min","time_elapsed"))),
                new Document("$sort", new Document("time_elapsed",1)),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")
        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("DENTRO test_userPerformance with index id_path - explain: " + cursor2.toJson());


        database.getCollection("Activities").createIndex(new Document("id_path",-1));
        Document cursor3 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_path", new ObjectId(idPath))),
                new Document("$group", new Document("_id",new Document("id_user","$id_user"))
                        .append("bestTime", new Document("$min","time_elapsed"))),
                new Document("$sort", new Document("time_elapsed",1)),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")
        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("DENTRO test_userPerformance with index id_path e id_user - explain: " + cursor3.toJson());
    }
    /*
    Questa versione funzionava quando usavo una collezione di soli commenti.
    è stato, infine, deciso di integrare i commenti per ogni path document della
    collezione Paths.
     */
    public List<Path> getBestRatingPathOldVersion(int limit) {
        int threshold = 1; //100 with the complete dataset
        System.out.println("DENTRO GetBestRatingPath");
        MongoCollection<Document> collection = database.getCollection("Reviews");
        List<Comment> reviews = new ArrayList<Comment>();

        //Bson group = group("$id_path",sum("count",1));
        //Bson sort = sort(descending("count"));
        //Bson Unwind = new Document("$unwind","_id");
        Bson Group = new Document("$group",
                new Document("_id", new Document("id_path", "$id_path").append("name_path", "$name_path"))
                        .append("avg_vote", new Document("$avg", "$vote"))
                        .append("count_votes", new Document("$sum", 1))
        );
        Bson Limit = limit(limit);
        Bson Filter = match(gte("count_votes", threshold));
        Bson Set = new Document("$set", new Document("id_path", "$_id.id_path").append("name_path", "$_id.name_path"));
        Bson Unset = new Document("$unset", "_id");
        Bson Project = project(fields(include("id_path")));


        //Trovare una lista di path_id più apprezzati
        MongoCursor cursor = collection.aggregate(Arrays.asList(Group, Limit, Filter, Set, Unset/*, Project*/)).iterator();
        List<Path> paths = new ArrayList<>();


        try {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                String idPath = document.getObjectId("id_path").toString();
                Path path = getPathDetail(idPath);
                if (path != null)
                    paths.add(path);
            }
        } finally {
            cursor.close();
        }
        return paths;
    }

    public List<Path> getBestRatingPath(int limit){
        int threshold = 100; //100 with the complete dataset
        System.out.println("DENTRO GetBestRatingPath");
        MongoCollection<Document> collection = database.getCollection("Paths");
        List<Path> paths = new ArrayList<Path>();

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("num_comments", new Document("$gte", threshold))),
                new Document("$sort", new Document("avg_score", -1)),
                new Document("$limit", limit)
                //new Document("$project", new Document("kcal", 0).append("distance",0)), //da togliere era solo per testare il project
        )).iterator();


        try {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                //String idPath = document.getObjectId("id_path").toString();
                //Path path = getPathDetail(idPath);
                Path path = new Path(document);
                if (path != null)
                    paths.add(path);
            }
        } finally {
            cursor.close();
        }
        return paths;
    }

    // TO DO - DA IMPLEMENTARE IN MONGODB <- perchè avevo scritto questa cosa non vera??
    public List<User> getSuggestedFriends(String idUser,int limit){
        List<User> friends = new ArrayList<>();
        System.out.println("*********** getSuggestedFriends: idUser ottenuto: "+idUser);
            try(Session session = driver.session())
            {
                session.readTransaction(tx->{
                    Result r = tx.run("MATCH (p1:Path)<-[ev1:EVALUATION]-(me:User{id:$idUser})-[:FRIEND*2..2]-(u:User)-[ev2:EVALUATION]->(p1)\n" +
                                    "where not exists ((me)-[:FRIEND]->(u)) and ev1.score >=3 and ev2.score >= 3\n" +
                                    "return distinct(u.id) as id,u.name as name,u.surname as surname,u.age as age, u.img_profile as img_profile\n" +
                                    "UNION\n" +
                                    "MATCH (me:User{id:$idUser})-[v1:EVALUATION]->(p:Path)<-[v2:EVALUATION]-(u:User)\n" +
                                    "where v1.score >= 3 and v2.score >= 3\n" +
                                    "    and not exists((me)-[:FRIEND]->(u))\n" +
                                    "return distinct(u.id) as id,u.name as name,u.surname as surname,u.age as age, u.img_profile as img_profile\n" +
                                    "limit $limit;",

                            //TO DO - se mi rimane tempo - General suggestion friend - per ridurre la probabilità che non vengano
                            //suggeriti utenti bisogna aggiungere una query più generica: bisogna suggerire gli utenti
                            //che hanno fatto attività a uno stesso path negli ultimi 6mesi e che non sono amici
                            Values.parameters("idUser", idUser,"limit",limit));
                    System.out.println("*********** getFriends: Prima del while");
                    while (r.hasNext()){
                        System.out.println("*********** getFriends: Dentro il while");
                        Record rec = r.next();
                        String idFriend = null;
                        String name = null;
                        String surname = null;
                        String imageProfile = null;
                        Integer age = null;
                        if(rec.get("id") != null)
                            idFriend = rec.get("id").asString();
                        if(rec.get("name") != null)
                            name = rec.get("name").asString();
                        if(rec.get("surname") != null)
                            surname = rec.get("surname").asString();
                        if(rec.get("img_profile") != null)
                            imageProfile = rec.get("img_profile").asString();
                        if(rec.get("age") != null)
                            age = rec.get("age").asInt();
                        else age = 0;
                        String password = "0000";
                        System.out.println("Nome amico:" + name);
                        System.out.println("Cognome amico:" + surname);
                        friends.add(new User(new ObjectId(idFriend),name,surname,password,age,null,null,null,null,imageProfile,null));
                    }
                    return null;
                });

                return friends;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }

    }

    //Testato - funziona correttamente
    public List<Path> getSuggestedPaths(String idUser,int limit){
        List<Path> paths = new ArrayList<>();
        System.out.println("*********** getSuggestedPaths: idUser ottenuto: "+ idUser);
        try(Session session = driver.session())
        {
            session.readTransaction(tx->{
                Result r = tx.run(" MATCH (p:Path)<-[ev:EVALUATION]-(u:User)\n" +
                                "    with avg(ev.score) as AverageScore,p\n" +
                                "    where AverageScore >= 3.0 and\n" +
                                "    not exists((:User{id:$idUser})-[:PERFORMED]->(:Activity)-[:on]->(p)) and\n" +
                                "    not exists((:User{id:$idUser})-[:EVALUATION]->(p))  and\n" +
                                "    exists((:User{id:$idUser})-[:FRIEND]->(:User)-[:CREATED]->(:Path))\n" +
                                "    RETURN distinct(p.id) as IdPath\n" +
                                "                UNION\n" +
                                "        MATCH (me:User{id:$idUser})-[:FRIEND]->(f:User)-[ev:EVALUATION]->(p:Path)\n" +
                                "        where ev.score >= 3.0 and\n" +
                                "        not exists((me)-[:PERFORMED]->(:Activity)-[:on]->(p)) and\n" +
                                "        not exists((me)-[:EVALUATION]->(p))\n" +
                                "        RETURN distinct(p.id) as IdPath\n" +
                                "                UNION\n" +
                                "      MATCH (p:Path)<-[ev:EVALUATION]-(u:User)\n" +
                                        "with avg(ev.score) as AverageScore,p\n" +
                                        "WHERE  not exists((:User{id:$idUser})-[:EVALUATION]->(p)) and\n" +
                                        "not exists(((:User{id:$idUser})-[:PERFORMED]->(:Activity)-[:on]->(p)))\n" +
                                        "and AverageScore >= 3.0\n" +
                                        "RETURN distinct(p.id) as IdPath\n" +
                                "        LIMIT $limit;",
                        Values.parameters("idUser", idUser,"limit",limit));
                System.out.println("*********** getSuggestedPaths: Prima del while");
                while (r.hasNext()){
                    System.out.println("*********** getSuggestedPaths: Dentro il while");
                    Record rec = r.next();
                    String idPath = null;
                    if(rec.get("IdPath") != null) {
                        idPath = rec.get("IdPath").asString();
                        paths.add(getPathDetail(idPath));
                    }
                }
                return null;
            });

            return paths;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }




    }

    public float getUserStatPerformance(String idUser,String idPath,String typeActivity){
        typeActivity = "Run";
        float perc = 0;
        boolean found = false;
        int numDocuments=0, userPosition = 0;

        //trovare tutte le attività di pathId = idPath
        System.out.println("Dentro getUserStatPerformance");

        MongoCollection<Document> collection = database.getCollection("Activities");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_path", new ObjectId(idPath))),
                new Document("$group", new Document("_id",new Document("id_user","$id_user"))
                                .append("bestTime", new Document("$min","time_elapsed"))),
                new Document("$sort", new Document("time_elapsed",1)),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")
        )).iterator();


        try{
            while(cursor.hasNext()){
                System.out.println("Dentro getUserStatPerformance - cursor");
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());

                String idUserFound = document.getObjectId("id_user").toString();
                if(idUser.equals(idUserFound)) {
                    found = true;
                    System.out.println("Utente trovato alla posizione: " + userPosition);
                }
                if(!found) {
                    userPosition++;
                }
                numDocuments++;
            }
        }
        finally{
            cursor.close();
        }
        //In un iteratore scorrere finché non si trova la posizione di idUser
        //contare il totale di document
        //calcolare
        if(found) {
            perc = (float) (numDocuments - userPosition) / numDocuments;
        }
        else
            perc = (float)0.0;
        System.out.println("Risultato calcolato: " + perc);
        return perc;
    }

    public List<Path> getAllPaths(){
        System.out.println("DENTRO getAllPaths");
        MongoCollection<Document> collection = database.getCollection("Path");
        List<Path> paths = new ArrayList<Path>();
        //  TESTARE EXPLAIN STAT
        Document cursor1 = collection.find().explain();
        System.out.println("EXPLAIN PROVA: " + cursor1.toJson());
       // *************************** RIMUOVERE POI
        MongoCursor<Document> cursor = collection.find().iterator();
        try{
            while(cursor.hasNext()){
                Document document = cursor.next();
                System.out.println(document.toJson());
                paths.add(new Path(document));
            }
        }
        finally{
            cursor.close();
        }

        return paths;
    }

    @Override
    public void close(){

        mongoClient.close();
        if( driver != null)
            driver.close();
    }

    //TESTATO - FUNGE CORRETTAMENTE
    public User getUserDetails(String idUser){
        User result;
        MongoCollection<Document> collection = database.getCollection("Users");
        Document doc = collection.find(eq("_id", new ObjectId(idUser))).first();
        if (doc == null) {
            System.out.println("Document does not exist");
        } else {
            System.out.println("Document FOUND!!");
            System.out.println(doc.toJson());
        }

        result = new User(doc);
        return result;
    }

    //TESTATO - FUNGE CORRETTAMENTE
    public Path getPathDetail(String idPath){
        Path result;
        MongoCollection<Document> collection = database.getCollection("Paths");
        Document doc = collection.find(eq("_id", new ObjectId(idPath))).first();
        if (doc == null) {
            System.out.println("Document does not exist");
            return null;
        } else {
            System.out.println("Document FOUND!!");
            System.out.println(doc.toJson());
        }

        result = new Path(doc);
        return result;
    }

    //TESTATO - FUNZIONA
    public List<Activity> getLastActivities(String idUser,int limit, int skip){
        List<Activity> activities = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("Activities");
        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match",new Document("id_user",new ObjectId(idUser))),
                new Document("$sort", new Document("date", -1)),
                new Document("$limit", limit),
                //new Document("$project", new Document("kcal", 0).append("distance",0)), //da togliere era solo per testare il project
                new Document("$skip",skip)
        )).iterator();
        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                Activity act = new Activity(document);
                System.out.println("Controllo se id_activity e' stato preso:");
                System.out.println(act.idActivityProperty().getValue().toString());
                activities.add(act);

            }
        }
        finally{
            cursor.close();
        }

        return activities;
    }

    public List<Path> getDiscoveredPathsByUser(String idUser,int limit){
        List<Path> paths = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("Paths");
        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match",new Document("id_creator",new ObjectId(idUser))),
                new Document("$limit", limit)
        )).iterator();
        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                Path act = new Path(document);
                paths.add(act);
            }
        }
        finally{
            cursor.close();
        }
        return paths;
    }

    public List<Comment> getLastComments(String pathId,int limit){

        System.out.println("Dentro getLastComments");
        List<Comment> comments = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("Paths");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("_id", new ObjectId(pathId))),
                new Document("$project", new Document("comments", 1)), //select ONLY comments
                new Document("$unwind", "$comments"),
                new Document("$project",new Document("name","$comments.name").append("surname","$comments.surname").
                        append("country","$comments.country").append("comment_timestamp","$comments.comment_timestamp")
                        .append("content","$comments.content").append("title","$comments.title")
                        .append("score","$comments.score")),
                new Document("$sort", new Document("comment_timestamp",-1))
        )).iterator();

        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                comments.add(new Comment(document));
            }
        }
        finally{
            cursor.close();
        }
        return comments;
    }

    public String getMostFrequentActivityTypeByUser(String moverId){
        System.out.println("Dentro getMostFrequentActivityTypeByUser");
        String activityType = "";
        MongoCollection<Document> collection = database.getCollection("Activities");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_user", new ObjectId(moverId))),
                new Document("$group",
                        new Document("_id",new Document("type_activity","$type_activity"))
                                .append("count_type", new Document("$sum",1))),
                new Document("$sort", new Document("count_type",-1)),
                new Document("$set", new Document("type_activity", "$_id.type_activity")),
                new Document("$unset","_id"),
                new Document("$limit",1)

        )).iterator();
        try{
            while(cursor.hasNext()){
                Document doc = (Document) cursor.next();
                System.out.println(doc.toJson());
                activityType = doc.get("type_activity").toString();
                System.out.println("Result: " + doc.get("type_activity").toString());
            }
        }
        finally{
            cursor.close();
        }

        return activityType;
    }

    public String getMostPracticedPathByUser(String moverId){

        System.out.println("Dentro getMostPraticedPathByUser");
        String pathName = "";
        MongoCollection<Document> collection = database.getCollection("Activities");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_user", new ObjectId(moverId))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_type", new Document("$sum",1))),
                new Document("$sort", new Document("count_type",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id"),
                new Document("$limit",1)

        )).iterator();
        try{
            while(cursor.hasNext()){
                Document doc = (Document) cursor.next();
                System.out.println("Risultato query:");
                System.out.println(doc.toJson());

                String pathId = doc.getObjectId("id_path").toString();
                System.out.println("pathId trovato: " + pathId);
                Path path = getPathDetail(pathId);
                System.out.println("Pathname trovato:");
                pathName = path.nameProperty().getValue();
                System.out.println("---> " + pathName);
            }
        }
        finally{
            cursor.close();
        }
        return pathName;
    }

    public int getNumberActivitiesPraticedByUser(String moverId){
        int result = getNumberActivitiesLastPeriodByUser(moverId,1000);
        return result;
    }

    public int getNumberActivitiesLastPeriodByUser(String moverId,int agoMonths){
        System.out.println("Dentro getNumberActivitiesLastPeriod()");
        int result = 0;
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDay = localDateTime.toLocalDate().atStartOfDay().minusMonths(agoMonths);

        MongoCollection<Document> collection = database.getCollection("Activities");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_user", new ObjectId(moverId))),
                new Document("$match", new Document("date",new Document("$gte",startDay))),

                new Document("$group",
                        new Document("_id",new Document("id_user","$id_user"))
                                .append("count_activities", new Document("$sum",1))),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")

        )).iterator();
        try{
            while(cursor.hasNext()){
                Document doc = (Document) cursor.next();
                System.out.println(doc.toJson());
                result = Integer.parseInt(doc.get("count_activities").toString());
                System.out.println("Result: " + doc.get("count_activities").toString());
            }
        }
        finally{
            cursor.close();
        }

        return result;
    }

    public int getNumberKcalsBurnedLastPeriod(String moverId,int agoMonths){
        System.out.println("Dentro getNumberKcalsBurnedLastPeriod()");
        int totKcals = 0;
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDay = localDateTime.toLocalDate().atStartOfDay().minusMonths(agoMonths);

        MongoCollection<Document> collection = database.getCollection("Activities");

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("id_user", new ObjectId(moverId))),
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_user","$id_user"))
                                .append("total_kcals", new Document("$sum","$kcal"))),
                new Document("$set", new Document("id_user", "$_id.id_user")),
                new Document("$unset","_id")
        )).iterator();
        try{
            while(cursor.hasNext()){
                Document doc = (Document) cursor.next();
                System.out.println(doc.toJson());
                totKcals = Integer.parseInt(doc.get("total_kcals").toString());
                System.out.println("Result: " + doc.get("total_kcals").toString());
            }
        }
        finally{
            cursor.close();
        }

        return totKcals;
    }





    public List<Path> getTrendingPaths(int limit) {
        List<Path> paths = new ArrayList<>();

        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDay = localDateTime.toLocalDate().atStartOfDay().minusMonths(3);
        //Nota LocalDateTime fornisce il tempo nella stesso formato usato da MongoDB
        //String filterDate = startDay.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); NON USARE
        System.out.println("Dentro getTRENDPATHS()");
        MongoCollection<Document> collection = database.getCollection("Activities");
        Bson filter = match(gte("date",startDay));

        Document cursor2 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")

        )).explain(ExplainVerbosity.EXECUTION_STATS);
        //System.out.println("DENTRO getTrendingPaths - explain: " + cursor2.toJson());

        database.getCollection("Activities").createIndex(new Document("date",1));
        Document cursor3 = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")

        )).explain(ExplainVerbosity.EXECUTION_STATS);
        System.out.println("createIndex()");
        //System.out.println("DENTRO getTrendingPaths - explain: " + cursor3.toJson());


        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("date",new Document("$gte",startDay))),
                new Document("$group",
                        new Document("_id",new Document("id_path","$id_path"))
                                .append("count_votes", new Document("$sum",1))),
                new Document("$sort", new Document("count_votes",-1)),
                new Document("$set", new Document("id_path", "$_id.id_path")),
                new Document("$unset","_id")
        )).iterator();
        try{
            while(cursor.hasNext()){
                Document doc = (Document) cursor.next();
                System.out.println(doc.toJson());
                String idPath = doc.getObjectId("id_path").toString();
                Path path = getPathDetail(idPath);
                if(path != null)
                    paths.add(path);
            }
        }
        finally{
            cursor.close();
        }
        return paths;
    }

    public boolean insertUser(User user){
        MongoCollection<Document> collection = database.getCollection("Users");
        try {
            Document doc = new Document("name", user.nameProperty().getValue())
                    .append("username", user.surnameProperty().getValue())
                    .append("password", user.passwordProperty().getValue())
                    .append("date",user.dateProperty().getValue());
            collection.insertOne(doc);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getFriends(String idUser, int limit){
        List<User> friends = new ArrayList<>();
        System.out.println("*********** getFriends: idUser ottenuto: "+idUser);
        try(Session session = driver.session())
        {
                session.readTransaction(tx->{
                Result r = tx.run("match (u:User)-[:FRIEND]->(u2:User)\n" +
                                "where u.id = $idUser\n" +
                                "return distinct(u2.id) as id,u2.name as name,u2.surname as surname," +
                                "u2.age as age, u2.img_profile as img_profile " +
                                "limit 10;",//,u2.surname,u2.age;",
                        Values.parameters("idUser", idUser));
                System.out.println("*********** getFriends: Prima del while");
                while (r.hasNext()){
                    System.out.println("*********** getFriends: Dentro il while");
                    Record rec = r.next();
                    String idFriend = null;
                    String name = null;
                    String surname = null;
                    String imageProfile = null;
                    Integer age = null;
                    if(rec.get("id") != null)
                        idFriend = rec.get("id").asString();
                    if(rec.get("name") != null)
                        name = rec.get("name").asString();
                    if(rec.get("surname") != null)
                        surname = rec.get("surname").asString();
                    if(rec.get("img_profile") != null)
                        imageProfile = rec.get("img_profile").asString();
                    if(rec.get("age") != null)
                        age = rec.get("age").asInt();
                    else age = 0;
                    String password = "0000";
                    System.out.println("Nome amico:" + name);
                    System.out.println("Cognome amico:" + surname);
                    friends.add(new User(new ObjectId(idFriend),name,surname,password,age,null,null,null,null,imageProfile,null));
                }
                return null;
            });

            return friends;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<User> getMutualFriends(String idUser, String idUser2, int limit){
        List<User> friends = new ArrayList<>();
        System.out.println("*********** getMutualFriends: idUser ottenuto: "+idUser);
        try(Session session = driver.session())
        {
            session.readTransaction(tx->{
                Result r = tx.run("MATCH (me:User{id:$idUser})-[:FRIEND]->(u:User)<-[:FRIEND]-(u2:User{id:$idUser2})\n" +
                                "return distinct(u.id) as id,u.name as name\n" +
                                "limit $limit;",//,u2.surname,u2.age;",
                        Values.parameters("idUser", idUser,"idUser2",idUser2,"limit",limit));
                System.out.println("*********** getMutualFriends: Prima del while");
                while (r.hasNext()){
                    System.out.println("*********** getMutualFriends: Dentro il while");
                    Record rec = r.next();
                    String idFriend = null;
                    String name = null;
                    String surname = null;
                    String imageProfile = null;
                    Integer age = null;
                    if(rec.get("id") != null)
                        idFriend = rec.get("id").asString();
                    if(rec.get("name") != null)
                        name = rec.get("name").asString();
                    if(rec.get("surname") != null)
                        surname = rec.get("surname").asString();
                    if(rec.get("img_profile") != null)
                        imageProfile = rec.get("img_profile").asString();
                    //if(rec.get("age") != null)
                    //    age = rec.get("age").asInt();
                    String password = "0000";
                    System.out.println("Nome amico:" + name);
                    System.out.println("Cognome amico:" + surname);
                    friends.add(new User(new ObjectId(idFriend),name,"surname",password,age=18,null,null,null,null,imageProfile,null));
                }
                return null;
            });

            return friends;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Path> getMutualLikedPaths(String loggedUserId, String moverId,int limit){
        List<Path> paths = new ArrayList<>();
        System.out.println("*********** getMutualLikePaths: idUserLogged ottenuto: "+loggedUserId);
        try(Session session = driver.session())
        {
            session.readTransaction(tx->{
                Result r = tx.run("MATCH (me:User{id:$loggedUserId})-[ev1:EVALUATION]->(p:Path)<-[ev2:EVALUATION]-(u2:User{id:$moverId})\n" +
                                "where ev1.score >= 1 and ev2.score >= 1\n" +
                                "return distinct(p.id) as id,p.name as name\n" +
                                "limit $limit;",//,u2.surname,u2.age;",
                        Values.parameters("loggedUserId", loggedUserId,"moverId",moverId,"limit",limit));
                System.out.println("*********** getMutualLikePaths: Prima del while");
                while (r.hasNext()){
                    System.out.println("*********** getMutualLikePaths: Dentro il while");
                    Record rec = r.next();
                    String idPath = null;
                    String name = null;
                    if(rec.get("id") != null)
                        idPath = rec.get("id").asString();
                    if(rec.get("name") != null)
                        name = rec.get("name").asString();

                    int distance = 123;
                    System.out.println("Nome path:" + name);
                    paths.add(new Path(new ObjectId(idPath),name=name,new ObjectId(),distance=123,"pippo",123,45,"location",0,0.0,null));
                }
                return null;
            });

            return paths;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean checkFriendship(String loggedUserId, String moverId){
        System.out.println("Dentro checkFriendship");
        System.out.println("Dentro checkFriendship - loggerUserId: " + loggedUserId);
        System.out.println("Dentro checkFriendship - moverId: " + moverId);
        boolean[] isFriend = new boolean[1];
        try(Session session = driver.session())
        {
            session.readTransaction(tx->{
                Result r = tx.run("match (u:User)-[:FRIEND]->(u2:User)\n" +
                                "where u.id = $loggedUserId and u2.id = $moverId\n"+
                                "return distinct(u2.id) as id,u2.name as name ;"
                                ,
                        Values.parameters("loggedUserId", loggedUserId,"moverId",moverId));
                System.out.println("*********** checkFriendship: Prima del while");
                if(r.hasNext()){
                    System.out.println("*********** checkFriendship: dentro il r.hasNext()");
                    isFriend[0] = true;
                }
                else
                    isFriend[0] = false;

                return null;
            });

        return isFriend[0];
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void createFriendship(String loggedUserId,String newFriendId) {
        try (Session session = driver.session()) {
            session.run("MATCH (u1:User {id: $id1}), (u2:User {id: $id2}) CREATE (u1)-[:FRIEND]->(u2)",
                    Values.parameters("id1", loggedUserId, "id2", newFriendId));
            session.run("MATCH (u1:User {id: $id1}), (u2:User {id: $id2}) CREATE (u1)<-[:FRIEND]-(u2)",
                    Values.parameters("id1", loggedUserId, "id2", newFriendId));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public User checkCredUser(String username, String password) {
        MongoCollection collection = database.getCollection("Users");
        try{
            Document result = (Document) collection.find(Filters.and(eq("name", username),
                            eq("password", password))).
                    first();

            User user = new User(result);
            return user;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //FUNZIONA CORRETTAMENTE
    public List<User> getMostAppreciatedDiscovers(int limit) {
        int threshold = 100; //100 with the complete dataset
        System.out.println("DENTRO getMostAppreciatedDiscovers");
        MongoCollection<Document> collection = database.getCollection("Paths");
        List<User> users = new ArrayList<User>();

        MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
                new Document("$match", new Document("num_comments", new Document("$gte", threshold))),
                new Document("$group", new Document("_id", new Document("id_creator", "$id_creator"))
                        .append("avg_score", new Document("$avg", "$avg_score"))),
                new Document("$sort", new Document("avg_score", -1)),
                new Document("$set", new Document("id_creator", "$_id.id_creator")),
                new Document("$unset","_id"),
                new Document("$limit", limit)

        )).iterator();

        try {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                String idUser = document.getObjectId("id_creator").toString();
                User user = getUserDetails(idUser);
                if (user != null)
                    users.add(user);
            }
        } finally {
            cursor.close();
        }

        return users;
        //return getFriendsVersion3(limit);
    }

    public int getNumberRegisteredUsers(){
        MongoCollection collection = database.getCollection("Users");
        int count = (int) collection.countDocuments();
        return count;
    }

    public int getNumberDiscoveredPaths(){
        MongoCollection collection = database.getCollection("Paths");
        int count = (int) collection.countDocuments();
        return count;
    }

    public List<User> findUsers(String nameUser,String city, int limit) {

        Pattern pattern1 = Pattern.compile("^.*" + nameUser + ".*$", Pattern.CASE_INSENSITIVE);
        Bson filterName = Filters.regex("name",pattern1);
        Bson filterSurname = Filters.regex("surname",pattern1);
        Pattern pattern2 = Pattern.compile("^.*" + city + ".*$", Pattern.CASE_INSENSITIVE);
        Bson filterLocality = Filters.regex("city",city);
        MongoCollection<Document> collection = database.getCollection("Users");
        MongoCursor<Document> cursor = collection.find(Filters.and(Filters.or(filterName, filterSurname),filterLocality)).limit(limit).iterator();
        List<User> users = new ArrayList<>();
        System.out.println("Dentro findUsers(): -> dopo la FIND");
        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                users.add(new User(document));
            }
        }
        finally{
            cursor.close();
        }
        if(users == null)
            System.out.println("Dentro findUsers(): -> Non sono stati trovati utenti, return null");
        return users;
    }

    public List<Activity> findActivities(String nameAct, String minDist, String maxDist, String minDur, String maxDur, String loc, int limit) {

        Pattern pattern1 = Pattern.compile("^.*" + nameAct + ".*$", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("^.*" + loc + ".*$", Pattern.CASE_INSENSITIVE);

        List<Bson> pipeline = new ArrayList<>();
        Bson filterName = Filters.regex("name",pattern1);
        pipeline.add(match(filterName));
        //Bson filterLocation= Filters.regex("location",pattern2);
        //pipeline.add(match(filterLocation));

        if(!minDist.isEmpty()) {
            Bson filterGteDistance = Filters.gte("distance", Integer.parseInt(minDist));
            pipeline.add(match(filterGteDistance));
        }

        if(!maxDist.isEmpty()) {
            Bson filterLteDistance = Filters.lte("distance", Integer.parseInt(maxDist));
            pipeline.add(match(filterLteDistance));
        }

        if(!minDur.isEmpty()) {
            Bson filterGteDuration = Filters.gte("time_elapsed", Integer.parseInt(minDur));
            pipeline.add(match(filterGteDuration));
        }

        if(!maxDur.isEmpty()) {
            Bson filterLteDuration = Filters.lte("time_elapsed", Integer.parseInt(maxDur));
            pipeline.add(match(filterLteDuration));
        }
        pipeline.add(limit(limit));

        MongoCollection<Document> collection = database.getCollection("Activities");
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        List<Activity> activities = new ArrayList<>();
        System.out.println("Dentro findActivities(): -> dopo la FIND");
        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                activities.add(new Activity(document));
            }
        }
        finally{
            cursor.close();
        }
        if(activities == null)
            System.out.println("Dentro findUsers(): -> Non sono stati trovate attività, return null");
        return activities;
    }

    public List<Path> findPaths(String namePath, String minDist, String maxDist, String minDur, String maxDur, String loc, int limit) {
        Pattern pattern1 = Pattern.compile("^.*" + namePath + ".*$", Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("^.*" + loc + ".*$", Pattern.CASE_INSENSITIVE);

        List<Bson> pipeline = new ArrayList<>();
        Bson filterName = Filters.regex("name",pattern1);
        pipeline.add(match(filterName));
        Bson filterLocation= Filters.regex("location",pattern2);
        pipeline.add(match(filterLocation));

        if(!minDist.isEmpty()) {
            Bson filterGteDistance = Filters.gte("distance", Integer.parseInt(minDist));
            pipeline.add(match(filterGteDistance));
        }

        if(!maxDist.isEmpty()) {
            Bson filterLteDistance = Filters.lte("distance", Integer.parseInt(maxDist));
            pipeline.add(match(filterLteDistance));
        }

        if(!minDur.isEmpty()) {
            Bson filterGteDuration = Filters.gte("time_elapsed", Integer.parseInt(minDur));
            pipeline.add(match(filterGteDuration));
        }

        if(!maxDur.isEmpty()) {
            Bson filterLteDuration = Filters.lte("time_elapsed", Integer.parseInt(maxDur));
            pipeline.add(match(filterLteDuration));
        }
        pipeline.add(limit(limit));

        MongoCollection<Document> collection = database.getCollection("Paths");
        MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
        List<Path> paths = new ArrayList<>();
        System.out.println("Dentro findPaths(): -> dopo la FIND");
        try{
            while(cursor.hasNext()){
                Document document = (Document) cursor.next();
                System.out.println(document.toJson());
                paths.add(new Path(document));
            }
        }
        finally{
            cursor.close();
        }
        if(paths == null)
            System.out.println("Dentro findPaths(): -> Non sono stati trovate attività, return null");
        return paths;
    }




    public void updateActivity(Activity activityModified){
        System.out.println("QUA ANDREBBBE MODIFICATA LA ACTIVITY");

        String idActivity = activityModified.idActivityProperty().getValue().toString();
        String name = activityModified.nameProperty().getValue();
        String typeActivity = activityModified.typeActivityProperty().getValue();
        int distance = activityModified.distanceProperty().getValue();
        int avgBpm = activityModified.avgBpmProperty().getValue();
        MongoCollection<Document> collection = database.getCollection("Activities");
        Bson Filter = new Document("_id", new ObjectId(idActivity));
        Bson update = new Document("$set",new Document("name",name)
                .append("type_activity",typeActivity)
                .append("distance",distance)
                .append("avg_bpm",avgBpm));

        collection.updateOne(Filter,update);

    }

    public void addComment(String pathId,Comment com){
        System.out.println("Dentro DatabaseDriver.java - addComment()");
        System.out.println(com.toString());

        MongoCollection<Document> collection = database.getCollection("Paths");

        String name = com.nameProperty().getValue();
        String surname = com.surnameProperty().getValue();
        String country = com.countryProperty().getValue();
        String title = com.titleProperty().getValue();
        String content = com.contentProperty().getValue();
        int score = com.scoreProperty().getValue();
        Date timestamp = com.commentTimestampProperty().getValue();

        Document documentToAdd = new Document("title",title).append("content",content).append("score",score)
                .append("comment_timestamp",timestamp).append("name",name).append("surname",surname).append("country",country);
        Bson Filter = new Document("_id", new ObjectId(pathId));
        Bson update = new Document("$push",new Document("comments",documentToAdd));

        collection.updateOne(Filter,update);
    }


// ***************************************************** INTER-CONSISTENCY


public Document getActivityMongoDB(String activityId) {
    try {
        MongoCollection<Document> activities = database.getCollection("Activities");
        Document activityDoc = activities.find(Filters.eq("_id", new ObjectId(activityId))).first();
        return activityDoc;
    } catch (Exception e) {
        return null;
    }
}

public boolean deleteActivityMongoDB(String activityId){

    //Intanto fare una find che andrà sostituita con una delete
    System.out.println("Dentro *** deleteActivity");

    MongoCollection<Document> collection = database.getCollection("Activities");
    MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(
            new Document("$match", new Document("_id", new ObjectId(activityId)))
    )).iterator();
    try{
        while(cursor.hasNext()){
            Document document = (Document) cursor.next();
            System.out.println("Verrebbe eliminato il seguente documento:");
            System.out.println(document.toJson());
        }
    }
    finally{
        cursor.close();
    }

    //Altri modi per cancellare:
    //Bson filter = new Document("$eq", new Document("_id", new ObjectId(activityId)));
    //Bson match = Filters.eq("_id", new ObjectId(activityId));
    try{
        Bson filter = new Document("_id", new ObjectId(activityId));
        collection.deleteOne(filter);
        return true;
    } catch (MongoException e) {
        System.err.println("Error: cannot delete course");
        return false;
    }
}


    public boolean deleteActivityNeo4j(String actId){
        System.out.println("*** deleteActivityNeo4j");
        try(Session session = driver.session()){
            session.writeTransaction((TransactionWork<Void>) tx -> {
                tx.run( "MATCH (u:Activity{id: $actId}) "+
                        "DETACH DELETE u", parameters( "actId", actId) );
                return null;
            });
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void addActivityMongoDB(Document activity){
        System.out.println("addActivityMongoDB ****");
        //Document actDoc = activity.getDocument();
        System.out.println("addActivityMongoDB - document aggiunto:");
        System.out.println(activity.toJson());
        MongoCollection<Document> collection = database.getCollection("Activities");
        collection.insertOne(activity);
    }


    public boolean deleteActivity(String actId){
        Document oldActivity = getActivityMongoDB(actId);
        System.out.println("Activity da cancellare:\n" + oldActivity.toString());
        if(deleteActivityMongoDB(actId)){
            if(!deleteActivityNeo4j(actId)){
                try{
                    addActivityMongoDB(oldActivity);
                    System.out.println("DELETE USER: FAILED");
                    System.out.println("DELETE USER: User not found in Neo4j");
                    System.out.println(oldActivity.toString());
                    return false;
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("DELETE USER: ROLLBACK FAILED");
                    System.out.println(oldActivity.toString());
                    return false;
                }
            }
            else{
                return true;
            }
        }

        return true;
    }


    /**
     * Deletes a user from the databases
     * If one of the operation goes bad, write to log the information about the error and the user to be deleted
     */
    public static boolean deleteUser(User user) {
        try {
            deleteUserReviews(user);
        } catch (MongoException e) {
            return false;
        }

        try {
            deleteUserActivities(user);
        } catch (MongoException e) {
            System.out.println(e.getMessage());
            System.out.println("DELETE USER: DELETE USER ACTIVITIES FAILED");

            return false;
        }

        try {
            neo4jDriver.deleteUser(user);
        } catch (Neo4jException e) {
            System.out.println(e.getMessage());
            System.out.println("DELETE USER: FAILED");
            return false;
        }

        try {
            mongoDBDriver.deleteUser(user);
        } catch (MongoException e) {
            System.out.println(e.getMessage());
            System.out.println("DELETE USER: FAILED");

            return false;
        }

        return true;
    }

    public boolean deleteUserMongoDB(String userId){

        return true;
    }

    public boolean deleteUserNeo4j(String userId){

        return true;
    }

    public boolean deleteUser(String userId){
        Document oldUserDocument = new Document(); //save oldUserDocument
        if(deleteUserMongoDB(userId)){
            if(!deleteUserNeo4j(userId)){
                try{
                    //addUserMongoDB(Document);
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("DELETE USER: ROLLBACK FAILED");
                    System.out.println(oldUserDocument.toString());
                }
            }
            else{
                return true;
            }
        }
        return false;
    }



    public boolean addUserMongoDB(String userId){

        return true;
    }

    public boolean addUserNeo4j(String userId){

        return true;
    }

    public boolean addUser(String userId){
        Document userDocumentToAdd = new Document();
        if(addUserMongoDB(userId)){
            if(!addUserNeo4j(userId)){
                try{
                    deleteUserMongoDB(userId);
                } catch (MongoException e) {
                    System.out.println(e.getMessage());
                    System.out.println("ADD USER: ROLLBACK FAILED");
                    System.out.println(userDocumentToAdd.toString());
                }
            }
            else{
                return true;
            }
        }
        return false;
    }


}
