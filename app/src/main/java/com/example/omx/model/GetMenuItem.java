package com.example.omx.model;

/**
 * Created by Muvi on 12/15/2016.
 */

public class GetMenuItem {


    private String name;
    private String sectionId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }



    public GetMenuItem(String name, String sectionId) {
        this.name = name;
        this.sectionId = sectionId;


    }



}
