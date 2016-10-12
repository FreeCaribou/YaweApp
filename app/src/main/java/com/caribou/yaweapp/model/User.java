package com.caribou.yaweapp.model;

public class User {

    private long id;
    private String name;
    private String password;
    private boolean admin;
    private boolean heretic;

    public User(){
        this.admin = false;
        this.heretic = true;
        this.name = "jean-michel";
        this.password = "azery";
    }

    public User(long id, String name, String password, boolean admin, boolean heretic) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.admin = admin;
        this.heretic = heretic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isHeretic() {
        return heretic;
    }

    public void setHeretic(boolean heretic) {
        this.heretic = heretic;
    }
}
