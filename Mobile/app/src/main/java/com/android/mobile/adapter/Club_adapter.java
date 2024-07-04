package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.ClubActivity;
import com.android.mobile.R;
import com.android.mobile.activity_chapters;
import com.android.mobile.activity_checkin;
import com.android.mobile.activity_classes;
import com.android.mobile.models.Chapter;
import com.android.mobile.models.Club;

import java.util.ArrayList;

public class Club_adapter extends RecyclerView.Adapter<Club_adapter.ViewHolder>{
    Context context;

    ArrayList<Club> clubList = new ArrayList<>();

    public Club_adapter(Context context, ArrayList<Club> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_club, viewGroup, false);
        return new Club_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtClubTitle = clubList.get(i).getTen();
        String txtClubAddress = clubList.get(i).getDiaChi();
        viewHolder.txtClubTitle.setText(txtClubTitle);
        viewHolder.txtClubAddress.setText(txtClubAddress);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_classes.class);
                intent.putExtra("title", txtClubTitle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtClubTitle, txtClubAddress;

        ViewHolder(View itemView) {
            super(itemView);
            txtClubTitle = itemView.findViewById(R.id.txtClubTitle);
            txtClubAddress = itemView.findViewById(R.id.txtClubAddress);
        }
    }
}
