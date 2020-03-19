package com.jeetg57.myinterests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class UpdateInterest extends AppCompatActivity {
    private EditText txtActivity, textDesc, txtHours, txtMins;
    private Interest thisInterest;
    private InterestDao interestDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interest);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Update Interest");

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
        thisInterest.hoursPerWeek = Integer.parseInt(String.valueOf(txtHours.getText()));
        thisInterest.minsPerWeek = Integer.parseInt(String.valueOf(txtMins.getText()));
        if(!(thisInterest.interestName.equals("") || thisInterest.interestDescription.equals(""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    interestDao.updateInterest(thisInterest);
                }
            }).start();
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), R.string.no_null_values,Toast.LENGTH_SHORT).show();
        }

    }

    public void reset(View v){
        txtHours.setText("");
        txtMins.setText("");
        txtHours.requestFocus();
    }
}
