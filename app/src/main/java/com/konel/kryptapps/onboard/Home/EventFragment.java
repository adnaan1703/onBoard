package com.konel.kryptapps.onboard.Home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.konel.kryptapps.onboard.DetailActivity;
import com.konel.kryptapps.onboard.R;
import com.konel.kryptapps.onboard.model.User;
import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    @BindView(R.id.eventsRecycler)
    RecyclerView eventsRecycler;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    TextView noInvitations;
    private List<String> eventIDs = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private List<Event> allEventsOnDB = new ArrayList<>();
    private EventsAdapter adapter = new EventsAdapter(events);

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
        progressbar.setVisibility(View.GONE);
        noInvitations = (TextView) rootView.findViewById(R.id.no_invitations_text);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.callback = new EventsAdapter.Callback() {
            @Override
            public void click(Event data) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("id", data.getId());
                getContext().startActivity(intent);
            }
        };
        eventsRecycler.setAdapter(adapter);
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (events.size() == 0) {
            progressbar.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReference("users")
                    .child(PreferenceUtil.getString(PreferenceUtil.USER_ID))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // GenericTypeIndicator<List<Event>> t = new GenericTypeIndicator<>();
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getEventsInvited() != null) {
                                eventsRecycler.setVisibility(View.VISIBLE);
                                noInvitations.setVisibility(View.GONE);
                                events.clear();
                                events.addAll(user.getEventsInvited());
                                adapter.notifyDataSetChanged();
                            } else {
                                noInvitations.setVisibility(View.VISIBLE);
                                eventsRecycler.setVisibility(View.GONE);
                            }
                            progressbar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
        }
    }

}
