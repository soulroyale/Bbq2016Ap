package com.jalee.bbqbuddy;

/**
 * Created by Aaron on 25/01/2016.
 */
public class SmartTimer_cardUI {
    String subTitle;
    String name;
    int id;
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
    public SmartTimer_cardUI(String subTitle, String name, int id) {
        this.subTitle = subTitle;
        this.name = name;
        this.id = id;
    }
}
