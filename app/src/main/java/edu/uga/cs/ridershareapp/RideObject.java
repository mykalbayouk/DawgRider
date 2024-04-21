package edu.uga.cs.ridershareapp;

public class RideObject {
    private String key;
    private String destination;
    private String origin;
    private String date;

    private final String creator;

    public RideObject() {
        this.destination = null;
        this.origin = null;
        this.date = null;
        this.key = null;
        this.creator = null;
    }
    public RideObject(String destination, String origin, String date, String creator) {
        this.destination = destination;
        this.origin = origin;
        this.date = date;
        this.key = null;
        this.creator = creator;
    }


    public String getCreator() {
        return creator;
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

    @Override
    public String toString() {
        return "Destination: " + destination + " Origin: " + origin + " Date: " + date + " Creator: " + creator;
    }






}
