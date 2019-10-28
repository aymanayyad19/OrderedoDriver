package com.example.orderedodriver;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.orderedodriver.model.Order;

public class OrederStatus extends BaseActivity {
    Order order;
    TextView username,ordernum,suptotal,deleveryfees,descount,fees,finalTotal;
    ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oreder_status);
        order = MapsActivity.SOrder;
        if (!order.isDriverApprove()) {
            Button button = findViewById(R.id.btn_order_plcaed_return);
            button.setBackground(getResources().getDrawable(R.drawable.desable_btn));
            button.setEnabled(false);
        }
        initComponent();
        initData();
        initListener();
    }

    public void EndOrder(View view) {
        startActivity(new Intent(this, BarCodeReader.class));
        finish();
    }

    @Override
    public void initComponent() {
        imageView = findViewById(R.id.iv_new_order_status_user_image);
        username = findViewById(R.id.tv_new_order_status_username);
        ordernum = findViewById(R.id.tv_new_order_status_order_num);
        suptotal = findViewById(R.id.tv_new_order_status_sup_total);
        deleveryfees = findViewById(R.id.tv_new_order_status_delivery_fees);
        fees = findViewById(R.id.tv_new_order_status_fees);
        descount = findViewById(R.id.tv_new_order_discount);
        finalTotal = findViewById(R.id.tv_new_order_final_total);
    }

    @Override
    public void initData() {
        username.setText(order.getCustomerName());
        ordernum.setText(order.getProducts().get(0).getProductId().substring(0,8).toUpperCase());// for test
        suptotal.setText(order.getProducts().get(0).getTotalPrice()+"$");
        deleveryfees.setText(order.getProducts().get(0).getDeliveryPrice()+"$");
        fees.setText("0 $");
        finalTotal.setText(order.getProducts().get(0).getTotalPrice()+order.getProducts().get(0).getDeliveryPrice()+"");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
