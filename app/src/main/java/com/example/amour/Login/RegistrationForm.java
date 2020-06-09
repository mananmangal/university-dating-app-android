package com.example.amour.Login;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.amour.R;
import com.example.amour.match.HomeScreen;
import com.example.amour.match.ProfileFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.amour.Util.User;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.hdodenhof.circleimageview.CircleImageView;


public class RegistrationForm extends AppCompatActivity implements View.OnClickListener {
    Button save_btn;
    FloatingActionButton add_profile_pic;
    private static final int SELECT_PICTURE = 100;
    CircleImageView profile_pic;
    Spinner gender_spinner, degree_spinner, pref_gender_spinner;
    RangeSeekBar age_range, height_range;
    EditText age_editText, height_editText, i_am_editText, i_like_editText, i_appreciate_editText;
    private StorageReference mStorageRef;
    public Uri imageuri;
    String TAG = "Registration Form!!";
    FirebaseDatabase db;
    private FirebaseAuth mAuth;
    String userName;
    boolean isEdit = false;
    boolean isImageChanged = false;
    LoadingDialog loadingDialog;


    String userId, image_link;
    int pref_age_min_val, pref_age_max_val, pref_height_min_val, pref_height_max_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        add_profile_pic = findViewById(R.id.add_profile_picture);
        age_editText = findViewById(R.id.age_editText);
        height_editText = findViewById(R.id.height_editText);
        gender_spinner = findViewById(R.id.gender_spinner);
        degree_spinner = findViewById(R.id.degree_spinner);
        pref_gender_spinner = findViewById(R.id.pref_gender_spinner);
        age_range = findViewById(R.id.age_range);
        height_range = findViewById(R.id.height_range);
        i_am_editText = findViewById(R.id.i_am_editText);
        i_appreciate_editText = findViewById(R.id.i_appreciate_editText);
        i_like_editText = findViewById(R.id.i_like_editText);
        profile_pic = findViewById(R.id.profile_pic);
        save_btn = findViewById(R.id.button);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(RegistrationForm.this);

        userId = mAuth.getCurrentUser().getUid();
        final Intent intent = getIntent();

        if (intent.hasExtra("action")) {
            isEdit = true;
            findUser();
        } else {
            if (intent.hasExtra("userName")) {
                userName = intent.getStringExtra("userName");
            } else {
                userName = "Alex";
            }
        }

        age_editText.setOnClickListener(this);
        height_editText.setOnClickListener(this);
        i_am_editText.setOnClickListener(this);
        i_appreciate_editText.setOnClickListener(this);
        i_like_editText.setOnClickListener(this);

        age_range.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pref_age_min_val = (Integer) minValue;
                pref_age_max_val = (Integer) maxValue;
            }
        });

        height_range.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {
                pref_height_min_val = (Integer) minValue;
                pref_height_max_val = (Integer) maxValue;
            }
        });


        add_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageChanged = true;
                fileChooser();
            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            // Write a message to the database

            //getcontext.getcurrentuser
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    loadingDialog.startLoadingDialog();
                    saveData();
                    if (isEdit) {
                        onBackPressed();
//                        Fragment fragment = new ProfileFragment();
//                        getSupportFragmentManager().beginTransaction().replace(R.id.registration_form, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                    } else {
                        Intent intent = new Intent(RegistrationForm.this, HomeScreen.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        if (age_editText.getText().toString().isEmpty()) {
            age_editText.setError("Please enter your age.");
            return false;
        }

        if (height_editText.getText().toString().isEmpty()) {
            height_editText.setError("Please enter your Height.");
            return false;
        }

        if (i_am_editText.getText().toString().isEmpty()) {
            i_am_editText.setError("Tell your date about you!");
            return false;
        }

        if (i_like_editText.getText().toString().isEmpty()) {
            i_like_editText.setError("Help us find you your best match!");
            return false;
        }

        if (i_appreciate_editText.getText().toString().isEmpty()) {
            i_appreciate_editText.setError("Let your date know how to impress you.");
            return false;
        }

        if (imageuri == null && !isEdit) {
            Toast.makeText(RegistrationForm.this, "Please select an image for profile picture.!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void saveData() {
        if (isImageChanged) {
            image_link = System.currentTimeMillis() + "." + getExtension(imageuri);
            fileUploader();
        }
        User userDetails = new User(userName, age_editText.getText().toString(), height_editText.getText().toString(),
                i_am_editText.getText().toString(), i_appreciate_editText.getText().toString(), i_like_editText.getText().toString(),
                pref_age_min_val, pref_age_max_val, pref_height_min_val, pref_height_max_val, pref_gender_spinner.getSelectedItem().toString(),
                image_link, gender_spinner.getSelectedItem().toString(), degree_spinner.getSelectedItem().toString());

        DatabaseReference myRef = db.getReference("userDetails");
        myRef.child(mAuth.getCurrentUser().getUid()).setValue(userDetails);
        if (!isEdit) {
            fileUploader();
        }
        Toast.makeText(RegistrationForm.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader() {
        StorageReference ref = mStorageRef.child(image_link);

        ref.putFile(imageuri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(RegistrationForm.this, exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            profile_pic.setImageURI(imageuri);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.age_editText:
                if (age_editText.getText().toString().isEmpty()) {
                    age_editText.setError("Please enter your age!");
                }
                break;
            case R.id.height_editText:
                if (height_editText.getText().toString().isEmpty()) {
                    height_editText.setError("Please enter your height!");
                }
                break;
            case R.id.i_am_editText:
                if (i_am_editText.getText().toString().isEmpty()) {
                    i_am_editText.setError("Please enter your height!");
                }
                break;
        }
    }

    private void findUser() {
        DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("userDetails");
        usersDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals(userId)) {
                    getUserPhotoAndName(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getUserPhotoAndName(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getUserPhotoAndName(DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        userName = user.getUsername();
        age_editText.setText(user.getAge());
        height_editText.setText(user.getHeight());
        i_am_editText.setText(user.getI_am());
        pref_gender_spinner.setSelection(getIndex(pref_gender_spinner, user.getPref_gender()));
        gender_spinner.setSelection(getIndex(gender_spinner, user.getSex()));
        degree_spinner.setSelection(getIndex(degree_spinner, user.getDegree()));
        i_appreciate_editText.setText(user.getI_appreciate());
        i_like_editText.setText(user.getI_like());
        age_range.setSelectedMinValue(user.getpref_age_min_val());
        age_range.setSelectedMaxValue(user.getPref_age_max_val());
        pref_age_min_val = (Integer) user.getpref_age_min_val();
        pref_age_max_val = (Integer) user.getPref_age_max_val();
        height_range.setSelectedMinValue(user.getPref_height_min_val());
        height_range.setSelectedMaxValue(user.getPref_height_max_val());
        pref_height_min_val = (Integer) user.getPref_height_min_val();
        pref_height_max_val = (Integer) user.getPref_height_max_val();
        image_link = user.getImage_link();
        StorageReference ref = mStorageRef.child(user.getImage_link());
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String image = uri.toString();
                Glide.with(getApplicationContext()).load(image).into(profile_pic);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }
}
