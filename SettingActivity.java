package com.example.h2obuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private EditText etDailyGoal;
    private EditText etReminderInterval;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize views
        etDailyGoal = findViewById(R.id.etDailyGoal);
        etReminderInterval = findViewById(R.id.etReminderInterval);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve user ID from intent
        userId = getIntent().getExtras().getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load existing settings
        loadUserSettings();
    }

    private void loadUserSettings() {
        String userEmail = databaseHelper.getUserEmailById(userId);
        if (userEmail == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int dailyGoal = databaseHelper.getDailyGoal(userEmail);
        int reminderInterval = databaseHelper.getReminderInterval(userEmail);

        etDailyGoal.setText(String.valueOf(dailyGoal));
        etReminderInterval.setText(String.valueOf(reminderInterval));
    }

    public void saveSettings(View view) {
        String dailyGoalText = etDailyGoal.getText().toString().trim();
        String reminderIntervalText = etReminderInterval.getText().toString().trim();

        if (dailyGoalText.isEmpty() || reminderIntervalText.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int dailyGoal;
        int reminderInterval;

        try {
            dailyGoal = Integer.parseInt(dailyGoalText);
            reminderInterval = Integer.parseInt(reminderIntervalText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = databaseHelper.getUserEmailById(userId);
        if (userEmail == null) {
            Toast.makeText(this, "Error retrieving user", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = databaseHelper.updateUserSettings(userEmail, dailyGoal, reminderInterval);
        if (success) {
            Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show();

            // Redirect back to HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save settings", Toast.LENGTH_SHORT).show();
        }
    }
}
