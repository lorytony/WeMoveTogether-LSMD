package it.unipi.dii.aide.movetogether.Models;

import javafx.beans.property.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Activity implements Cloneable{
    private StringProperty name;
    private final ObjectProperty<Date> date;
    private final ObjectProperty<ObjectId> id_activity;
    private final ObjectProperty<ObjectId> id_user;
    private final ObjectProperty<ObjectId> id_path;
    private StringProperty name_path;
    private StringProperty type_activity;
    private IntegerProperty distance;
    private IntegerProperty time_elapsed;
    private final IntegerProperty avg_bpm;
    private final IntegerProperty max_bpm;
    private final IntegerProperty kcal;


    public Activity(ObjectId id_activity,String name,ObjectId id_user, ObjectId id_path, String name_path,
                    Date date, String type_activity,
                    int distance, int time_elapsed, int avg_bpm, int max_bpm, int kcal){
        this.id_activity = new SimpleObjectProperty<>(this,"_id",id_activity);
        this.name = new SimpleStringProperty(this,"name",name);
        this.date = new SimpleObjectProperty<>(this,"date",date);
        this.id_user = new SimpleObjectProperty<>(this,"id_user",id_user);
        this.id_path = new SimpleObjectProperty<>(this,"id_path",id_path);
        this.name_path = new SimpleStringProperty(this,"name_path",name_path);
        this.type_activity = new SimpleStringProperty(this,"type_activity",type_activity);
        this.distance = new SimpleIntegerProperty(this,"distance",distance);
        this.time_elapsed = new SimpleIntegerProperty(this,"time_elapsed",time_elapsed);
        this.avg_bpm = new SimpleIntegerProperty(this,"avg_bpm",avg_bpm);
        this.max_bpm = new SimpleIntegerProperty(this,"max_bpm",max_bpm);
        this.kcal = new SimpleIntegerProperty(this,"kcal",kcal);
    }

    public Activity(Document doc){
        this.id_activity = new SimpleObjectProperty<>(this,"_id",doc.getObjectId("_id"));
        this.name = new SimpleStringProperty(this,"name",doc.getString("name"));
        this.date = new SimpleObjectProperty<>(this,"date",(doc.get("date") == null) ? new Date(1990,1,1) : doc.getDate("date"));
        this.id_user = new SimpleObjectProperty<>(this,"id_user",doc.getObjectId("id_user"));
        this.id_path = new SimpleObjectProperty<>(this,"id_path",doc.getObjectId("id_path"));
        this.name_path = new SimpleStringProperty(this,"name_path",doc.getString("name_path") == null? "N.G": doc.getString("name_path"));
        this.type_activity = new SimpleStringProperty(this,"type_activity",doc.getString("type_activity"));
        this.distance = new SimpleIntegerProperty(this,"distance",(doc.getInteger("distance") == null)? -1:doc.getInteger("distance"));
        this.time_elapsed = new SimpleIntegerProperty(this,"time_elapsed",(doc.getInteger("time_elapsed") == null)? -1:doc.getInteger("time_elapsed"));
        this.avg_bpm = new SimpleIntegerProperty(this,"avg_bpm",doc.getInteger("avg_bpm"));
        this.max_bpm = new SimpleIntegerProperty(this,"max_bpm",doc.getInteger("max_bpm"));
        this.kcal = new SimpleIntegerProperty(this,"kcal",doc.getInteger("kcal") == null? -1:doc.getInteger("kcal"));
    }

    public Activity copy(){
        Activity act = new Activity(new ObjectId(idActivityProperty().getValue().toString()),nameProperty().getValue(),
                new ObjectId(idUserProperty().getValue().toString()),
                new ObjectId(idPathProperty().getValue().toString()),
                namePathProperty().getValue(),dateProperty().getValue(),
                typeActivityProperty().getValue(),
                distanceProperty().getValue(), timeElapsedProperty().intValue(),
                avgBpmProperty().intValue(), maxBpmProperty().intValue(),
                kcalProperty().intValue()
                );

        return act;
    }

    public Document getDocument(){
        Document result = new Document()
                .append("_id",new ObjectId(id_activity.getValue().toString()))
                .append("name",name.getValue())
                .append("date",date.getValue())
                .append("id_user",id_user.getValue())
                .append("id_path",id_path.getValue())
                .append("name_path",name_path.getValue())
                .append("type_activity",type_activity.getValue())
                .append("distance",distance.getValue())
                .append("time_elapsed",time_elapsed.getValue())
                .append("avg_bpm",avg_bpm.getValue())
                .append("max_bpm",max_bpm.getValue())
                .append("kcal",kcal.getValue());

        return result;
    }


    public void setNameProperty(String s){
        this.name = new SimpleStringProperty(s);
    }

    public void setTypeProperty(String s){
        this.type_activity = new SimpleStringProperty(s);
    }

    public void setDistanceProperty(int i){
        this.distance = new SimpleIntegerProperty(i);
    }

    public ObjectProperty<ObjectId> idActivityProperty() {  return this.id_activity;    }
    public StringProperty nameProperty() {  return this.name;   }
    public ObjectProperty<Date> dateProperty() { return this.date;  }
    public ObjectProperty<ObjectId> idPathProperty() {  return this.id_path;    }
    public StringProperty namePathProperty() {        return name_path;    }
    public ObjectProperty<ObjectId> idUserProperty() {  return this.id_user;    }
    public StringProperty typeActivityProperty() {     return type_activity;    }
    public IntegerProperty timeElapsedProperty() { return this.time_elapsed;}
    public IntegerProperty avgBpmProperty() {            return avg_bpm;    }
    public IntegerProperty maxBpmProperty() {            return max_bpm;    }
    public IntegerProperty kcalProperty() {             return kcal;    }
    public IntegerProperty distanceProperty() {         return this.distance;   }

    public Object clone()
    {
        Object obj = null;
        try {
            obj = super.clone();
        }
        catch(Exception e){}
        return obj;
    }


    @Override
    public String toString(){
        String output = "";
        output = "id_activity: " + idActivityProperty().getValue().toString() + ",\n";
        output += "name_property" + nameProperty().getValue() + ",\n";
        output += "id_user" + idUserProperty().getValue().toString() + ",\n";
        output += "id_path" + idPathProperty().getValue().toString() + ",\n";
        output += "type_activity" + typeActivityProperty().getValue() + ",\n";
        output += "distance_property" + distanceProperty().getValue().toString() + ",\n";
        output += "time_elapsed" +  timeElapsedProperty().getValue().toString() + ",\n";
        output += "avg_bpm" +  avgBpmProperty().getValue().toString() + ",\n";
        output += "kcalProperty" +  kcalProperty().getValue().toString() + ",\n";
        return output;
    }

}
