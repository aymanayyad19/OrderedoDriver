package com.example.orderedodriver;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class NewOrderDialog extends Dialog {

    public Activity activity;
    public Dialog dialog;
    public Button yes, no;

    public NewOrderDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.now_order_dialog);
//
//        yes = (Button) findViewById(R.id.btn_yes);
//        no = (Button) findViewById(R.id.btn_no);
//
//        yes.setOnClickListener(new OnClickListener() {
//            public void onClick(View v)
//            {
//                activity.finish();
//            }
//        });
//
//        no.setOnClickListener(new OnClickListener() {
//            public void onClick(View v)
//            {
//                dismiss();
//            }
//        });

    }

}