package com.caribou.yaweapp.model;

import java.util.Date;

public class Event {

    private long id;
    private Date dateEvent;
    private String title;
    private String description;

    public Event() {
    }

    public Event(Date dateEvent, String title, String description) {
        this.dateEvent = dateEvent;
        this.title = title;
        this.description = description;
    }

    public Event(long id, Date dateEvent, String title, String description) {
        this.id = id;
        this.dateEvent = dateEvent;
        this.title = title;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toStringDetailDate(){
        String result = "Heure: " + this.dateEvent.getHours();
        result += ":" + this.dateEvent.getMinutes();
        result += " Jour: " + (this.dateEvent.getYear() + 1900);
        result += "/" + (this.dateEvent.getMonth() + 1);
        result += "/" + this.dateEvent.getDate();
        return result;
    }

    public String getHours(){
        String result = "";
        result += this.dateEvent.getHours() + ":" + this.dateEvent.getMinutes();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "dateEvent=" + dateEvent +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
