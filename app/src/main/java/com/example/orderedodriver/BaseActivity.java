package com.example.orderedodriver;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity  extends AppCompatActivity implements View.OnClickListener{
    public abstract void initComponent();

    public abstract void initData();

    public abstract void initListener();
}
