package com.example.eventmate;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private  int id;
    @ColumnInfo(name = "eventId")
    private String eventID;
    @ColumnInfo(name = "categoryId")
    private String catID;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "ticketCount")
    private int ticket;
    @ColumnInfo(name = "status")
    private boolean isActive;

    public Event(String eventID, String catID, String name, int ticket, boolean isActive) {
        this.eventID = eventID;
        this.catID = catID;
        this.name = name;
        this.ticket = ticket;
        this.isActive = isActive;
    }

    public String getEventID() {
        return eventID;
    }

    public String getCatID() {
        return catID;
    }

    public String getName() {
        return name;
    }

    public int getTicket() {
        return ticket;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setCatID(String catID) {
        this.catID = catID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
