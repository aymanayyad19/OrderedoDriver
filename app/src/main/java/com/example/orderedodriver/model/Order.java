package com.example.orderedodriver.model;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    boolean adminApprove ;
    CustomerAddress customerAddress ;
    String customerImage ;
    String customerName ;
    String customerPhone ;
    boolean driverApprove ;
    String driverId ;
    boolean finished ;
    boolean paymentWasMade ;
    String qrcode ;
    List<Product> products ;
    public boolean isAdminApprove() {
        return adminApprove;
    }

    public void setAdminApprove(boolean adminApprove) {
        this.adminApprove = adminApprove;
    }

    public CustomerAddress getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(CustomerAddress customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public boolean isDriverApprove() {
        return driverApprove;
    }

    public void setDriverApprove(boolean driverApprove) {
        this.driverApprove = driverApprove;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isPaymentWasMade() {
        return paymentWasMade;
    }

    public void setPaymentWasMade(boolean paymentWasMade) {
        this.paymentWasMade = paymentWasMade;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }





}
