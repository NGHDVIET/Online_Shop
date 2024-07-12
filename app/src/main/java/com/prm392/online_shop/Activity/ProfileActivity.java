package com.prm392.online_shop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prm392.online_shop.R;

public class ProfileActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private TextView tvProfileName, tvProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        findViewById(R.id.btnEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Placeholder for edit profile action
                Toast.makeText(ProfileActivity.this, "Edit Profile clicked", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Handle back button click
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Fetch user info
        fetchUserInfo();
    }

    private void fetchUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvProfileName.setText(user.getDisplayName());
            tvProfileEmail.setText(user.getEmail());
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }
}
