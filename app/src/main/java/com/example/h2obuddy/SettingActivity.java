package com.example.h2obuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private EditText etDailyGoal, etReminderInterval;
    private Button btnSaveSettings;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Initialize views
        etDailyGoal = findViewById(R.id.etDailyGoal);
        etReminderInterval = findViewById(R.id.etReminderInterval);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get user email (passed from previous activity)
        userEmail = getIntent().getStringExtra("userEmail");

        // Load existing settings from the database
        loadSettings();

        // Save settings button click listener
        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    // Load the current user's settings (Daily Goal and Reminder Interval)
    private void loadSettings() {
        // Retrieve saved daily goal and reminder interval from the database
        int dailyGoal = databaseHelper.getDailyGoal(userEmail);
        int reminderInterval = databaseHelper.getReminderInterval(userEmail);

        // Set the values to the EditText fields
        etDailyGoal.setText(String.valueOf(dailyGoal));
        etReminderInterval.setText(String.valueOf(reminderInterval));
    }

    // Save the user's new settings (Daily Goal and Reminder Interval)
    private void saveSettings() {
        String dailyGoalStr = etDailyGoal.getText().toString().trim();
        String reminderIntervalStr = etReminderInterval.getText().toString().trim();

        // Validate input fields
        if (dailyGoalStr.isEmpty() || reminderIntervalStr.isEmpty()) {
            Toast.makeText(SettingActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int dailyGoal;
        int reminderInterval;
        try {
            // Parse the user inputs to integers
            dailyGoal = Integer.parseInt(dailyGoalStr);
            reminderInterval = Integer.parseInt(reminderIntervalStr);
        } catch (NumberFormatException e) {
            // Handle invalid input format
            Toast.makeText(SettingActivity.this, "Invalid input. Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the settings in the database
        boolean isUpdated = databaseHelper.updateUserSettings(userEmail, dailyGoal, reminderInterval);

        // Provide feedback to the user
        if (isUpdated) {
            Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Optionally close the settings activity after saving
        } else {
            Toast.makeText(this, "Failed to save settings", Toast.LENGTH_SHORT).show();
        }
    }
}
