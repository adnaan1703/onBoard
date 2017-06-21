package com.konel.kryptapps.onboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.konel.kryptapps.onboard.Home.AttendeeAdapter;
import com.konel.kryptapps.onboard.custom.ReadMoreTextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView mName,mLocation;
    private ReadMoreTextView readMoreTextView;
    private RecyclerView mRecyclerview;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        inflateUI();
        eventId = getIntent().getStringExtra("id");
        populateUI();
    }

    private void inflateUI(){
        mName = (TextView) findViewById(R.id.eventName);
        mLocation = (TextView) findViewById(R.id.eventLocation);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        readMoreTextView = (ReadMoreTextView) findViewById(R.id.eventDescription);
    }

    private void populateUI(){
        FirebaseDatabase.getInstance().getReference("events")
                .child(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> arrayList = dataSnapshot.getValue(ArrayList.class);
                        setRecyclerView(arrayList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setRecyclerView(ArrayList<String> arrayList){
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(new AttendeeAdapter(arrayList));
    }



}
