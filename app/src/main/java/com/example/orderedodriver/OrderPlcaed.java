package com.example.orderedodriver;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OrderPlcaed extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_plcaed);
//        startActivityForResult(new Intent(this,BarCodeReader.class),11);

    }

    public void goToMap(View view) {
        startActivity(new Intent(this,MapsActivity.class));
        finish();
    }

}
