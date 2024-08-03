package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.AttendanceModel;
import com.android.mobile.models.Checkin;
import com.android.mobile.models.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Checked_adapter extends RecyclerView.Adapter<Checked_adapter.ViewHolder>{

    Context context;

    List<AttendanceModel.Attendance> checkinList = new ArrayList<>();

    public Checked_adapter(List<AttendanceModel.Attendance> checkinList, Context context) {
        this.checkinList = checkinList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checked, viewGroup, false);
        return new Checked_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtDate = checkinList.get(i).getDate();
        String txtDateOfWeek = checkinList.get(i).getDay_of_week();
        viewHolder.txtDate.setText(txtDateOfWeek +" Ngày "+txtDate);
        String txtTimeIn = checkinList.get(i).getIn();
        viewHolder.txtTimeIn.setText(txtTimeIn);
        viewHolder.txtChecked.setText("Có mặt");


    }

    @Override
    public int getItemCount() {
        return checkinList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtChecked;
        TextView txtTimeIn;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtChecked = itemView.findViewById(R.id.txtChecked);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
        }
    }
}
