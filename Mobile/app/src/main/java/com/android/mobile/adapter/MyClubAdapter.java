package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.MyClassActivity;
import com.android.mobile.models.Club;

import java.util.ArrayList;

public class MyClubAdapter extends RecyclerView.Adapter<MyClubAdapter.ViewHolder>{
    Context context;

    ArrayList<Club> clubList = new ArrayList<>();

    public MyClubAdapter(Context context, ArrayList<Club> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_club, viewGroup, false);
        return new MyClubAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtClubTitle = clubList.get(i).getTen();
        String txtClubAddress = clubList.get(i).getDiachi();
        viewHolder.txtClubTitle.setText(txtClubTitle);
        viewHolder.txtClubAddress.setText(txtClubAddress);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyClassActivity.class);
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
