package com.jeetg57.myinterests;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;
import java.util.Random;

public class AddAchievement extends AppCompatActivity {
    private EditText txtHours, txtMins;
    private Interest thisInterest;
    private InterestDao interestDao;
    TextView quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_achievement);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();
        Objects.requireNonNull(getSupportActionBar()).setSubtitle("Add an achievement");

        txtHours = findViewById(R.id.txtHours);
        txtMins = findViewById(R.id.txtMins);
        quote = findViewById(R.id.randomQuote);

        String[] quotes = {"Keep away from those who try to belittle your ambitions. Small people always do that, but the really great make you believe that you too can become great.",
                "Things aren’t always easy, but you just have to keep going and don’t let the small stuff bog you down.",
                "Let everything happen to you. Beauty and terror. Just keep going. No feeling is final.",
                "When in doubt, throw doubt out and have a little faith….",
                "Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time."
        };
        Random rand = new Random();
        int qt = rand.nextInt(quotes.length);
        quote.setText(quotes[qt]);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("INTEREST", 0);

        new Thread(new Runnable() {
            @Override
            public void run() {
                thisInterest = interestDao.findById(id);
            }
        }).start();
    }


    public void updateInterest(View v){
        if(!(txtHours.getText().toString().isEmpty() || txtMins.getText().toString().isEmpty())) {
            if(Integer.parseInt(txtMins.getText().toString()) > 59 || Integer.parseInt(txtHours.getText().toString()) >= 24){
                Toast.makeText(this, "Wrong values for minutes or hours", Toast.LENGTH_SHORT).show();
            }
            else {
                thisInterest.hoursCompleted += Integer.parseInt(txtHours.getText().toString());
                thisInterest.minsCompleted += Integer.parseInt(txtMins.getText().toString());
                thisInterest.totalTime += (thisInterest.hoursCompleted * 60 + thisInterest.minsCompleted);
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
            Toast.makeText(this, R.string.no_null_values, Toast.LENGTH_SHORT).show();
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
