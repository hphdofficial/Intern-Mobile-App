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
import com.android.mobile.activity_item_chapter;
import com.android.mobile.models.Chapter;

import java.util.ArrayList;

public class Chapter_adapter extends RecyclerView.Adapter<Chapter_adapter.ViewHolder>{
    Context context;

    ArrayList<Chapter> chapterList = new ArrayList<>();

    public Chapter_adapter(Context context, ArrayList<Chapter> chapterList) {
        this.context = context;
        this.chapterList = chapterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtTitle = chapterList.get(i).getTitle();
        int viewCount = chapterList.get(i).getView();
        String txtInfo = txtTitle + "(Lượt xem: " + viewCount + ")";

        viewHolder.txtInfo.setText(txtInfo);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_chapter.class);
                intent.putExtra("title", txtTitle);
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
