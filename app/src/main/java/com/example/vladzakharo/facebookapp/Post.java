package com.example.vladzakharo.facebookapp;

/**
 * Created by Vlad Zakharo on 30.04.2016.
 */
public class Post {
    public String Message;
    public String Name;
    public String Caption;
    public String Description;
    public String Picture;
    public String Link;
    public String Id;

    public Post(String Message, String Name, String Caption, String Description, String Picture,
         String Link, String Id){
        this.Message = Message;
        this.Name = Name;
        this.Caption = Caption;
        this.Description = Description;
        this.Picture = Picture;
        this.Link = Link;
        this.Id = Id;
    }
    public String getMessage(){
        return Message;
    }
    public String getName(){
        return Name;
    }
    public String getCaption(){
        return Caption;
    }
    public String getDescription(){
        return Description;
    }
    public String getPicture(){
        return Picture;
    }
    public String getLink(){
        return Link;
    }
    public String getId(){
        return Id;
    }


}
