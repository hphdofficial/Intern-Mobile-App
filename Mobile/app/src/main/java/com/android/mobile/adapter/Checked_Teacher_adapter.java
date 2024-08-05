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
import com.android.mobile.models.AttendanceTeacher;

import java.util.ArrayList;
import java.util.List;

public class Checked_Teacher_adapter extends RecyclerView.Adapter<Checked_Teacher_adapter.ViewHolder>{

    Context context;

    List<AttendanceTeacher> checkinList = new ArrayList<>();

    public Checked_Teacher_adapter(Context context, List<AttendanceTeacher> checkinList) {
        this.checkinList = checkinList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_checkin, viewGroup, false);
        return new Checked_Teacher_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtDateOfWeek = checkinList.get(i).getDay_of_week();
        String txtDate = checkinList.get(i).getDate();
        viewHolder.txtDate.setText(txtDate + " "+ txtDateOfWeek);

        String txtName = checkinList.get(i).getMember_name();
        viewHolder.txtName.setText(txtName);

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
        TextView txtName;
        TextView txtChecked;
        TextView txtTimeIn;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtName = itemView.findViewById(R.id.txtName);
            txtChecked = itemView.findViewById(R.id.txtChecked);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
        }
    }

}
