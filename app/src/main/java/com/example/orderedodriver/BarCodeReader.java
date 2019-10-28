package com.example.orderedodriver;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.example.orderedodriver.model.Permissons;
import com.example.orderedodriver.model.Preferances;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class BarCodeReader extends AppCompatActivity {
    SurfaceView camera_preview;
    FirebaseFirestore db;
    Preferances preferances ;
    boolean isReaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_reader);
        camera_preview = findViewById(R.id.cameraPreview);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        preferances = new Preferances(this);
        if (!Permissons.Check_CAMERA(this)) {
            //if not permisson granted so request permisson with request code
            Permissons.Request_CAMERA(this, 22);
        }
        createCameraSource();

    }
    private void createCameraSource() {

        Log.e("createCameraSource","0");
        final BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .build();

        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600, 1024).build();

        // Log.e("createCameraSource","1");
        camera_preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // Log.e("createCameraSource","2");

                if (ActivityCompat.checkSelfPermission(BarCodeReader.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                try {
                    // Log.e("createCameraSource","4");
                    cameraSource.start(camera_preview.getHolder());

                    //  Log.e("createCameraSource","5");
                } catch (IOException e) {
                    //   Log.e("createCameraSource","6");
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                /// Log.e("createCameraSource","7");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //  Log.e("createCameraSource","8");
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.e("createCameraSource","9");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                Log.e("createCameraSource","10");
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size()>0){
                    confermQRCode(barcodes.valueAt(0));
                    Log.e("createCameraSource","11");


                }
            }
        });
    }
    //ToDo confarm the barcode and go to next Activity

    private void confermQRCode(Barcode valueAt) {
        if (!isReaded) {
            String barcode = preferances.getBarcode();
            Log.e("barcode", valueAt.displayValue);
            if (barcode.equals(valueAt.displayValue)) {
                plcaedOrder();

            }else{
               // Toast.makeText(BarCodeReader.this, R.string.wrong_data, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void plcaedOrder() {

        Map<String, Object> data = new HashMap<>();
        data.put("finished", true);
        db.collection("Orders").document(preferances.getCurrentOrder())
                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isReaded =true;
                Intent intent = new Intent(BarCodeReader.this, OrderPlcaed.class);
//            intent.putExtra("barcode",valueAt);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {

                //Camera
                case 11:
                    createCameraSource();
                    break;

            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}