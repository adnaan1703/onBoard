package com.konel.kryptapps.onboard.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.konel.kryptapps.onboard.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tushargupta on 21/06/17.
 */

class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.MyViewHolder> {

    public interface Callback {
        void click(Event data);
    }

    private List<Event> mEvents;

    public Callback callback;

    public EventsAdapter(List<Event> events) {
        this.mEvents = events;
    }

    @Override
    public EventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.MyViewHolder holder, int position) {
        holder.bind(mEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.eventName)
        TextView eventName;

        @BindView(R.id.eventDescription)
        TextView eventDescription;

        @BindView(R.id.eventLocation)
        TextView eventLocation;

        @BindView(R.id.eventImage)
        ImageView eventImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Event data) {
            eventName.setText(data.getEventName());
            eventDescription.setText(data.getEventDescription());
            eventLocation.setText(data.getEventVenue() + data.getEventDate());
            Glide.with(itemView.getContext())
                    .load(data.getUrl())
                    .into(eventImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.click(data);
                    }
                }
            });
        }
    }

}
