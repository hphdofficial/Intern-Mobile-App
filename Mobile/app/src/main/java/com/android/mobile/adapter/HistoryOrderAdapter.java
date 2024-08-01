package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.OrderModel;

import java.util.List;

public class HistoryOrderAdapter extends RecyclerView.Adapter<HistoryOrderAdapter.ViewHolder> {
    Context context;
    private List<OrderModel> orderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_purchase_history);
        }
    }

    public HistoryOrderAdapter(Context context, List<OrderModel> data) {
        this.context = context;
        this.orderList = data;
    }

    @Override
    public HistoryOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryOrderAdapter.ViewHolder holder, int position) {
        holder.textView.setText(orderList.get(position).getOrderInfo());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setData(List<OrderModel> newData) {
        orderList.clear();
        orderList.addAll(newData);
        notifyDataSetChanged();
    }
}