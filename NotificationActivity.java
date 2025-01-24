package com.example.h2obuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {

    private TimePicker tpWakeUpTime;
    private TimePicker tpBedTime;
    private DatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Initialize views
        tpWakeUpTime = findViewById(R.id.tpWakeUpTime);
        tpBedTime = findViewById(R.id.tpBedTime);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Retrieve user ID from intent
        userId = getIntent().getExtras().getInt("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Optionally, load existing notification preferences
        loadNotificationSettings();
    }

    private void loadNotificationSettings() {
        String userEmail = databaseHelper.getUserEmailById(userId);
        if (userEmail == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load and set existing notification times (if stored in database)
        // This example assumes a method getNotificationTimes in DatabaseHelper to fetch the times.
        // You'll need to implement it in your DatabaseHelper, if necessary.
        // For now, this example skips loading to focus on saving functionality.
    }

    public void gotoHome(View view) {
        // Get wake-up and bedtime from TimePickers
        int wakeUpHour = tpWakeUpTime.getHour();
        int wakeUpMinute = tpWakeUpTime.getMinute();
        int bedTimeHour = tpBedTime.getHour();
        int bedTimeMinute = tpBedTime.getMinute();

        // Save notification preferences to the database
        String userEmail = databaseHelper.getUserEmailById(userId);
        if (userEmail == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Example of saving the times (you need to implement saveNotificationTimes in DatabaseHelper)
        boolean success = databaseHelper.saveNotificationTimes(userEmail, wakeUpHour, wakeUpMinute, bedTimeHour, bedTimeMinute);

        if (success) {
            Toast.makeText(this, "Notification settings saved successfully", Toast.LENGTH_SHORT).show();

            // Pass the user ID to HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("USER_ID", userId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to save notification settings", Toast.LENGTH_SHORT).show();
        }
    }
}
