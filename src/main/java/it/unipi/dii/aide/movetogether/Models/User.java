package it.unipi.dii.aide.movetogether.Models;

import com.google.gson.Gson;
import javafx.beans.property.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private ObjectProperty<ObjectId> userId;
    private final StringProperty name;
    private final StringProperty surname;
    private final StringProperty nickname;
    private final StringProperty imageProfile;
    private final StringProperty gender;
    private final StringProperty city;
    private final StringProperty country;
    private final StringProperty password;
    private final IntegerProperty age;
    private final ObjectProperty<Date> date;


   public User(ObjectId userId,String name,String surname,String password,int age,Date date,
                String nickname, String country, String city, String imageProfile, String gender){//, ObjectId idUser){
       this.userId = new SimpleObjectProperty<>(this,"userId",userId);
       this.name = new SimpleStringProperty(this,"name",name);
       this.surname = new SimpleStringProperty(this,"surname",surname);
       this.gender = new SimpleStringProperty(this,"gender",gender);
       this.nickname = new SimpleStringProperty(this,"nickname",nickname);
       this.imageProfile = new SimpleStringProperty(this,"img_profile",imageProfile);
       this.city = new SimpleStringProperty(this,"city",surname);
       this.country = new SimpleStringProperty(this,"country",country);
       this.password = new SimpleStringProperty(this,"password",password);
       this.age = new SimpleIntegerProperty(this,"age",age);
       this.date = new SimpleObjectProperty<>(this,"date",date);
   }
   @SuppressWarnings("unchecked")
   public User(Document doc){
       this.userId = new SimpleObjectProperty<>(this,"userId",(doc.getObjectId("_id") == null)? new ObjectId("0000"):doc.getObjectId("_id"));
       this.name = new SimpleStringProperty(this,"name",(doc.get("name") == null) ? "" : doc.getString("name"));
       this.surname = new SimpleStringProperty(this,"surname",(doc.get("surname") == null) ? "" : doc.getString("surname"));
       this.nickname = new SimpleStringProperty(this,"nickname",(doc.get("nickname") == null) ? "" : doc.getString("nickname"));
       this.gender = new SimpleStringProperty(this,"gender",(doc.get("gender") == null) ? "" : doc.getString("gender"));
       this.imageProfile = new SimpleStringProperty(this,"imageProfile",(doc.get("img_profile") == null) ? "" : doc.getString("img_profile"));
       this.city = new SimpleStringProperty(this,"city",(doc.get("city") == null) ? "" : doc.getString("city"));
       this.country = new SimpleStringProperty(this,"country",(doc.get("country") == null) ? "" : doc.getString("country"));
       this.password = new SimpleStringProperty(this,"password",(doc.get("password") == null) ? "" : doc.getString("password"));
       this.age = new SimpleIntegerProperty(this,"age",(doc.get("age") == null) ? -1 : doc.getInteger("age"));
       this.date = new SimpleObjectProperty<Date>(this,"date",(doc.get("date") == null) ? new Date(2020, 1, 8) : doc.getDate("date"));
   }

   public Document getDocument(){
       Document result = new Document();
       result.append("_id",new ObjectId(userId.getValue().toString()))
               .append("name",name.getValue())
               .append("surname",surname.getValue())
               .append("nickname",nickname.getValue())
               .append("gender",gender.getValue())
               .append("img_profile",imageProfile.getValue())
               .append("country",country.getValue())
               .append("password",password.getValue())
               .append("age",age.getValue())
               .append("date",date.getValue());
       return result;
   }

    public ObjectProperty<ObjectId> idUserProperty(){return this.userId;}
    public StringProperty nameProperty() {return this.name;}
    public StringProperty surnameProperty() {return this.surname;}
    public StringProperty genderProperty(){return this.gender;}
    public StringProperty nicknameProperty(){return this.nickname;}
    public StringProperty imageProfileProperty(){return this.imageProfile;}
    public StringProperty cityProperty() {return this.city;}
    public StringProperty countryProperty() {return this.country;}
    public IntegerProperty ageProperty() {return this.age;}
    public StringProperty passwordProperty() {return this.password;}
    public ObjectProperty<Date> dateProperty() {return this.date;}
}


