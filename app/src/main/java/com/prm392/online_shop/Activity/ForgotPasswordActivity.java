package com.prm392.online_shop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.prm392.online_shop.R;

import java.util.Objects;

public class ForgotPasswordActivity extends BaseActivity {

    private static final String TAG = "ForgotPasswordActivity";
    private TextInputLayout tilEmailForgetPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tilEmailForgetPassword = findViewById(R.id.tilEmailForgetPassword);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnForgotPasswordSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });

        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginActivity
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void sendPasswordResetEmail() {
        String email = Objects.requireNonNull(tilEmailForgetPassword.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            tilEmailForgetPassword.setError("Email is required");
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Password reset email sent.");
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.tvSubmitMsg).setVisibility(View.VISIBLE);
                        } else {
                            Log.e(TAG, "sendPasswordResetEmail:failure", task.getException());
                            Toast.makeText(ForgotPasswordActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
