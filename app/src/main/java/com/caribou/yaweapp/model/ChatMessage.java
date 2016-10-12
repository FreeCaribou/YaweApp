package com.caribou.yaweapp.model;

import java.util.Date;

/**
 * Created by fgerard on 07-10-16.
 */

public class ChatMessage {

    private long id;
    private String text;
    private Date postDate;
    private long id_user;
    private String author_name;

    public ChatMessage(long id, String text, Date postDate, long id_user) {
        this.id = id;
        this.text = text;
        this.postDate = postDate;
        this.id_user = id_user;
    }

    public ChatMessage(long id, String text, Date postDate, long id_user, String author_name) {
        this.id = id;
        this.text = text;
        this.postDate = postDate;
        this.id_user = id_user;
        this.author_name = author_name;
    }

    public ChatMessage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }
}
