package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;

import java.util.List;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {
    Context context;
    private List<String> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_purchase_history);
        }
    }

    public PurchaseHistoryAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public PurchaseHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PurchaseHistoryAdapter.ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}