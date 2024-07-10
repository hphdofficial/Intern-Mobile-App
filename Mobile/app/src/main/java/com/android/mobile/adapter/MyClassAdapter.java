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
import com.android.mobile.activity_checkin;
import com.android.mobile.models.Class;

import java.util.ArrayList;

public class MyClassAdapter extends RecyclerView.Adapter<MyClassAdapter.ViewHolder>{
    Context context;

    ArrayList<Class> classList = new ArrayList<>();

    public MyClassAdapter(Context context, ArrayList<Class> classList) {
        this.context = context;
        this.classList = classList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_class, viewGroup, false);
        return new MyClassAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtClassTitle = classList.get(i).getTen();
        String txtTeacherName = classList.get(i).getTeacherName();
        viewHolder.txtClassTitle.setText(txtClassTitle);
        viewHolder.txtTeacherName.setText(txtTeacherName);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_checkin.class);
                intent.putExtra("title", txtClassTitle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassTitle, txtTeacherName;

        ViewHolder(View itemView) {
            super(itemView);
            txtClassTitle = itemView.findViewById(R.id.txtClassTitle);
            txtTeacherName = itemView.findViewById(R.id.txtTeacherName);
        }
    }
}
