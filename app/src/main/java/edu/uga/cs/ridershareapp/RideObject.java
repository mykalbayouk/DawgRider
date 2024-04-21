package edu.uga.cs.ridershareapp;

public class RideObject {
    private String key;
    private String destination;
    private String origin;
    private String date;

    public RideObject() {
        this.destination = null;
        this.origin = null;
        this.date = null;
        this.key = null;
    }
    public RideObject(String destination, String origin, String date) {
        this.destination = destination;
        this.origin = origin;
        this.date = date;
        this.key = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDate() {
        return date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDate(String date) {
        this.date = date;
    }






}
