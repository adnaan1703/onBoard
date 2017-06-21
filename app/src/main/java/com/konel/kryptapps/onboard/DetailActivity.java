package com.konel.kryptapps.onboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.konel.kryptapps.onboard.custom.ReadMoreTextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mName,mLocation;
    private ReadMoreTextView readMoreTextView;
    private RecyclerView mRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        inflateUI();
    }

    private void inflateUI(){
        mName = (TextView) findViewById(R.id.eventName);
        mLocation = (TextView) findViewById(R.id.eventLocation);
        mRecyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        readMoreTextView = (ReadMoreTextView) findViewById(R.id.eventDescription);
    }

}
