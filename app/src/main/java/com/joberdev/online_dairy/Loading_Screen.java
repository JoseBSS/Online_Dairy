package com.joberdev.online_dairy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Loading_Screen extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        int time = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*startActivity(new Intent(Loading_Screen.this, MainActivity.class));
                finish();*/
                verifyUser();
            }
        },time);
    }

    private void verifyUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            startActivity(new Intent(Loading_Screen.this, MainActivity.class));
            finish();

        }else {
            startActivity(new Intent(Loading_Screen.this, MainActivity.class ));
            finish();
        }
    }
}