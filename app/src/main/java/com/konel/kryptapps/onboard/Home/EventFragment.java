package com.konel.kryptapps.onboard.Home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konel.kryptapps.onboard.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    // All views
    @BindView(R.id.sometext)
    TextView someText;

    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance() {

        Bundle args = new Bundle();

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, rootView);

        someText.setText("Events Page");

        return rootView;
    }

}
