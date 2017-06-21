package com.konel.kryptapps.onboard.Home;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tushargupta on 21/06/17.
 */

class EventsAdapter extends RecyclerView.Adapter {

    private List<Event> mEvents;

    public EventsAdapter(List<Event> events) {
        this.mEvents = events;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }
}
