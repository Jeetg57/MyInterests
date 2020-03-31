package com.jeetg57.myinterests;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.MessageFormat;

public class ViewInterest extends AppCompatActivity {
    private TextView activity;
    private TextView desc;
    private TextView hoursPerWeek;
    private TextView timeCompleted;
    private TextView createdAt;
    private TextView percentageProgress;
    private TextView totalTime;
    private TextView rept;
    private TextView daysToGo;
    AlertDialog.Builder builder;
    private Interest thisInterest;
    private InterestDao interestDao;
    private final Handler handler = new Handler();
    private int ids;
    private float tots, comps;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_interest);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();
        Intent i = getIntent();
        final String interest = i.getStringExtra("ID");
        rept = findViewById(R.id.reptOne);
        percentageProgress = findViewById(R.id.percentageProgress);
        desc = findViewById(R.id.desc);
        hoursPerWeek = findViewById(R.id.timePerWeek);
        timeCompleted = findViewById(R.id.timeCompleted);
        createdAt = findViewById(R.id.createdAt);
        daysToGo = findViewById(R.id.txtDaysToGo);
        assert interest != null;
        ids = Integer.parseInt(interest);
        loadData();
        loadReport();
    }

    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest = interestDao.findById(ids);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity = findViewById(R.id.activityDisabled);
                        totalTime = findViewById(R.id.total_time);
                        activity.setText(thisInterest.interestName);
                        desc.setText(thisInterest.interestDescription);
                        String txtHours = thisInterest.hoursPerWeek + " Hours " + thisInterest.minsPerWeek + " Mins";
                        String completed = thisInterest.hoursCompleted + " Hours " + thisInterest.minsCompleted + " Mins";
                        String totalHours = ((thisInterest.hoursCompleted*60 + thisInterest.minsCompleted)/60 + " Hours " + (thisInterest.minsCompleted)%60 + " Mins");
                        totalTime.setText(totalHours);
                        hoursPerWeek.setText(txtHours);
                        timeCompleted.setText(completed);
                        long difference = System.currentTimeMillis() - thisInterest.createdAt;
                        int daysBetween = (int) (difference / (1000 * 60 * 60 * 24));
                        int days2Go = 7 - daysBetween;
                        if (daysBetween == 0) {
                            createdAt.setText(R.string.today);
                        } else {
                            String days = daysBetween + " days ago";
                            createdAt.setText(days);
                        }
                        daysToGo.setText(days2Go + " days to reset");
                        if (days2Go == 0) {
                            resetInterest();
                        }
                    }
                });
            }
        }).start();
    }

    private void loadReport() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest = interestDao.findById(ids);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tots = ((thisInterest.hoursPerWeek * 60) + thisInterest.minsPerWeek);
                        comps = ((thisInterest.hoursCompleted * 60) + thisInterest.minsCompleted);
                        double percentageProgress2 = (comps / tots * 100);

                        @SuppressLint("DefaultLocale") String percent = String.format("%.2f", percentageProgress2);
                        percentageProgress.setText(MessageFormat.format("{0}%", percent));
                        String value;
                        if (percentageProgress2 == 0) {
                            value = getString(R.string.not_started) + " " +thisInterest.interestName;
                            rept.setText(value);
                        } else if (percentageProgress2 == 100) {
                            rept.setText(R.string.not_completed);
                        } else if (percentageProgress2 > 100) {
                            rept.setText(R.string.surpassed);
                        } else if (percentageProgress2 >= 75) {
                            rept.setText(R.string.almost_done);
                        } else if (percentageProgress2 >= 50) {
                            rept.setText(R.string.halfway);
                        } else {
                            rept.setText(R.string.keep_working);
                        }

                        circularProgressBar = findViewById(R.id.circularProgressBar);
                        // Set Progress
                        //        circularProgressBar.setProgress(100f);
                        // or with animation
                        circularProgressBar.setProgressWithAnimation(comps, 1000L); // =1s
                        // Set Progress Max
                        circularProgressBar.setProgressMax(tots);
                        // Set ProgressBar Color
                        circularProgressBar.setProgressBarColor(Color.rgb(5, 36, 51));
                        // or with gradient
//                          circularProgressBar.setProgressBarColorStart(Color.GRAY);
//                        circularProgressBar.setProgressBarColorEnd(Color.RED);
//                        circularProgressBar.setProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

                        // Set background ProgressBar Color
                        circularProgressBar.setBackgroundProgressBarColor(Color.argb(30, 7, 54, 76));
                        // or with gradient
//                        circularProgressBar.setBackgroundProgressBarColorStart(Color.WHITE);
//                        circularProgressBar.setBackgroundProgressBarColorEnd(Color.RED);
//                        circularProgressBar.setBackgroundProgressBarColorDirection(CircularProgressBar.GradientDirection.TOP_TO_BOTTOM);

                        // Set Width
                        circularProgressBar.setProgressBarWidth(10f); // in DP
                        circularProgressBar.setBackgroundProgressBarWidth(10f); // in DP

                        // Other
                        circularProgressBar.setRoundBorder(true);
                        circularProgressBar.setStartAngle(0f);
                        circularProgressBar.setProgressDirection(CircularProgressBar.ProgressDirection.TO_RIGHT);

                    }
                });
            }
        }).start();


    }

    private void deleteInterest() {
        builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click

        builder.setMessage("You will not be able to revert this!")
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
        alert.setIcon(R.drawable.error);
        alert.setTitle("Are you sure you want to delete?");
        alert.show();
    }

    public void addAchievement(View v) {
        Intent intent = new Intent(this, AddAchievement.class);

        intent.putExtra("INTEREST", thisInterest.interestId);
        startActivity(intent);
    }

    public void updateInterests(View v) {
        Intent intent = new Intent(this, UpdateInterest.class);

        intent.putExtra("INTEREST", thisInterest.interestId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        loadReport();
    }

    private void resetInterest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest.minsCompleted = 0;
                thisInterest.hoursCompleted = 0;
                interestDao.updateInterest(thisInterest);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_interest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_mi){
            deleteInterest();
            return true;
        }
        else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        else{
           return super.onOptionsItemSelected(item);
        }
    }


}
