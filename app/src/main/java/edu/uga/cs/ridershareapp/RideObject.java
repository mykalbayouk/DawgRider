package edu.uga.cs.ridershareapp;

public class RideObject {
    private String key;
    private String destination;
    private String origin;
    private String date;

    private boolean accepted;
    private final boolean offer;
    // true = driver, false = rider

    private final String creator;

    private String acceptedBy;

    private boolean driverConfirmed;
    private boolean riderConfirmed;

    public RideObject() {
        this.destination = null;
        this.origin = null;
        this.date = null;
        this.key = null;
        this.creator = null;
        this.accepted = false;
        this.offer = false;
        this.acceptedBy = null;
    }
    public RideObject(String destination, String origin, String date, String creator, String acceptedBy, boolean accepted, boolean offer) {
        this.destination = destination;
        this.origin = origin;
        this.date = date;
        this.key = null;
        this.creator = creator;
        this.accepted = accepted;
        this.offer = offer;
        this.acceptedBy = acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public boolean getOffer() {
        return offer;
    }


    public boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
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

    public boolean isDriverConfirmed() {
        return driverConfirmed;
    }

    public void setDriverConfirmed(boolean driverConfirmed) {
        this.driverConfirmed = driverConfirmed;
    }

    public boolean isRiderConfirmed() {
        return riderConfirmed;
    }

    public void setRiderConfirmed(boolean riderConfirmed) {
        this.riderConfirmed = riderConfirmed;
    }

    @Override
    public String toString() {
        return "Destination: " + destination + " Origin: " + origin + " Date: " + date + " Creator: " + creator + " Accepted: " + accepted + " Offer: " + offer + " Key: " + key;
    }
}
