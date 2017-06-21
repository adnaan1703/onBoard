package com.konel.kryptapps.onboard.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.konel.kryptapps.onboard.Home.Event;
import com.konel.kryptapps.onboard.R;
import com.konel.kryptapps.onboard.model.User;
import com.konel.kryptapps.onboard.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tushargupta on 21/06/17.
 */

public class EventAcceptDeclineDialog extends Dialog {

    private final Event mEvent;

    @BindView(R.id.declined)
    Button declined;

    @BindView(R.id.accepted)
    Button accepted;

    @BindView(R.id.eventName)
    TextView eventName;

    @BindView(R.id.eventDescription)
    TextView eventDescription;

    @BindView(R.id.eventLocation)
    TextView eventLocation;

    @BindView(R.id.eventImage)
    ImageView eventImage;

    public EventAcceptDeclineDialog(@NonNull Context context, @NonNull Event event) {
        super(context);
        this.mEvent = event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_accept_decline_dialog);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);

        eventName.setText(mEvent.getEventName());
        eventDescription.setText(mEvent.getEventDescription());
        eventLocation.setText(mEvent.getEventVenue() + mEvent.getEventDate());
        Glide.with(getContext())
                .load(mEvent.getUrl())
                .into(eventImage);

    }

    @OnClick(R.id.declined)
    public void userDeclined() {
        dismiss();
    }

    @OnClick(R.id.accepted)
    public void userAccepted() {
        FirebaseDatabase.getInstance().getReference("users")
                .child(PreferenceUtil.getString(PreferenceUtil.USER_ID))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        updateUserEventAccepted(user, mEvent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getOwnerActivity(), "some error", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                });


        ArrayList<String> arrayList = mEvent.getAttendees();
        if(arrayList==null)
            arrayList = new ArrayList<>();
        arrayList.add(PreferenceUtil.USER_NAME);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userCollection = database.getReference("events");
        DatabaseReference userObject = userCollection.child(mEvent.getId());
        userObject.setValue(mEvent);

    }

    private void updateUserEventAccepted(User user, Event mEvent) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userCollection = database.getReference("users");
        DatabaseReference userObject = userCollection.child(PreferenceUtil.getString(PreferenceUtil.USER_ID));

        ArrayList<Event> eventsInvited = user.getEventsInvited();
        if (eventsInvited == null)
            eventsInvited = new ArrayList<>();
        eventsInvited.add(mEvent);
        user.setEventsInvited(eventsInvited);
        userObject.setValue(user);
        dismiss();
    }
}
