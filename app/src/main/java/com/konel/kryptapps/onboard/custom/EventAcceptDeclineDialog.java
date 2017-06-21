package com.konel.kryptapps.onboard.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konel.kryptapps.onboard.Home.Event;
import com.konel.kryptapps.onboard.R;

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

    }

    @OnClick(R.id.accepted)
    public void userAccepted() {

    }
}
