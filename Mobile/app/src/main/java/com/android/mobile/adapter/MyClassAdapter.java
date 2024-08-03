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
import com.android.mobile.models.ClassModel;

import java.util.List;

public class MyClassAdapter extends RecyclerView.Adapter<MyClassAdapter.ViewHolder>{
    Context context;

    List<ClassModel> classList;

    public MyClassAdapter(Context context, List<ClassModel> classList) {
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
        String txtClassTime = classList.get(i).getThoigian();
        int idClass = classList.get(i).getId();
        viewHolder.txtClassTitle.setText(txtClassTitle);
        viewHolder.txtClassTime.setText(txtClassTime);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_checkin.class);
                intent.putExtra("id", idClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtClassTitle, txtClassTime;

        ViewHolder(View itemView) {
            super(itemView);
            txtClassTitle = itemView.findViewById(R.id.txtClassTitle);
            txtClassTime = itemView.findViewById(R.id.txtClassTime);
        }
    }
}
