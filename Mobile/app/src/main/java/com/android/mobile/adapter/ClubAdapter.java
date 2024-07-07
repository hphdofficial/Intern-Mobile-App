package com.android.mobile.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.DetailClubActivity;
import com.android.mobile.R;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    Context context;
    private List<String> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button btnJoin;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_club);
            btnJoin = view.findViewById(R.id.btn_join_club);
        }
    }

    public ClubAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubAdapter.ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        holder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailClubActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}