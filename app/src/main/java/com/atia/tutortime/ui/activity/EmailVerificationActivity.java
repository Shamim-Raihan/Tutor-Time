package com.atia.tutortime.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atia.tutortime.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private TextView verifyMessageTv, navigateToLogInTv;
    private Button resendLinkButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_varification);

        verifyMessageTv = findViewById(R.id.verification_msg_id);
        resendLinkButton = findViewById(R.id.resend_link_btn_id);
        navigateToLogInTv = findViewById(R.id.navigate_to_log_in_id);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        resendLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null && !user.isEmailVerified()) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(EmailVerificationActivity.this, "Verification Email has been sent", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EmailVerificationActivity.this, "Verification Email not sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        navigateToLogInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailVerificationActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}