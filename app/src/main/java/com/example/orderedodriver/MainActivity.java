package com.example.orderedodriver;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.orderedodriver.model.Preferances;
import com.example.orderedodriver.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dmax.dialog.SpotsDialog;

public class MainActivity extends BaseActivity {
    //firebase and firestore
    FirebaseAuth firebaseAuth;
    Preferances preferances;
    FirebaseFirestore db;
    //ui
    EditText etUsername, etPassword;
    Button btnButton;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (UtilsApp.isLogin(this)){
            startActivity(MapsActivity.getInstance(this));
            finish();
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /// for test
      //  startActivityForResult(new Intent(this,BarCodeReader.class),0);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        preferances = new Preferances(this);
        initComponent();
        initData();
        initListener();

    }

    @Override
    public void initComponent() {
        etUsername = findViewById(R.id.et_main_activity_username);
        etPassword = findViewById(R.id.et_main_activity_password);
        btnButton = findViewById(R.id.btn_main_activity_login);
        alertDialog = new SpotsDialog.Builder().setMessage("جاري التحميل...").setContext(this).build();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        btnButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_activity_login:
                login(etUsername.getText().toString()+"@orderedo.com", etPassword.getText().toString());
                break;
        }
    }

    private void login(String username, String password) {
        alertDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.e("user id => ", user.getUid());
                            getUserData(user.getUid());
                            preferances.setUId(user.getUid());
                        } else {
                            alertDialog.hide();
                            Toast.makeText(MainActivity.this, getResources().getText(R.string.wrong_user_name_or_password), Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, getResources().getText(R.string.wrong_user_name_or_password), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(String uId) {
        DocumentReference docRef = db.collection("users").document(uId);
        docRef.get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.get("type").equals("driver")) ;
                                {
                                    alertDialog.hide();
                                    User user = task.getResult().toObject(User.class) ;
                                    UtilsApp.setIsLogin(MainActivity.this,true);
                                    startActivity(MapsActivity.getInstance(MainActivity.this));finish();

                                }

                            } else {
                                alertDialog.hide();
                                Log.e("tag", "No such document");
                            }
                        } else {
                            alertDialog.hide();
                            Log.e("tag", "get failed with ", task.getException());
                        }
                        alertDialog.hide();
                    }
                }
        ).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.hide();
            }
        });
    }
}
