package com.example.jorisvandijk.kanjiapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    private String inetAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect() {
        EditText editAddres = (EditText) findViewById(R.id.inetInput);
        inetAdd = editAddres.getText().toString();
        goToLoginScreen();
    }

    private void goToLoginScreen(){
        Intent intent = new Intent(this, LoginScreen.class);
        intent.putExtra("ia", inetAdd);
        startActivity(intent);
    }


}
