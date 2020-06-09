package com.example.amour.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amour.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
//import com.google.firebase.auth.FirebaseAuthCollisionException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText editText_fullname1, editText_email1,editText_username1, editText_password1;
    Button signUpButton;
    boolean isError;

    final LoadingDialog loadingDialog = new LoadingDialog(Sign_up.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        editText_fullname1 = findViewById(R.id.editText_fullname1);
        editText_email1 = findViewById(R.id.editText_email1);
        editText_password1 = findViewById(R.id.editText_password1);
       // editText_username1 = findViewById(R.id.editText_username1);

        signUpButton = findViewById(R.id.sign_up_btn);
        isError = false;

        // Client-Side Validations

        editText_fullname1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                /*error_msg.setText("");
                if (!b && fullname.getText().toString().isEmpty()) {
                    fullname.setError("Please enter your full name");
                }*/
            }
        });

        editText_email1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //error_msg.setText("");
                /*if (!editText_email1.getText().toString().matches("[a-zA-Z0-9._-]+@scu.edu")) {
                    editText_email1.setError("Please enter university email");
                }*/
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_email1.getText().toString().isEmpty()) {
                    editText_email1.setError("Please enter a valid email");
                }
            }
        });

        /*
        editText_username1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //error_msg.setText("");
                String str = charSequence.toString();
                if (!str.isEmpty() && str.contains(" ")) {
                    editText_username1.setError("Space is not allowed");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_username1.getText().toString().isEmpty()) {
                    isError = true;
                    editText_username1.setError("Please enter your username");
                }
            }
        });*/

        editText_password1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //error_msg.setText("");
                //if (!password.getText().toString().isEmpty() && password.length() < 6) {
                  //  password.setError("Password must be 6 characters");
                //} else {
                    Drawable myIcon = getResources().getDrawable(R.drawable.tick_mark);
                    myIcon.setBounds(0, 0, 90, 90);
                  //  password.setError("Good", myIcon);
             //   }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editText_password1.getText().toString().isEmpty()) {
                    isError = true;
                    //password.setError("Please enter your password");
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (editText_fullname1.getText().toString().isEmpty() ||
                        editText_email1.getText().toString().isEmpty() || editText_password1.getText().toString().isEmpty()) {
                        Toast.makeText(Sign_up.this, "Please provide a valid input!!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    loadingDialog.startLoadingDialog();
                    mAuth.createUserWithEmailAndPassword(editText_email1.getText().toString().trim(), editText_password1.getText().toString().trim())
                            .addOnCompleteListener(Sign_up.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(Sign_up.this, verification.class);
                                        intent.putExtra("userName", editText_fullname1.getText().toString());
                                        startActivity(intent);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (e instanceof FirebaseAuthWeakPasswordException) {
                                Toast.makeText(Sign_up.this, "Weak Password - minimum should be 6 char!", Toast.LENGTH_LONG).show();
                            } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(Sign_up.this, "Invalid email address!", Toast.LENGTH_LONG).show();
                            } else if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(Sign_up.this, "User already exist!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Sign_up.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.e("Sign_up", e.getLocalizedMessage());
                            }
                            loadingDialog.dismissDialog();
                        }
                    });
                }
            }
        });
    }
}