package com.domoffon.efremov.domoffon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ServerErrorActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_error);

        Intent intent = getIntent();

        int defaultValue = 500;
        int code = intent.getIntExtra("code", defaultValue);
        String des = intent.getStringExtra("des");

        TextView errorView = (TextView) findViewById(R.id.text_error);
        errorView.setText("Код: " + code + ". Описание: " + des);
    }
}
