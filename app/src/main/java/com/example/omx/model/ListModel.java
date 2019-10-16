package com.example.omx.model;

public class ListModel {

    public int icon;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name;

    // Constructor.
    public ListModel(int icon, String name) {

        this.icon = icon;
        this.name = name;
    }
}