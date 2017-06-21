package com.konel.kryptapps.onboard.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.konel.kryptapps.onboard.CreateEventActivity;
import com.konel.kryptapps.onboard.R;
import com.konel.kryptapps.onboard.custom.EventAcceptDeclineDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.navigation)
    BottomNavigationView navigationView;
    @BindView(R.id.create_event)
    FloatingActionButton createEvent;

    private Fragment eventFragment;
    private Fragment profileFragment;
    private static final String TAG = HomeActivity.class.getSimpleName();

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
        handleDeeplink();

        eventFragment = EventFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setHomeScreenToEventsPage();
    }

    @OnClick(R.id.create_event)
    public void createEvent() {
        Intent i = new Intent(this, CreateEventActivity.class);
        startActivity(i);
    }

    private void handleDeeplink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData data) {
                        if (data == null) {
                            Log.d(TAG, "getInvitation: no data");
                            return;
                        }

                        // Get the deep link
                        Uri deepLink = data.getLink();

                        // Extract invite
                        FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                        if (invite != null) {
                            String invitationId = invite.getInvitationId();
                        }

                        // Handle the deep link
                        // [START_EXCLUDE]
                        Log.d(TAG, "deepLink:" + deepLink);
                        if (deepLink != null) {
                            String url = deepLink.getQueryParameter("link");
                            Uri u = Uri.parse(url);
                            List<String> path = u.getPathSegments();
                            String eventId = path.get(0);
                            Log.d(TAG, eventId);
                            FirebaseDatabase.getInstance().getReference("events")
                                    .child(eventId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Event event = dataSnapshot.getValue(Event.class);
                                            if (event==null){
                                                return;
                                            }
                                            EventAcceptDeclineDialog dialog =
                                                    new EventAcceptDeclineDialog(HomeActivity.this,event);
                                            dialog.show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                        }
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "getDynamicLink:onFailure", e);
                    }
                });
    }
}
