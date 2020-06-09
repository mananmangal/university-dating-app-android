package com.example.amour.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText reset_password_Email;
    Button reset_password_btn;
    private FirebaseAuth mAuth      ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reset_password);
        reset_password_Email = findViewById(R.id.reset_password_Email);
        reset_password_btn = findViewById(R.id.reset_password_btn);
        mAuth = FirebaseAuth.getInstance();

        reset_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail =  reset_password_Email.getText().toString();
                if (userEmail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter an email address", Toast.LENGTH_LONG).show();
                } else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Email Sent ", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ResetPasswordActivity.this, Login.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                        }
                    });
                }
            }
        });
    }
}
