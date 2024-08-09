package com.android.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.DetailClassActivity;
import com.android.mobile.R;
import com.android.mobile.models.Class;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    Context context;
    private List<Class> classList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button btnRegister;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_class);
            btnRegister = view.findViewById(R.id.btn_register_class);
        }
    }

    public ClassAdapter(Context context, List<Class> data) {
        this.context = context;
        this.classList = data;
    }

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(classList.get(position).getTen());
        holder.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailClassActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id_class", String.valueOf(classList.get(position).getId()));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setData(List<Class> newData) {
        classList.clear();
        classList.addAll(newData);
        notifyDataSetChanged();
    }
}