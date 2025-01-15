package com.example.h2obuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvHistory;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize views
        lvHistory = findViewById(R.id.lvHistory);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get user email (passed from previous activity)
        userEmail = getIntent().getStringExtra("userEmail");

        // Fetch and display history
        loadWeeklyHistory();
    }

    private void loadWeeklyHistory() {
        // Fetch weekly water intake history from database
        List<String> historyList = databaseHelper.getWeeklyHistory(userEmail);

        if (historyList.isEmpty()) {
            Toast.makeText(this, "No history available", Toast.LENGTH_SHORT).show();
        } else {
            // Create an ArrayAdapter to display history in ListView
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
            lvHistory.setAdapter(adapter);
        }
    }
}
