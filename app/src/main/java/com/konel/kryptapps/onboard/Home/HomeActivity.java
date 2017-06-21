package com.konel.kryptapps.onboard.Home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.konel.kryptapps.onboard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;

    private Fragment eventFragment;
    private Fragment profileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    setHomeScreenToEventsPage();
                    return true;
                case R.id.navigaton_profile:
                    setHomeScreenToPage();
                    return true;
            }
            return false;
        }

    };

    private void setHomeScreenToPage() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, profileFragment);
        transaction.commit();
    }

    private void setHomeScreenToEventsPage() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, eventFragment);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        eventFragment = EventFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setHomeScreenToEventsPage();
    }
}
