package com.android.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.DetailClubActivity;
import com.android.mobile.R;
import com.android.mobile.models.Club;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    Context context;
    private List<Club> clubList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button btnJoin;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_club);
            btnJoin = view.findViewById(R.id.btn_join_club);
        }
    }

    public ClubAdapter(Context context, List<Club> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = clubList.get(position).getName();
        String ten = clubList.get(position).getTen();
        if (name != null && !name.isEmpty()) {
            holder.textView.setText(name);
        } else if (ten != null && !ten.isEmpty()) {
            holder.textView.setText(ten);
        } else {
            holder.textView.setText("Không có");
        }
        holder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailClubActivity.class);
                Bundle bundle = new Bundle();
                String idClub = clubList.get(position).getId_club();
                String id = clubList.get(position).getId();
                if (idClub != null && !idClub.isEmpty()) {
                    bundle.putString("id_club", idClub);
                } else if (id != null && !id.isEmpty()) {
                    bundle.putString("id_club", id);
                } else {
                    bundle.putString("id_club", null);
                }
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public void setData(List<Club> newData) {
        clubList.clear();
        clubList.addAll(newData);
        notifyDataSetChanged();
    }
}