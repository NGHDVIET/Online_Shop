package com.prm392.online_shop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.prm392.online_shop.R;

import java.util.Objects;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUp).setOnClickListener(v -> registerUser());

        // Handle Login click
        TextView loginPage = findViewById(R.id.tvLoginPage);
        loginPage.setOnClickListener(v -> {
            // Navigate to LoginActivity
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();  // Optional: close the RegisterActivity
        });
    }

    private void registerUser() {
        String name = Objects.requireNonNull(tilName.getEditText()).getText().toString().trim();
        String email = Objects.requireNonNull(tilEmail.getEditText()).getText().toString().trim();
        String password = Objects.requireNonNull(tilPassword.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(tilConfirmPassword.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            sendEmailVerification(user);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            // Optionally update the user profile
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(tilName.getEditText().getText().toString().trim())
                                    .build();
                            user.updateProfile(profileUpdates);
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
