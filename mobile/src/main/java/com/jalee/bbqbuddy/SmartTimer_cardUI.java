package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */
public class SmartTimer_cardUI {
    String subTitle;
    String name;
    int id;
    int imageID;
    public String getsubTitle() {
        return subTitle;
    }
    public void setsubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getimageId() {
        return imageID;
    }
    public void setimageId(int id) {
        this.imageID = imageID;
    }
    public SmartTimer_cardUI(String subTitle, String name, int id,int imageID) {
        this.subTitle = subTitle;
        this.name = name;
        this.id = id;
        this.imageID = imageID;
    }
}
