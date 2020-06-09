package com.example.amour.match;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.amour.Login.RegistrationForm;
import com.example.amour.R;
import com.example.amour.Util.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    final String TAG = "ProfileFragment";
    TextView age_TextView, height_TextView, i_am_TextView, i_like_TextView, i_appreciate_TextView;
    Spinner gender_spinner, degree_spinner, pref_gender_spinner;
    RangeSeekBar age_range, height_range;
    String userId;
    //    ImageView image;
    FloatingActionButton edit_btn;
    ImageView profile_pic;
    private StorageReference mStorageRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        userId = mAuth.getCurrentUser().getUid();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        age_TextView = view.findViewById(R.id.age_TextView);
        height_TextView = view.findViewById(R.id.height_TextView);
        gender_spinner = view.findViewById(R.id.gender_spinner);
        degree_spinner = view.findViewById(R.id.degree_spinner);
        pref_gender_spinner = view.findViewById(R.id.pref_gender_spinner);
        age_range = view.findViewById(R.id.age_range);
        height_range = view.findViewById(R.id.height_range);
        i_am_TextView = view.findViewById(R.id.i_am_TextView);
        i_appreciate_TextView = view.findViewById(R.id.i_appreciate_TextView);
        i_like_TextView = view.findViewById(R.id.i_like_TextView);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        edit_btn = view.findViewById(R.id.edit_btn);

        DatabaseReference myRef = db.getReference();
        findUser();

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(getContext(), RegistrationForm.class);
                editProfileIntent.putExtra("action", "editProfile");
                startActivity(editProfileIntent);
            }
        });

        age_range.setEnabled(false);
        height_range.setEnabled(false);
        gender_spinner.setEnabled(false);
        degree_spinner.setEnabled(false);
        pref_gender_spinner.setEnabled(false);
    }

    private void findUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
        //name.setText(user.getUsername());
        age_TextView.setText(user.getAge());
        height_TextView.setText(user.getHeight());
        i_am_TextView.setText(user.getI_am());
        pref_gender_spinner.setSelection(getIndex(pref_gender_spinner, user.getPref_gender()));
        gender_spinner.setSelection(getIndex(gender_spinner, user.getSex()));
        degree_spinner.setSelection(getIndex(degree_spinner, user.getDegree()));
        i_appreciate_TextView.setText(user.getI_appreciate());
        i_like_TextView.setText(user.getI_like());
        age_range.setSelectedMinValue(user.getpref_age_min_val());
        age_range.setSelectedMaxValue(user.getPref_age_max_val());
        height_range.setSelectedMinValue(user.getPref_height_min_val());
        height_range.setSelectedMaxValue(user.getPref_height_max_val());
        StorageReference ref = mStorageRef.child(user.getImage_link());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String image = uri.toString();
                if (getActivity() != null)
                    Glide.with(getActivity()).load(image).into(profile_pic);
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
