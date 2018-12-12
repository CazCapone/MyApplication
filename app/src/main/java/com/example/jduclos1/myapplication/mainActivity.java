package com.example.jduclos1.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.BrokenBarrierException;

public class mainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button calendarActBtn = (Button) findViewById(R.id.calBtn);
        Button listsActBtn = (Button) findViewById(R.id.listBtn);
        Button groupsActBtn = (Button)findViewById(R.id.groupMgtBtn);


        //Opens activity within app
        groupsActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent groupMgtIntent = new Intent(getApplicationContext(), GroupMgtActivity.class);
                startActivity(groupMgtIntent);
            }
        });



        //Opens activity outside of app
        Button googleBtn = (Button) findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String google = "http://www.google.com";
                Uri webAddress = Uri.parse(google);

                Intent gotoGoogle = new Intent(Intent.ACTION_VIEW, webAddress);
                if (gotoGoogle.resolveActivity(getPackageManager()) != null){
                    startActivity(gotoGoogle);
                }
            }
        });
    }
}
