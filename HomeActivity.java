package com.example.h2obuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView tvDailyGoal;
    private TextView tvProgress;
    private ProgressBar progressBar;

    private int dailyGoal = 2000; // Default daily goal in ml
    private int currentIntake = 0; // Tracks the current water intake
    private int userId = -1; // Holds the user ID passed from MainActivity

    private DatabaseHelper databaseHelper; // Database helper instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        tvDailyGoal = findViewById(R.id.tvDailyGoal);
        tvProgress = findViewById(R.id.tvProgress);
        progressBar = findViewById(R.id.progressBar);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper();

        // Get user ID from intent extras
        userId = getIntent().getExtras().getInt("USER_ID", -1);

        // Load user data from database
        loadUserData();
    }

    private void loadUserData() {
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch user data from the database
        User user = databaseHelper.getUserById(userId);
        if (user != null) {
            dailyGoal = user.dailyGoal;
            currentIntake = user.currentIntake;
            updateUI();
        } else {
            Toast.makeText(this, "User not found in database", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void addWaterIntake(int amount) {
        currentIntake += amount;

        // Ensure intake doesn't exceed daily goal
        if (currentIntake > dailyGoal) {
            currentIntake = dailyGoal;
        }

        // Save updated intake to the database
        boolean success = databaseHelper.updateUserIntake(userId, currentIntake);

        if (success) {
            updateUI(); // Update UI with the new values
        } else {
            Toast.makeText(this, "Failed to update water intake", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        tvDailyGoal.setText("Daily Goal: " + dailyGoal + " ml");
        tvProgress.setText("Progress: " + currentIntake + " / " + dailyGoal + " ml");
        progressBar.setMax(100); // Ensure progressBar max value is set to 100
        progressBar.setProgress((currentIntake * 100) / dailyGoal); // Update progress
    }

    public void addWaterIntake250(View view) {
        addWaterIntake(250);
    }

    public void addWaterIntake500(View view) {
        addWaterIntake(500);
    }

    public void gotoSettings(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void gotoHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("USER_ID", userId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class DatabaseHelper {

        private Map<Integer, User> users = new HashMap<>();

        public DatabaseHelper() {
            users.put(1, new User(1, "test@example.com", "password123", 2000, 0));
            users.put(2, new User(2, "user2@example.com", "pass456", 2500, 500));
        }

        public User getUserById(int userId) {
            return users.get(userId);
        }

        public boolean updateUserIntake(int userId, int intake) {
            User user = users.get(userId);
            if (user != null) {
                user.currentIntake = intake;
                return true;
            }
            return false;
        }
    }

    private static class User {
        int id;
        String email;
        String password;
        int dailyGoal;
        int currentIntake;

        public User(int id, String email, String password, int dailyGoal, int currentIntake) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.dailyGoal = dailyGoal;
            this.currentIntake = currentIntake;
        }
    }
}
