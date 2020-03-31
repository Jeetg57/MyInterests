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

import java.util.Objects;

public class AddInterest extends AppCompatActivity {
    private EditText txtActivity, txtDesc, txtHours, txtMins;
    private InterestDao interestDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Add an interest");
        txtActivity = findViewById(R.id.activityDisabled);
        txtDesc = findViewById(R.id.descDisabled);
        txtHours = findViewById(R.id.txtHours);
        txtMins = findViewById(R.id.txtMins);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();
    }
    public void addInterest(View v) {
        final Interest interest = new Interest();
        interest.interestName = txtActivity.getText().toString().trim();
        interest.interestDescription = txtDesc.getText().toString().trim();
        interest.hoursPerWeek = Integer.parseInt(txtHours.getText().toString());
        interest.minsPerWeek = Integer.parseInt(txtMins.getText().toString());
        interest.hoursCompleted = 0;
        interest.minsCompleted = 0;
        interest.totalTime = 0;
        interest.createdAt = System.currentTimeMillis();
        if(!(interest.interestName.equals("") || interest.interestDescription.equals(""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    interestDao.addInterest(interest);
                }
            }).start();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Values should not be null!",Toast.LENGTH_SHORT).show();
        }
    }

    public void reset(View v){
        txtDesc.setText("");
        txtActivity.setText("");
        txtHours.setText("");
        txtMins.setText("");
        txtActivity.requestFocus();
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
