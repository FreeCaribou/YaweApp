package com.caribou.yaweapp.ApiUrl;


public class ListOfApiUrl {

    // here will be the path of your url route for API
    // EDIT YOURSELF WITH YOUR API URL (or other url)
    private final static String URL_API_BASE = "";
    private final static String URL_UPLOAD_PICTURE = "";

    // and here all the method of all your request API

    public static String getUrlAllUser() {
        return URL_API_BASE + "users";
    }

    public static String getUrlUserByName(String username) {
        return URL_API_BASE + "user/search/" + username;
    }

    public static String getUrlUserById(String id){
        return URL_API_BASE + "user/" + id;
    }

    public static String getUrlDeleteEvent(String id) {
        return URL_API_BASE + "deleteEvent/" + id;
    }

    public static String getUrlAllEvent() {
        return URL_API_BASE + "events";
    }

    public static String getUrlDeletePicture(String id) {
        return URL_API_BASE + "deletePicture/" + id;
    }

    public static String getUrlDeleteUser(String id) {
        return URL_API_BASE + "deleteUser/" + id;
    }

    public static String getUrlAllPicture() {
        return URL_API_BASE + "pictures";
    }

    public static String getUrlAddEvent() {
        return URL_API_BASE + "addEvent";
    }

    public static String getUrlAddPicture() {
        return URL_API_BASE + "addPicture";
    }

    public static String getUrlAddUser(){
        return URL_API_BASE + "addUser";
    }

    public static String getUrlUpdatePicture(String id){
        return URL_API_BASE + "updatePicture/" + id;
    }

    public static String getUrlUpdateUserAdmin(String id){
        return URL_API_BASE + "updateUserAdmin/" + id;
    }

    public static String getUrlUpdateUserHeretic(String id){
        return URL_API_BASE + "updateUserHeretic/" + id;
    }

    public static String getUrlUpdateUserPassword(String id){
        return URL_API_BASE + "updateUserPassword/" + id;
    }

    public static String getUrlGuildMessage(){
        return URL_API_BASE + "message";
    }

    public static String getUrlEditGuildMessage(){
        return URL_API_BASE + "updateGuildMessage";
    }

    // not a url of API
    public static String getUrlUploadPicture(){
        return URL_UPLOAD_PICTURE;
    }

    public static String getUrlAllCommentPictureByIdPicture( String id_picture){
        return URL_API_BASE + "commentPicture/" + id_picture;
    }

    public static String getUrlAddCommentPicture(){
        return URL_API_BASE + "addCommentPicture";
    }

    public static String getUrlAddChatMessage(){
        return URL_API_BASE + "addChatMessage";
    }

    public static String getUrlAllChatMessage(){
        return URL_API_BASE + "chatMessage";
    }

    public static String getUrlAllUserDetail(){
        return URL_API_BASE + "userDetail";
    }

    public static String getUrlUpdateUserDetail(String id){
        return URL_API_BASE + "updateUserDetail/" + id;
    }

    public static String getUrlUserDetailById(String id){
        return URL_API_BASE + "userDetailById/" + id;
    }


}