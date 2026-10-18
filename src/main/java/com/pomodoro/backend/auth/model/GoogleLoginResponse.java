package com.pomodoro.backend.auth.model;

public class GoogleLoginResponse {
    private String email;
    private String name;
    private String picture;
    private String googleId;

    public GoogleLoginResponse(
            String email,
            String name,
            String picture,
            String googleId
    ) {
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
