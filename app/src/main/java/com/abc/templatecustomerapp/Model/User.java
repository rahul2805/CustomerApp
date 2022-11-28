package com.abc.templatecustomerapp.Model;

public class User {
    private static User mUser;
    private String username, psswd, name, email, id, token, address, contact;
    private User(String username, String psswd, String name, String email, String id, String address, String contact, String token) {
        this.username = username;
        this.psswd = psswd;
        this.name = name;
        this.email = email;
        this.id = id;
        this.token = token;
        this.address = address;
        this.contact = contact;
    }
    public static User getUserInstance(String username, String psswd, String name, String email, String id, String address, String contact, String token) {
        mUser = new User(username, psswd, name, email, id, address, contact, token);
        return  mUser;
    }
    public static User getUserInstance() {
        return  mUser;
    }

    public String getUsername() {
        return mUser.username;
    }

    public String getPsswd() {
        return mUser.psswd;
    }

    public String getName() {
        return mUser.name;
    }

    public String getEmail() {
        return mUser.email;
    }

    public String getId() {
        return mUser.id;
    }

    public String getToken() {
        return mUser.token;
    }

    public String getAddress() {
        return mUser.address;
    }

    public String getContact() {
        return mUser.contact;
    }

    public void makeNull() {
        mUser = null;
    }
}
