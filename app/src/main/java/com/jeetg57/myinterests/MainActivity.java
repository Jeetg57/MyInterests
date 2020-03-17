package com.jeetg57.myinterests;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private InterestDao interestDao;
    private TextView createdAt;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createdAt = findViewById(R.id.createdAt);
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
                                new String[] { "interest_name", "_id", "interest_desc", "created_at"},
                                new int[] { R.id.activityDisabled, R.id.interestID, R.id.desc, R.id.createdAt});
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
