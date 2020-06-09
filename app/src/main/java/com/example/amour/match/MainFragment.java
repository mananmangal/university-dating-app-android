package com.example.amour.match;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.example.amour.R;
import com.example.amour.Util.NotificationHelper;
import com.example.amour.Util.PhotoAdapter;
import com.example.amour.Util.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    private DatabaseReference userDB, matchDB;
    private FirebaseAuth mAuth;
    public String currentUID, preferredGender;
    List<User> rowItems;
    private PhotoAdapter arrayAdapter;
    public RelativeLayout parentView;
    FloatingActionButton likeBtn, DislikeBtn;
    private NotificationHelper mNotificationHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userDB = FirebaseDatabase.getInstance().getReference("userDetails");
        matchDB = FirebaseDatabase.getInstance().getReference("Matches");
        mAuth = FirebaseAuth.getInstance();
        mNotificationHelper = new NotificationHelper(getActivity());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = (RelativeLayout) view.findViewById(R.id.activity_frame);
        LayoutInflater inflate =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.fragment_main, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        containerView.setLayoutParams(layoutParams);

        parentView.addView(containerView);
        likeBtn = view.findViewById(R.id.floatingButtonLit);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBtnClicked(view);
            }
        });
        DislikeBtn = view.findViewById(R.id.floatingButtonNope);
        DislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeBtnClicked(view);
            }
        });
        checkUserSex();
        rowItems = new ArrayList<User>();
        arrayAdapter = new PhotoAdapter(getContext(), R.layout.item, rowItems);
        updateSwipeCard();
    }


    public void checkUserSex() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.currentUID = user.getUid();
            DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("userDetails");
            usersDB.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.getKey().equals(currentUID)) {
                        preferredGender = dataSnapshot.getValue(User.class).getPref_gender().toLowerCase();
                        getPotentialMatch();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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
    }


    public void getPotentialMatch() {
        DatabaseReference potentialMatch = FirebaseDatabase.getInstance().getReference("userDetails");
        potentialMatch.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(User.class).getSex().toLowerCase().equals(preferredGender) &&
                        !dataSnapshot.child("connections").child("dislikeme").hasChild(currentUID) && !dataSnapshot.child("connections").child("likeme").hasChild(currentUID) && !dataSnapshot.getKey().equals(currentUID)) {
                    User curUser = dataSnapshot.getValue(User.class);
                    curUser.setUserId(dataSnapshot.getKey());
                    rowItems.add(curUser);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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

    private void updateSwipeCard() {
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);
        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                User obj = (User) dataObject;
                String userId = obj.getUserId();
                userDB.child(userId).child("connections").child("dislikeme").child(currentUID).setValue(true);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                User obj = (User) dataObject;
                String userId = obj.getUserId();
                userDB.child(userId).child("connections").child("likeme").child(currentUID).setValue(true);
                //check matches
                isConnectionMatch(userId);
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });
    }

    private void isConnectionMatch(String userId) {
        final String matchedUID = userId;
        DatabaseReference currentUserConnectionsDb = userDB.child(currentUID).child("connections").child("likeme").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    matchDB.child(currentUID).child(matchedUID).child("Matches").setValue("saved");
                    matchDB.child(matchedUID).child(currentUID).child("Matches").setValue("saved");
                    NotificationCompat.Builder nb = mNotificationHelper.getChannel1Notification("Amour", "You have a new match!");
                    mNotificationHelper.getManager().notify(1, nb.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void dislikeBtnClicked(View v) {
        if (rowItems.size() != 0) {
            User card_item = rowItems.get(0);

            String userId = card_item.getUserId();
            userDB.child(userId).child("connections").child("dislikeme").child(currentUID).setValue(true);

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

            Toast.makeText(getActivity(), "NOPE!", Toast.LENGTH_LONG).show();
            getPotentialMatch();
        }
    }

    public void likeBtnClicked(View v) {
        if (rowItems.size() != 0) {
            User card_item = rowItems.get(0);

            String userId = card_item.getUserId();
            userDB.child(userId).child("connections").child("likeme").child(currentUID).setValue(true);

            //check matches
            isConnectionMatch(userId);

            rowItems.remove(0);
            arrayAdapter.notifyDataSetChanged();

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

            Toast.makeText(getActivity(), "LIT!", Toast.LENGTH_LONG).show();
        }
    }
}
