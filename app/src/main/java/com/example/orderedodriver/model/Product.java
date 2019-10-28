package com.example.orderedodriver.model;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Product
{
    int count;
    double deliveryPrice ;
    double distanceBetweenStoreAndCustomer ;
    String image ;
    String name;
    List<Option> options;
    String productId ;
    double productPrice ;
    GeoPoint storeLocation ;
    double totalPrice ;

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public double getDistanceBetweenStoreAndCustomer() {
        return distanceBetweenStoreAndCustomer;
    }

    public void setDistanceBetweenStoreAndCustomer(double distanceBetweenStoreAndCustomer) {
        this.distanceBetweenStoreAndCustomer = distanceBetweenStoreAndCustomer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public GeoPoint getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(GeoPoint storeLocation) {
        this.storeLocation = storeLocation;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
