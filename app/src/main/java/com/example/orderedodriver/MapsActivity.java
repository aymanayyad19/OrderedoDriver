package com.example.orderedodriver;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderedodriver.RouteBetweenTwoMarkers.FetchURL;
import com.example.orderedodriver.RouteBetweenTwoMarkers.TaskLoadedCallback;
import com.example.orderedodriver.adapter.CustomerInfoWindow;
import com.example.orderedodriver.model.Order;
import com.example.orderedodriver.model.Permissons;
import com.example.orderedodriver.model.Preferances;
import com.example.orderedodriver.model.WindowInfoData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , TaskLoadedCallback {

    private GoogleMap mMap;
    MarkerOptions marker = null;
    Marker driverLocation = null;
    LocationListener locationListener;
    LocationManager locationManager;
    CoordinatorLayout newOrderRequest;
    LatLng sydney;
    LatLng resturantLatLng;
    String TAG = "TAG";
    FirebaseFirestore db;
    Preferances preferances;
    String orderId = "";
    //Custom dialog ui
    TextView username, orderNum;
    SimpleDraweeView userImage;
    Button accept, reject;
    List<Order> orders;
    Order myOrder;
    //Polyline
    private Polyline currentPolyline;
    public static Order SOrder;
    Marker resturant;
    MarkerOptions marker1;
    Drawable circleDrawable;
    ProgressDialog dialog;
    Toolbar toolbar;
    LatLng driverLatLng;


    public static Intent getInstance(Activity mainActivity) {
        return new Intent(mainActivity, MapsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FirebaseApp.initializeApp(this);
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        preferances = new Preferances(this);

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        newOrderRequest = findViewById(R.id.layout_new_order);
        username = findViewById(R.id.tv_new_order_dialog_username);
        userImage = findViewById(R.id.iv_new_order_dialog_user_image);
        orderNum = findViewById(R.id.tv_new_order_dialog_order_num);
        accept = findViewById(R.id.btn_new_order_dialog_accept);
        reject = findViewById(R.id.btn_new_order_dialog_reject);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(MapsActivity.this, "",
                        "Loading. Please wait...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Map<String, Object> data = new HashMap<>();
                        data.put("driverId", preferances.getUId());
                        data.put("driverApprove", true);
                        db.collection("Orders").document(orderId)
                                .set(data, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                newOrderRequest.setVisibility(View.GONE);

                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                            }
                        });
                    }
                }).start();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOrderRequest.setVisibility(View.GONE);
            }
        });
        orderListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            if (!Permissons.Check_FINE_LOCATION(MapsActivity.this)) {
                //if not permisson granted so request permisson with request code
                Permissons.Request_FINE_LOCATION(MapsActivity.this, 22);
                return;
            }

        }



        initListenerLocation();

        //  orderListener();
    }

    private void initListenerLocation() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                driverLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                traking(driverLatLng);
//                mMap.clear();/
//                Toast.makeText(MapsActivity.this, "onLocationChanged", Toast.LENGTH_SHORT).show();
                sydney = new LatLng(location.getLatitude(), location.getLongitude());
                if (marker == null) {
                    marker = new MarkerOptions().icon(getMarkerIconFromDrawable(circleDrawable)).position(sydney).title("My Location");
                    driverLocation = mMap.addMarker(marker);
                } else {
                    driverLocation.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                }
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(sydney)      // Sets the center of the map to Mountain View
                        .zoom(13)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //  newOrderDilalog();


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    private void traking(LatLng driverLatLng) {

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 2000, 10, locationListener);



        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(31.501014, 34.456189))      // Sets the center of the map to Mountain View
                .zoom(10)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to North
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void addWindowInfo(LatLng latLng) {
        Marker melbourne = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Melbourne")
                .snippet("Population: 4,137,400"));
        melbourne.showInfoWindow();
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void newOrderDilalog(final Order order, final String id) {

        username.setText(order.getCustomerName());
        username.setText(order.getCustomerName());
        if (order.getCustomerImage() != null )
            if (!order.getCustomerImage().equals(""))
        Picasso.get().load(order.getCustomerImage()).into(userImage);

        CustomerInfoWindow infoWindow = new CustomerInfoWindow(MapsActivity.this,order.getCustomerName(), "" + order.isPaymentWasMade(), order.getCustomerImage());
        mMap.setInfoWindowAdapter(infoWindow);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                WindowInfoData infoData = (WindowInfoData) marker.getTag();
                if (infoData.getType().equals("order")) {
                    orderId = id;
                    SOrder  = order;
                    startActivity(new Intent(MapsActivity.this,OrderDiscription.class).putExtra("isApprove",order.isDriverApprove()));

                }else if (infoData.getType().equals("user")){
                    orderId = id;
                    SOrder  = order;
                    // show user profile

                    getProductData();


                }
//                orderNum.setText(order.);
            }
        });
        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                WindowInfoData infoData = (WindowInfoData) marker.getTag();
                SOrder  = order;
                startActivity(new Intent(MapsActivity.this,OrederStatus.class).putExtra("isApprove",order.isDriverApprove()));
                if (infoData.getType().equals("order")) {
                    SOrder  = order;
                    startActivity(new Intent(MapsActivity.this,OrderDiscription.class).putExtra("isApprove",order.isDriverApprove()));
                }else if (infoData.getType().equals("user")){
                    // show user profile
                    //for test


                }

            }
        });
         circleDrawable = ContextCompat.getDrawable(MapsActivity.this, R.drawable.order);

        sydney = new LatLng(order.getCustomerAddress().getAddressLocation().getLatitude(), order.getCustomerAddress().getAddressLocation().getLongitude());
        MarkerOptions marker = new MarkerOptions().icon(getMarkerIconFromDrawable(circleDrawable)).position(sydney).title("OrderLocation");
        if (driverLatLng != null)
        drowPlease(sydney,driverLatLng);
        Marker m = mMap.addMarker(marker);
        m.setTag(new WindowInfoData(MapsActivity.this,"user", order.getCustomerName(), "" + order.isPaymentWasMade()));
        m.showInfoWindow();



    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (resultCode == 22) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        }
    }

    public void orderListener() {

        new Thread(new Runnable() {
            @Override
            public void run() {
//                DocumentReference ref = db.document("/users/" + preferances.getUId());
                db.collection("Orders").whereEqualTo("adminApprove", true)
                        .whereEqualTo("finished", false)
                        .addSnapshotListener(MapsActivity.this, new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                orders = new ArrayList<>();
                                mMap.clear();
                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    if (doc.getString("driverId")!= null ){
                                        orders.add((Order) doc.toObject(Order.class));
                                        newOrderDilalog((Order) doc.toObject(Order.class),doc.getId());
                                        String uid =preferances.getUId() ;
                                        if (doc.getString("driverId").equals(uid)
                                        &&doc.getBoolean("driverApprove")== true) {
                                            mMap.clear();
                                            preferances.setBarcode(doc.getString("qrcode"));
                                            preferances.setCurrentOrder(doc.getId());
                                            myOrder = doc.toObject(Order.class);

                                            newOrderDilalog((Order) doc.toObject(Order.class), doc.getId());
//                                        newOrderDilalog(doc.getDocumentReference("customer"),doc.getReference(),new LatLng(doc.getGeoPoint("location").getLatitude(),doc.getGeoPoint("location").getLongitude()));
                                            break;
                                        }
                                    }
                                    else if (doc.getString("driverId") == null ){
                                        orders.add((Order) doc.toObject(Order.class));
                                        newOrderDilalog((Order) doc.toObject(Order.class),doc.getId());
                                    }



                                }
                            }
                        });
            }
        }).start();

    }


    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        Log.e("url",url);

        return url;
    }



    public void getProductData(){
        dialog = ProgressDialog.show(MapsActivity.this, "",
                "Loading. Please wait...", true);
        Log.e("getProductData","1");
        Log.e("getProductData",MapsActivity.SOrder.getProducts().get(0).getProductId());
        db.collection("Products")
                .document(MapsActivity.SOrder.getProducts().get(0).getProductId())
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        getStore(documentSnapshot.getString("storeId"));
                        Log.e("getProductData","onSuccess");
                        Log.e("getProductData",documentSnapshot.getString("storeId"));
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.e("getProductData","onFailure");
            }
        });
    }

    private void getStore(String store) {
        Log.e("getStore","2");
        db.collection("Stores")
                .document(store)
                .get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.e("getStore","onSuccess");
                        if (!SOrder.isDriverApprove()) {
                            newOrderRequest.setVisibility(View.VISIBLE);
                            username.setText(SOrder.getCustomerName());
                        }
                        Picasso.get().load(documentSnapshot.getString("store_logo")).fit().into(userImage);
                        String name = documentSnapshot.getString("name");
                        Log.e("getStore","name => "+name);
                        GeoPoint location = documentSnapshot.getGeoPoint("location");
                        int rate =0;
//                                documentSnapshot.getLong("rate").intValue();
                        resturantLatLng = new LatLng(location.getLatitude(),location.getLongitude());
                        marker1 = new MarkerOptions().icon(getMarkerIconFromDrawable(circleDrawable)).position(resturantLatLng).title("OrderLocation");
                        resturant = mMap.addMarker(marker1);
                        resturant.setTag(new WindowInfoData(MapsActivity.this,"order", name, rate+"" ));
                        resturant.showInfoWindow();
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(location.getLatitude(),location.getLongitude()))      // Sets the center of the map to Mountain View
                                .zoom(13)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        dialog.dismiss();

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.e("getStore","onFailure");
            }
        });
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    public void drowPlease(LatLng customer , LatLng driver) {
        MarkerOptions place1, place2;
        place1 = new MarkerOptions().position(customer).title("Location 1");
        place2 = new MarkerOptions().position(driver).title("Location 2");
        mMap.addMarker(place1);
        mMap.addMarker(place2);
        new FetchURL(MapsActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout :
                preferances.setUId("");
            preferances.setKeepLogin(false);
            UtilsApp.setIsLogin(this,false);
            startActivity(new Intent(MapsActivity.this,MainActivity.class));
            MapsActivity.this.finish();
            break;
        }
        return true ;

    }


}
