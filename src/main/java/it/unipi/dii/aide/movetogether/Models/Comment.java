package it.unipi.dii.aide.movetogether.Models;

import javafx.beans.property.*;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


public class Comment {
    //private final ObjectProperty<ObjectId> id_comment;
    private final ObjectProperty<ObjectId> id_user;
    private final StringProperty title;
    private final StringProperty content;
    private final IntegerProperty score;
    private final StringProperty name;
    private final StringProperty surname;
    private final StringProperty country;
    private final ObjectProperty<Date> comment_timestamp;



    public Comment(ObjectId id_user, String name,String surname, int score, String content,
                    String country,String title, Date comment_timestamp){
        //this.id_comment = new SimpleObjectProperty<>(this,"id_comment",id_comment);
        this.id_user = new SimpleObjectProperty<>(this,"id_user",id_user);
        this.name = new SimpleStringProperty(this,"name",name);
        this.surname = new SimpleStringProperty(this,"surname",surname);
        this.country = new SimpleStringProperty(this,"country",country);
        this.score = new SimpleIntegerProperty(this,"score",score);
        this.content = new SimpleStringProperty(this,"content",content);
        this.comment_timestamp = new SimpleObjectProperty<>(this,"date",comment_timestamp);
        this.title = new SimpleStringProperty(this,"title",title);

    }

    public Comment(Document doc){
        //id_comment not given
        this.title = new SimpleStringProperty(this,"title",doc.getString("title"));
        this.comment_timestamp = new SimpleObjectProperty<>(this,"comment_timestamp",(doc.get("comment_timestamp") == null) ? new Date(1990,1,1) : doc.getDate("comment_timestamp"));
        this.id_user = new SimpleObjectProperty<>(this,"id_user",doc.getObjectId("id_user"));
        this.content = new SimpleStringProperty(this,"content",doc.getString("content") == null? "N.G": doc.getString("content"));
        this.name = new SimpleStringProperty(this,"name",doc.getString("name") == null? "N.G": doc.getString("name"));
        this.surname = new SimpleStringProperty(this,"surname",doc.getString("surname") == null? "N.G": doc.getString("surname"));
        this.country = new SimpleStringProperty(this,"country",doc.getString("country") == null? "N.G": doc.getString("country"));
        this.score = new SimpleIntegerProperty(this,"score",(doc.getInteger("score") == null)? -1:doc.getInteger("score"));
    }




    //public ObjectProperty<ObjectId> idCommentProperty() {return this.id_comment;}
    public ObjectProperty<ObjectId> idUserProperty() {return this.id_user;}
    public StringProperty nameProperty() {return this.name;}
    public StringProperty surnameProperty() {return this.surname;}
    public StringProperty countryProperty() {return this.country;}
    public IntegerProperty scoreProperty() {return this.score;}
    public StringProperty contentProperty() {return this.content;}
    public StringProperty titleProperty() {return this.title;}
    public ObjectProperty<Date> commentTimestampProperty() { return this.comment_timestamp; }


}




