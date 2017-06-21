package com.konel.kryptapps.onboard.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konel.kryptapps.onboard.R;

import java.util.ArrayList;

/**
 * Created by himanshusaluja on 21/06/17.
 */

public class AttendeeAdapter extends RecyclerView.Adapter {

    ArrayList<String> name;

    public AttendeeAdapter(ArrayList<String> name) {
        this.name = name;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        (viewHolder).textView.setText(name.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

    @Override
    public int getItemCount() {
        if (name==null){
            return 0;
        }
        return name.size();
    }
}
