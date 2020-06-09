package com.example.amour.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;;
import com.example.amour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verification extends AppCompatActivity {

    Button submitButton;
    Button refreshButton;
    String userName;
    final LoadingDialog loadingDialog = new LoadingDialog(verification.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        submitButton = findViewById(R.id.SubmitBtn);
        refreshButton = findViewById(R.id.refreshBtn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(verification.this, "Email Link Sent", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                user.reload();
                if (user.isEmailVerified()) {
                    Intent homeIntent = new Intent(verification.this, RegistrationForm.class);
                    homeIntent.putExtra("userName", userName);
                    startActivity(homeIntent);
                    loadingDialog.startLoadingDialog();
                }
            }
        });

    }
}
