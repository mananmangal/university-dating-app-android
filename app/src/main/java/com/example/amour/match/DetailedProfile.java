package com.example.amour.match;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.amour.R;
import com.example.amour.Util.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailedProfile extends AppCompatActivity {
    private Context mContext;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = DetailedProfile.this;
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");


        TextView profileName = findViewById(R.id.edit_pic_btn);
        ImageView profileImage = findViewById(R.id.profile_pic);
        TextView age = findViewById(R.id.age_editText);
        TextView height = findViewById(R.id.height_editText);
        Spinner gender = findViewById(R.id.gender_spinner);
        Spinner degree = findViewById(R.id.degree_spinner);
        TextView iAm = findViewById(R.id.i_am_editText);
        TextView iLike = findViewById(R.id.i_like_editText);
        TextView iAppreciate = findViewById(R.id.i_appreciate_editText);

        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        profileName.setText(user.getUsername());
        age.setText(user.getAge());
        height.setText(user.getHeight());
        gender.setSelection(getIndex(gender, user.getSex()));
        degree.setSelection(getIndex(degree, user.getDegree()));
        iAm.setText(user.getI_am());
        iLike.setText(user.getI_like());
        iAppreciate.setText(user.getI_appreciate());

        gender.setEnabled(false);
        degree.setEnabled(false);


        getImage(user.getImage_link(), profileImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getImage(String imageURL, final ImageView image) {
        StorageReference ref = mStorageRef.child(imageURL);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(image);
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
