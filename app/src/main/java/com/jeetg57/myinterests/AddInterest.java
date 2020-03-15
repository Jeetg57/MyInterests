package com.jeetg57.myinterests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddInterest extends AppCompatActivity {
    EditText txtActivity;
    EditText txtDesc;
    EditText txtHours;
    EditText txtMins;
    InterestDao interestDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_interest);
        txtActivity = findViewById(R.id.txtActivity);
        txtDesc = findViewById(R.id.txtDesc);
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
        interest.createdAt = System.currentTimeMillis();
        if(!(interest.interestName.equals("") && interest.interestDescription.equals(""))) {
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
}
