package com.jeetg57.myinterests;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class UpdateInterest extends AppCompatActivity {
    private EditText txtActivity, textDesc, txtHours, txtMins;
    private Interest thisInterest;
    private InterestDao interestDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interest);
        AppDatabase db = AppDatabase.getInstance(this);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        interestDao = db.interestDao();
        getSupportActionBar().setSubtitle("Update Interest");
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
        String activityName = txtActivity.getText().toString().trim();
        String desc = textDesc.getText().toString().trim();
        if(!((activityName.isEmpty()|| desc.isEmpty()) || txtHours.getText().toString().isEmpty() || txtMins.getText().toString().isEmpty())) {
            if(Integer.parseInt(txtMins.getText().toString()) > 59 || Integer.parseInt(txtHours.getText().toString()) >= 24){
                Toast.makeText(this, "Wrong values for minutes or hours", Toast.LENGTH_SHORT).show();
            }
            else {
                thisInterest.interestName = activityName;
                thisInterest.interestDescription = desc;
                thisInterest.hoursPerWeek = Integer.parseInt(String.valueOf(txtHours.getText()));
                thisInterest.minsPerWeek = Integer.parseInt(String.valueOf(txtMins.getText()));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        interestDao.updateInterest(thisInterest);
                    }
                }).start();
                finish();
            }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}
