package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.Checkin;
import com.android.mobile.models.Member;

import java.util.ArrayList;
import java.util.Date;

public class Checked_adapter extends RecyclerView.Adapter<Checked_adapter.ViewHolder>{

    Context context;

    ArrayList<Checkin> checkinList = new ArrayList<>();

    public Checked_adapter(ArrayList<Checkin> checkinList, Context context) {
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
        Date txtDate = checkinList.get(i).getNgayDiemDanh();
        viewHolder.txtDate.setText(txtDate.toString());
        Boolean txtChecked = checkinList.get(i).isHienDien();
        if(txtChecked){
            viewHolder.txtChecked.setText("Có mặt");
        }else{
            viewHolder.txtChecked.setText("Vắng");
        }

    }

    @Override
    public int getItemCount() {
        return checkinList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtChecked;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtChecked = itemView.findViewById(R.id.txtChecked);
        }
    }
}
