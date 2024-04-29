package edu.uga.cs.ridershareapp;

public class UserObject {
    private String key;
    private String email;
    private int points;

    public UserObject() {
        this.key = null;
        this.email = null;
        this.points = 50;
    }
    public UserObject(String email, int points) {
        this.key = null;
        this.email = email;
        this.points = points;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    @Override
    public String toString() {
        return "Email: " + email + " Points: " + points + " Key: " + key;
    }
}