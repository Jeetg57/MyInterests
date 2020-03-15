package com.jeetg57.myinterests;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    Button closeButton;
    AlertDialog.Builder builder;
    Interest thisInterest;
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
        assert interest != null;
        final int ids = Integer.parseInt(interest);
        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest = interestDao.findById(ids);
                activity = findViewById(R.id.txtActivity);
                activity.setText(thisInterest.interestName);
                desc.setText(thisInterest.interestDescription);
                String txtHours = thisInterest.hoursPerWeek + " Hours " + thisInterest.minsPerWeek + " Mins";
                String completed = thisInterest.hoursCompleted + " Hours " + thisInterest.minsCompleted + " Mins";
                hoursPerWeek.setText(txtHours);
                timeCompleted.setText(completed);

                Date currentDate = new Date(thisInterest.createdAt);
                DateFormat df = DateFormat.getTimeInstance(DateFormat.LONG);
                createdAt.setText(df.format(currentDate));


            }
        }).start();

    }
    public void deleteInterest(View v){
        builder = new AlertDialog.Builder(this);
                //Uncomment the below code to Set the message and title from the strings.xml file
                builder.setMessage("Are You Sure you want to delete?") .setTitle("DELETE");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to delete this interest?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        interestDao.deleteInterest(thisInterest);
                                    }
                                }).start();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();
            }
    }
