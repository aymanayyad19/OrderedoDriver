package com.example.orderedodriver.model;

import com.google.firebase.firestore.GeoPoint;

public class CustomerAddress {
    GeoPoint addressLocation ;
    String addressTitle  ;
    String block ;
    String city ;
    String floor ;
    String frontOf ;
    String notes ;

    public GeoPoint getAddressLocation() {
        return addressLocation;
    }

    public void setAddressLocation(GeoPoint addressLocation) {
        this.addressLocation = addressLocation;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFrontOf() {
        return frontOf;
    }

    public void setFrontOf(String frontOf) {
        this.frontOf = frontOf;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
