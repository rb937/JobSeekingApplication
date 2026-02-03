package com.jobportal.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 1;

    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private UserType userType;

    public enum UserType {
        JOB_SEEKER, EMPLOYER
    }

    public User(String username, String password, String email, String fullName, UserType userType) {
        this.userId = idCounter++;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public UserType getUserType() {
        return userType;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}