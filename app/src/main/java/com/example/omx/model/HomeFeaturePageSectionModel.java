package com.example.omx.model;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For HomePageAsynTask
 *
 * @author MUVI
 */

public class HomeFeaturePageSectionModel {


    String title ="";
    String section_id ="";
    ArrayList<HomeFeaturePageSectionDetailsModel> homeFeaturePageSectionDetailsModel = new ArrayList<>();


    public ArrayList<HomeFeaturePageSectionDetailsModel> getHomeFeaturePageSectionDetailsModel() {
        return homeFeaturePageSectionDetailsModel;
    }

    public void setHomeFeaturePageSectionDetailsModel(ArrayList<HomeFeaturePageSectionDetailsModel> homeFeaturePageSectionDetailsModel) {
        this.homeFeaturePageSectionDetailsModel = homeFeaturePageSectionDetailsModel;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }





}
