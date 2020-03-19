package com.jeetg57.myinterests;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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


    public void updateInterest(View v) {
        thisInterest.hoursCompleted += Integer.parseInt(txtHours.getText().toString());
        thisInterest.minsCompleted += Integer.parseInt(txtMins.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    interestDao.updateInterest(thisInterest);
                }
            }).start();
            finish();
        }

    public void reset(View v){
        txtHours.setText("");
        txtMins.setText("");
        txtHours.requestFocus();
    }
}
