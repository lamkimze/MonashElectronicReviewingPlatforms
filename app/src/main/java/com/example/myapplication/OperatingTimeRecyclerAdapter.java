package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OperatingTimeRecyclerAdapter extends RecyclerView.Adapter<OperatingTimeRecyclerAdapter.CustomViewHolder> {

    ArrayList<operatingTime> data = new ArrayList<operatingTime>();

    public void setData(ArrayList<operatingTime> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_card_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.tvOperatingDay.setText(String.valueOf(data.get(position).getDay()));
        holder.tvOperatingStartHours.setText(data.get(position).getStartTime());
        holder.tvOperatingEndHours.setText(data.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOperatingDay;
        public TextView tvOperatingStartHours;
        public TextView tvOperatingEndHours;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOperatingDay = itemView.findViewById(R.id.tv_day);
            tvOperatingStartHours = itemView.findViewById(R.id.tv_start);
            tvOperatingEndHours = itemView.findViewById(R.id.tv_end);
        }


    }
}
