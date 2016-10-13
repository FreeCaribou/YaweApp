package com.caribou.yaweapp.model;


import java.util.Date;

public class TweetResponse extends Tweet {

    private long id_core;

    public TweetResponse(long id, long id_user, String tweet, Date postDate, String author, long id_core) {
        super(id, id_user, tweet, postDate, author);
        this.id_core = id_core;
    }

    public TweetResponse(long id, long id_user, String tweet, Date postDate, long id_core) {
        super(id, id_user, tweet, postDate);
        this.id_core = id_core;
    }

    public TweetResponse(long id_core) {
        this.id_core = id_core;
    }

    public TweetResponse(){

    }

    public long getId_core() {
        return id_core;
    }

    public void setId_core(long id_core) {
        this.id_core = id_core;
    }
}
