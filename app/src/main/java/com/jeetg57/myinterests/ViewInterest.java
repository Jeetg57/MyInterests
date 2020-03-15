package com.jeetg57.myinterests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewInterest extends AppCompatActivity {
    TextView activity;
    TextView desc;
    TextView hoursPerWeek;
    TextView timeCompleted;
    TextView createdAt;


    InterestDao interestDao;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_interest);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();
        Intent i = getIntent();
        final String interest =  i.getStringExtra("ID");


        desc = findViewById(R.id.desc);
        hoursPerWeek = findViewById(R.id.timePerWeek);
        timeCompleted = findViewById(R.id.timeCompleted);
        createdAt = findViewById(R.id.createdAt);
        final int ids = Integer.parseInt(interest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Interest thisInterest  = interestDao.findById(ids);
                activity = findViewById(R.id.txtActivity);
                activity.setText(thisInterest.interestName);
                desc.setText(thisInterest.interestDescription);
                hoursPerWeek.setText(thisInterest.hoursPerWeek + " Hours " + thisInterest.minsPerWeek + " Mins");
                timeCompleted.setText(thisInterest.hoursCompleted + " Hours " + thisInterest.minsCompleted + " Mins");

                Date currentDate = new Date(thisInterest.createdAt);
                DateFormat df = DateFormat.getTimeInstance(DateFormat.LONG);
                createdAt.setText(df.format(currentDate));


            }
        }).start();



    }
}
