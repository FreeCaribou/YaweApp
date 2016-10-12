package com.caribou.yaweapp.model;


public class UserDetail {

    private long id;
    private String favoriteRace;
    private String favoriteProfession;
    private String favoriteActiviy;
    private String gamePlayStyle;
    private String mood;
    private String authorName;

    public UserDetail() {
    }

    public UserDetail(long id, String favoriteRace, String favoriteProfession, String favoriteActiviy, String gamePlayStyle, String mood, String authorName) {
        this.id = id;
        this.favoriteRace = favoriteRace;
        this.favoriteProfession = favoriteProfession;
        this.favoriteActiviy = favoriteActiviy;
        this.gamePlayStyle = gamePlayStyle;
        this.mood = mood;
        this.authorName = authorName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFavoriteRace() {
        return favoriteRace;
    }

    public void setFavoriteRace(String favoriteRace) {
        this.favoriteRace = favoriteRace;
    }

    public String getFavoriteProfession() {
        return favoriteProfession;
    }

    public void setFavoriteProfession(String favoriteProfession) {
        this.favoriteProfession = favoriteProfession;
    }

    public String getFavoriteActiviy() {
        return favoriteActiviy;
    }

    public void setFavoriteActiviy(String favoriteActiviy) {
        this.favoriteActiviy = favoriteActiviy;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getGamePlayStyle() {
        return gamePlayStyle;
    }

    public void setGamePlayStyle(String gamePlayStyle) {
        this.gamePlayStyle = gamePlayStyle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
