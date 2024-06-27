package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.Member;

import java.util.ArrayList;

public class Checkin_adapter extends RecyclerView.Adapter<Checkin_adapter.ViewHolder>{
    Context context;

    ArrayList<Member> memberList = new ArrayList<>();

    public Checkin_adapter(Context context, ArrayList<Member> memberList) {
        this.context = context;
        this.memberList = memberList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkin, viewGroup, false);
        return new Checkin_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtMemberName = memberList.get(i).getTen();
        viewHolder.txtMemberName.setText(txtMemberName);
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMemberName;

        ViewHolder(View itemView) {
            super(itemView);
            txtMemberName = itemView.findViewById(R.id.txtMemberName);
        }
    }
}
