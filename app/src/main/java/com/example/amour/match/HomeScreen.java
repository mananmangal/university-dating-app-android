package com.example.amour.match;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.amour.Login.Login;
import com.example.amour.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends AppCompatActivity {
    private BottomNavigationView mMainNav;
    private FrameLayout mMainframe;
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_homescreen);
        mAuth = FirebaseAuth.getInstance();
        Log.d("mmm", "onCreate: ");
        mMainframe = (FrameLayout) findViewById(R.id.mainframe);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, new MainFragment()).commit();
        //  mMainNav.inflateMenu(R.menu.nav_items);

        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {

                    case R.id.nav_profile:
                        selectedFragment = new ProfileFragment();
                        Log.i("MMM", "onNavigationItemSelected: nav_profile");
                        break;

                    case R.id.nav_amour:
                        selectedFragment = new MainFragment();
                        Log.i("MMM", "onNavigationItemSelected: nav_amour");
                        break;

                    case R.id.nav_chat:
                        selectedFragment = new ChatFragment();
                        Log.i("MMM", "onNavigationItemSelected: nav_chat");
                        break;
                }
                if (selectedFragment != null) {
                    Log.i("mmm", "onNavigationItemSelected:selected fragment ");
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainframe, selectedFragment).commit();
                    return true;
                }
                return false;

            }
        });
        mMainNav.setSelectedItemId(R.id.nav_amour);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.logout1) {
            mAuth.signOut();
            Intent loginIntent = new Intent(HomeScreen.this, Login.class);
            startActivity(loginIntent);
        }
        return true;
    }
}
