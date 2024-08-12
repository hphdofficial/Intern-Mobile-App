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

import com.android.mobile.R;
import com.android.mobile.activity_item_detail;
import com.android.mobile.activity_lessons;
import com.android.mobile.models.Belt;
import com.android.mobile.models.BeltModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Theory_Belt_Adapter extends RecyclerView.Adapter<Theory_Belt_Adapter.ViewHolder>{
    Context context;
    List<Belt> beltList = new ArrayList<>();

    public Theory_Belt_Adapter(List<Belt> beltList, Context context) {
        this.beltList = beltList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter, viewGroup, false);
        return new Theory_Belt_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtInfo = beltList.get(i).getName();
        viewHolder.txtInfo.setText(txtInfo);

        String imgBelt = beltList.get(i).getLinkImage();
        if(imgBelt.isEmpty() || imgBelt.equals(" ")){
            viewHolder.imgBelt.setImageResource(R.drawable.logo_vovinam);
        }else{
            Picasso.get().load(imgBelt).placeholder(R.drawable.logo_vovinam).into(viewHolder.imgBelt);
        }

        int idBelt = beltList.get(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_lessons.class);
                intent.putExtra("idBelt", idBelt);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return beltList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;
        ImageView imgBelt;

        ViewHolder(View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            imgBelt = itemView.findViewById(R.id.imgBelt);
        }
    }
}
