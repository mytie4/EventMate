package com.example.assignment1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int id;

    @ColumnInfo(name = "categoryId")
    private String catID;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "eventCount")
    private int eventCount;
    @ColumnInfo(name = "status")
    private boolean isActive;

    @ColumnInfo(name = "eventLocation")
    private String eventLocation;

    public Category(String catID, String name, int eventCount, boolean isActive, String eventLocation) {
        this.catID = catID;
        this.name = name;
        this.eventCount = eventCount;
        this.isActive = isActive;
        this.eventLocation = eventLocation;
    }

    public String getCatID() {
        return catID;
    }

    public String getName() {
        return name;
    }

    public int getEventCount() {
        return eventCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }
}
