package com.example.orderedodriver.model;

import android.app.Activity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WindowInfoData {
    String type;
    String title;
    String name;
    int rate;
    String paymentType;
    FirebaseFirestore db;
    Activity activity;

    public WindowInfoData(String type, String title, int rate) {
        this.type = type;
        this.title = title;
        this.rate = rate;

    }

    public WindowInfoData(Activity activity, String type, String name, String paymentType) {
        this.type = type;
        this.name = name;
        this.paymentType = paymentType;
        this.activity = activity;

//        db = FirebaseFirestore.getInstance();
//        db.collection("Products")
//                .document(MapsActivity.SOrder.getProducts().get(0).getProductId())
//                .get()
//                .addOnSuccessListener(activity, new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        getStore(documentSnapshot.getString("storeId"));
//
//                    }
//                }).addOnFailureListener(activity, new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//        SOrder
    }

    private void getStore(String store) {
        db.collection("Stores").document(store)
                .get()
                .addOnSuccessListener(activity, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
