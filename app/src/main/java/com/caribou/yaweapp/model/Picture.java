package com.caribou.yaweapp.model;


public class Picture {

    private long id;
    private String url;
    private String title;
    private String description;
    private long id_user;
    // TODO attribut date si on a le temps

    public Picture(){

    }

    public Picture(long id, String url, String title, String description, long id_user) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.id_user = id_user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }
}
