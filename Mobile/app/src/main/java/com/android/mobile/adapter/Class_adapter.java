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
import com.android.mobile.activity_classes;
import com.android.mobile.models.Class;

import java.util.ArrayList;

public class Class_adapter extends RecyclerView.Adapter<Class_adapter.ViewHolder>{
    Context context;

    ArrayList<Class> classList = new ArrayList<>();

    public Class_adapter(Context context, ArrayList<Class> classList) {
        this.context = context;
        this.classList = classList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, viewGroup, false);
        return new Class_adapter.ViewHolder(view);
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
