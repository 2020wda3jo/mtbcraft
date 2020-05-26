package com.example.gpstest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submain);

        Button startbt = (Button)findViewById(R.id.ridingstart);
        Button viewreport = (Button)findViewById(R.id.aaa);

        startbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SubActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
        viewreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SubActivity.this,MyReport.class);
                startActivity(intent);
            }
        });
    }
}
