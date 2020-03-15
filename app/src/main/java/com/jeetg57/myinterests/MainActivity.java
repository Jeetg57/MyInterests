package com.jeetg57.myinterests;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    InterestDao interestDao;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.studentListView);
        AppDatabase db = AppDatabase.getInstance(this);
        interestDao = db.interestDao();

        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewStudent();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view, int position, long id) {
//                 ListView list = listView.getChildAt(position).findViewById(R.id.txtActivity);
                TextView textView = view.findViewById(R.id.interestID);
                String text = textView.getText().toString();
                Intent intent = new Intent(MainActivity.this, ViewInterest.class);
                intent.putExtra("ID", text);
                startActivity(intent);
            }
        });
        loadStudentData();
    }

    private void loadStudentData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Cursor cursor = interestDao.getAllInterestsCursor();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
                                R.layout.list_item_template,
                                cursor,
                                new String[] { "interest_name", "_id", "interest_desc", "hours_per_week", "mins_per_week", "hours_completed", "mins_completed"},
                                new int[] { R.id.activityDisabled, R.id.interestID, R.id.desc, R.id.hoursPerWeek, R.id.minsPerWeek, R.id.txtHoursCompleted, R.id.txtMinsCompleted});
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }

    public void addNewStudent() {
        Intent intent = new Intent(this, AddInterest.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentData();
    }
}
