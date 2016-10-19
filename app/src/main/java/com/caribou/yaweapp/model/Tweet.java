package com.caribou.yaweapp.model;


import java.util.Date;

public class Tweet {

    private long id;
    private long id_user;
    private String tweet;
    private Date postDate;
    private String author;
    private int nbResponse;

    public Tweet(long id, long id_user, String tweet, Date postDate, String author, int nbResponse) {
        this.id_user = id_user;
        this.id = id;
        this.tweet = tweet;
        this.postDate = postDate;
        this.author = author;
        this.nbResponse = nbResponse;
    }

    public Tweet(long id, long id_user, String tweet, Date postDate) {
        this.id = id;
        this.id_user = id_user;
        this.tweet = tweet;
        this.postDate = postDate;
    }

    public Tweet(long id, long id_user, String tweet, Date postDate, String author) {
        this.id = id;
        this.id_user = id_user;
        this.tweet = tweet;
        this.postDate = postDate;
        this.author = author;
    }

    public Tweet() {
    }

    public int getNbResponse() {
        return nbResponse;
    }

    public void setNbResponse(int nbResponse) {
        this.nbResponse = nbResponse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
