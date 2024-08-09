package com.android.mobile.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.android.mobile.Belt_Payment;
import com.android.mobile.MenuActivity;
import com.android.mobile.R;
import com.android.mobile.activity_item_chapter;
import com.android.mobile.models.Belt;
import com.android.mobile.models.BeltModel;
import com.android.mobile.models.HistoryClassModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HistoryClassAdapter extends RecyclerView.Adapter<HistoryClassAdapter.ViewHolder>{
    Context context;

    BeltModel b ;
    List<HistoryClassModel> chapterList = new ArrayList<>();

    public HistoryClassAdapter(Context context, List<HistoryClassModel> list) {
        this.context = context;
        this.chapterList = list;
    }

    public void loadList(List<HistoryClassModel> list){
        this.chapterList = list;
    }
    public void loadBelt(BeltModel b){
        this.b = b;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_class, viewGroup, false);
        id = view.findViewById(R.id.id);
        date = view.findViewById(R.id.date);
        nameClass = view.findViewById(R.id.nameclass);
        item_belt = view.findViewById(R.id.item_belt);
        current = view.findViewById(R.id.current);
        price = view.findViewById(R.id.price);

        return new ViewHolder(view);
    }
    private TextView id;
    private  TextView date;
    private TextView nameClass;
    private TextView current;
    private TextView price;

    private ConstraintLayout item_belt;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        id.setText("Chi tiết hóa đơn số " +chapterList.get(i).getIdBill());
        nameClass.setText("Tên lớp: " + chapterList.get(i).getNameClass());
        date.setText("Ngày tạo: " + chapterList.get(i).getDate());
        if(chapterList.get(i).getStatus().contains("chua"))
        current.setText("Trạng thái: chưa thanh toán");else {
            current.setText("Trạng thái: hoàn thành");
        }
        price.setText("Tổng thanh toán: "+chapterList.get(i).getPrice().toString());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
