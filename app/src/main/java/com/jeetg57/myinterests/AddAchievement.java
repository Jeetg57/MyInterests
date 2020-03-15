package com.jeetg57.myinterests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class AddAchievement extends AppCompatActivity {
    EditText txtActivity;
    EditText textDesc;
    EditText txtHours;
    EditText txtMins;
    Interest thisInterest;
    InterestDao interestDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievement);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();


        txtActivity = findViewById(R.id.activityDisabled);
        textDesc = findViewById(R.id.descDisabled);
        txtHours = findViewById(R.id.txtHours);
        txtMins = findViewById(R.id.txtMins);


        Intent intent = getIntent();
        final int id = intent.getIntExtra("INTEREST", 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest = interestDao.findById(id);
                txtActivity.setText(thisInterest.interestName);
                textDesc.setText(thisInterest.interestDescription);
            }
        }).start();



    }
    public void updateInterest(View v) {
        thisInterest.interestName = txtActivity.getText().toString().trim();
        thisInterest.interestDescription = textDesc.getText().toString().trim();
        thisInterest.hoursCompleted = Integer.parseInt(txtHours.getText().toString());
        thisInterest.minsCompleted = Integer.parseInt(txtMins.getText().toString());
        if(!(thisInterest.interestName.equals("") && thisInterest.interestDescription.equals(""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    interestDao.updateInterest(thisInterest);
                }
            }).start();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(),"Values should not be null!",Toast.LENGTH_SHORT).show();
        }

    }
}
