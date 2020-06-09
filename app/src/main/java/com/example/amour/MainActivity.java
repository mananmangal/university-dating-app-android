package com.example.amour;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.amour.Login.Login;
import com.example.amour.match.HomeScreen;

import com.example.amour.Login.verification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        mAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUser();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        checkUser();
    }

    private void checkUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent loginIntent = new Intent(this, Login.class);
            startActivity(loginIntent);
        } else if (!currentUser.isEmailVerified()) {
            Intent verificationIntent = new Intent(this, verification.class);
            startActivity(verificationIntent);
        } else {
            Intent homeIntent = new Intent(this, HomeScreen.class);
            startActivity(homeIntent);
        }
    }
}