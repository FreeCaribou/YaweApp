package com.caribou.yaweapp.model;


import java.util.Date;

public class CommentPicture {

    private long id;
    private String text;
    private Date postDate;
    private long id_user;
    private long id_picture;
    private String author_name;


    public CommentPicture(long id, String text, Date postDate, long id_user, long id_picture, String author_name) {
        this.id = id;
        this.text = text;
        this.postDate = postDate;
        this.id_user = id_user;
        this.id_picture = id_picture;
        this.author_name = author_name;
    }

    public CommentPicture() {
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    @Override
    public String toString() {
        return "CommentPicture{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", postDate=" + postDate +
                ", id_user=" + id_user +
                ", id_picture=" + id_picture +
                '}';
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

    public long getId_picture() {
        return id_picture;
    }

    public void setId_picture(long id_picture) {
        this.id_picture = id_picture;
    }
}
