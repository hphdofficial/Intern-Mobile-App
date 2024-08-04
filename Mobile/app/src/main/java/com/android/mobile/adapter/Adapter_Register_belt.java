package com.android.mobile.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android.mobile.Belt_Payment;
import com.android.mobile.R;
import com.android.mobile.activity_item_chapter;
import com.android.mobile.models.Belt;

import java.util.ArrayList;

public class Adapter_Register_belt extends RecyclerView.Adapter<Adapter_Register_belt.ViewHolder>{
    Context context;

    ArrayList<Belt> chapterList = new ArrayList<>();

    public Adapter_Register_belt(Context context, ArrayList<Belt> list) {
        this.context = context;
        this.chapterList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_belt, viewGroup, false);
         name = view.findViewById(R.id.name);
         status = view.findViewById(R.id.status);
         image = view.findViewById(R.id.imageView2);


        return new ViewHolder(view);
    }
    private TextView name;
    private  TextView status;
    private ImageView image;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtTitle = chapterList.get(i).getName();


        name.setText(txtTitle);


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Belt_Payment.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;

        ViewHolder(View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}
