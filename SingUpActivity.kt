package com.example.h2obuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etDailyGoal;
    private Button btnSignup;
    private TextView tvLogin;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etDailyGoal = findViewById(R.id.etDailyGoal);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set up the signup button click listener
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignup();
            }
        });

        // Set up the login text click listener
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleSignup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String dailyGoalStr = etDailyGoal.getText().toString().trim();

        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || dailyGoalStr.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignupActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return;
        }

        int dailyGoal;
        try {
            dailyGoal = Integer.parseInt(dailyGoalStr);
            if (dailyGoal <= 0) {
                Toast.makeText(SignupActivity.this, "Daily goal must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(SignupActivity.this, "Daily goal must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert user into the database
        boolean isInserted = databaseHelper.insertUser(name, email, password, dailyGoal);

        if (isInserted) {
            Toast.makeText(SignupActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(SignupActivity.this, "Signup failed. Email may already be registered.", Toast.LENGTH_SHORT).show();
        }
    }
}
