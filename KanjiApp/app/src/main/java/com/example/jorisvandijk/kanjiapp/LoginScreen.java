package com.example.jorisvandijk.kanjiapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginScreen extends AppCompatActivity {
    private String ia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        ia = getIntent().getStringExtra("ia");

    }

    

    public void announcePlayer() {

    }
}
