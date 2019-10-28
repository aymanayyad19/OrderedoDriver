package com.example.orderedodriver.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.orderedodriver.R;
import com.example.orderedodriver.model.WindowInfoData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomerInfoWindow implements GoogleMap.InfoWindowAdapter {
    Context context ;
    ImageView userImage ;
    TextView cName;
    TextView cpayment;
    String name,payment,image;
    RatingBar ratingBar ;
    public CustomerInfoWindow(Context context, String name , String payment, String image){
        this.context = context;
        this.name = name ;
        this.payment = payment ;
        this.image = image ;
    }
    @Override
    public View getInfoWindow(Marker marker) {

        WindowInfoData infoData = (WindowInfoData) marker.getTag();
        if (infoData.getType().equals("order")){
            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.custom_order_window_info, null);
            cName = view.findViewById(R.id.tv_order_wi_name);
            ratingBar = view.findViewById(R.id.rb_order_wi_rating);
            userImage = view.findViewById(R.id.iv_order_wi_image);
            cName.setText(infoData.getName());
            ratingBar.setRating(infoData.getRate());
            if (image != null){
                Uri uri = Uri.parse(image);
                userImage.setImageURI(uri);
            }

            return view ;
        }else if (infoData.getType().equals("user")){
            View view = ((Activity)context).getLayoutInflater()
                    .inflate(R.layout.custom_clint_window_info, null);
            cName = view.findViewById(R.id.tv_clint_wi_name);
            cpayment = view.findViewById(R.id.tv_clint_wi_payment);
            userImage = view.findViewById(R.id.iv_clint_wi_image);
            cName.setText(infoData.getName());
            cpayment.setText((infoData.getPaymentType().equals("false"))?"الدفع عند التوصيل":"الدفع ببطاقة الفيزا");
//            if (!image.equals(null)){
//                Uri uri = Uri.parse(image);
//                userImage.setImageURI(uri);
//            }
            return view ;
        }

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}
