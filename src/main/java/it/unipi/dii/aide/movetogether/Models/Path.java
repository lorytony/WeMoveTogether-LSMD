package it.unipi.dii.aide.movetogether.Models;

import javafx.beans.property.*;
import javafx.scene.control.ChoiceBox;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

public class Path {
    private final ObjectProperty<ObjectId> id_path;
    private final StringProperty description;
    private final StringProperty name;
    private final ObjectProperty<ObjectId> id_creator;
    private final StringProperty nameCreator;
    private final IntegerProperty distance;
    private final IntegerProperty altitudeVariation;
    private final IntegerProperty indicativeDuration;
    private final StringProperty location;
    private final IntegerProperty num_comments;
    private final DoubleProperty avg_score;


    public Path(ObjectId id_path, String name, ObjectId id_creator, int distance,String nameCreator,
                int altitudeVariation,int indDuration, String location, int num_comments, double avg_score, String description){

        this.id_path = new SimpleObjectProperty<>(this,"id_path",(id_path.toString()== null)? new ObjectId(): id_path);
        this.name = new SimpleStringProperty(this,"name",(name == null) ? "NO_NAME" : name);
        this.distance = new SimpleIntegerProperty(this,"distance",(distance == 0)? -1:distance);
        this.id_creator = new SimpleObjectProperty<>(this,"id_creator",(id_creator.toString()== null)? new ObjectId(): id_creator);
        this.nameCreator = new SimpleStringProperty(this,"name_creator",(nameCreator == null) ? "" : nameCreator);
        this.indicativeDuration = new SimpleIntegerProperty(this,"indicative_duration",(indDuration == 0) ? -1 : indDuration);
        this.altitudeVariation = new SimpleIntegerProperty(this,"altitude_variation",(altitudeVariation == 0) ? -1 : altitudeVariation);
        this.location = new SimpleStringProperty(this,"location",(location == null) ? "" : location);
        this.num_comments = new SimpleIntegerProperty(this,"num_comments",(num_comments==0)? 0: num_comments);
        this.avg_score = new SimpleDoubleProperty(this,"avg_score",(avg_score == 0.0)? 0.0:avg_score);
        this.description = new SimpleStringProperty(this,"description",(description == null) ? "" : description);
    }

    public Path(Document doc){
        this.id_path = new SimpleObjectProperty<>(this,"id_path",(doc.getObjectId("_id").toString()== null)? new ObjectId(): doc.getObjectId("_id"));
        this.description = new SimpleStringProperty(this,"description",(doc.get("description") == null) ? "NO_NAME" : doc.getString("description"));
        this.name = new SimpleStringProperty(this,"name",(doc.get("name") == null) ? "NO_NAME" : doc.getString("name"));
        this.distance = new SimpleIntegerProperty(this,"distance",(doc.getInteger("distance") == null)? -1:doc.getInteger("distance"));
        this.id_creator = new SimpleObjectProperty<>(this,"id_creator",(doc.getObjectId("id_creator")== null)? new ObjectId(): doc.getObjectId("id_creator"));
        this.nameCreator = new SimpleStringProperty(this,"name_creator",(doc.get("name_creator") == null) ? "" : doc.getString("name_creator"));
        this.indicativeDuration = new SimpleIntegerProperty(this,"indicative_duration",(doc.getInteger("indicative_duration") == null) ? -1 : doc.getInteger("indicative_duration"));
        this.altitudeVariation = new SimpleIntegerProperty(this,"altitude_variation",(doc.getInteger("altitude_variation") == null) ? -1 : doc.getInteger("altitude_variation"));
        this.location = new SimpleStringProperty(this,"location",(doc.get("location") == null) ? "" : doc.getString("location"));
        this.num_comments = new SimpleIntegerProperty(this,"num_comments",(doc.getInteger("num_comments") == null)? -1:doc.getInteger("num_comments"));
        this.avg_score = new SimpleDoubleProperty(this,"avg_score",(doc.getDouble("avg_score") == null)? -1.0:doc.getDouble("avg_score"));
    }

    public ObjectProperty<ObjectId> idPathProperty() {return this.id_path;}
    public StringProperty nameProperty() {return this.name;}
    public IntegerProperty distanceProperty() {return this.distance;}
    public ObjectProperty<ObjectId> idCreatorProperty() {return this.id_creator;}
    public StringProperty descriptionProperty() {return this.description; }
    public StringProperty nameCreatorProperty() {return this.nameCreator; }
    public IntegerProperty indicativeDurationProperty() {return this.indicativeDuration; }
    public IntegerProperty altitudeVariationProperty() {return this.altitudeVariation; }
    public StringProperty locationProperty() {return this.location; }
    public IntegerProperty numCommentsProperty() {return this.num_comments;}
    public DoubleProperty avgScoreProperty() {return this.avg_score;}
}
