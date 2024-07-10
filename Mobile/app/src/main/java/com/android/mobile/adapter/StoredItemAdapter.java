package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;

import java.util.List;

public class StoredItemAdapter extends RecyclerView.Adapter<StoredItemAdapter.ViewHolder> {
    Context context;
    private List<String> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_stored_item);
        }
    }

    public StoredItemAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public StoredItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stored_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoredItemAdapter.ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}