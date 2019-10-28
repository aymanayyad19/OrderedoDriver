package com.example.orderedodriver;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.orderedodriver.adapter.BillAdapter;
import com.example.orderedodriver.model.Order;
import com.example.orderedodriver.model.Product;

import java.util.ArrayList;
import java.util.List;

public class OrderDiscription extends AppCompatActivity {
    RecyclerView recyclerView;
    BillAdapter billAdapter;
    List<Product> product;
    Toolbar toolbar;
    Order order ;

    TextView tvNewOrderDescriptionSupTotal;
    TextView tvNewOrderDescriptionDeliveryFees;
    TextView tvNewOrderDescriptionFees;
    TextView tvNewOrderDescriptionDiscount;
    TextView tvNewOrderDescriptionFinalTotal;
    Intent intent ;
    double supTotal = 0,finalTotal = 0,fees = 0,delivery = 0,discount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_discription);
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(getResources().getString(R.string.bill));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = findViewById(R.id.rv);
        tvNewOrderDescriptionSupTotal =  findViewById(R.id.tv_new_order_description_sup_total);
        tvNewOrderDescriptionDeliveryFees =  findViewById(R.id.tv_new_order_description_delivery_fees);
        tvNewOrderDescriptionFees =  findViewById(R.id.tv_new_order_description_fees);
        tvNewOrderDescriptionDiscount =  findViewById(R.id.tv_new_order_description_discount);
        tvNewOrderDescriptionFinalTotal =  findViewById(R.id.tv_new_order_description_final_total);

        order = MapsActivity.SOrder;

        product = new ArrayList<>();
        product = order.getProducts();
        billAdapter = new BillAdapter(product, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(billAdapter);
        for (int i = 0; i < product.size();i++){
            if (i == 1)
                delivery = product.get(i).getDeliveryPrice();
            supTotal += (product.get(i).getCount()*product.get(i).getProductPrice());
            finalTotal += supTotal;
        }
            finalTotal += delivery;
        tvNewOrderDescriptionSupTotal.setText("$"+supTotal);
        tvNewOrderDescriptionDeliveryFees.setText("$"+delivery);
        tvNewOrderDescriptionFees.setText("$"+fees);
        tvNewOrderDescriptionDiscount.setText("$"+discount);
        tvNewOrderDescriptionFinalTotal.setText("$"+finalTotal);


    }
}
