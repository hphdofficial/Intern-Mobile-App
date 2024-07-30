package com.android.mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.SupplierModel;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    private List<SupplierModel> suppliers;

    public SupplierAdapter(List<SupplierModel> suppliers) {
        this.suppliers = suppliers;
    }

    @NonNull
    @Override
    public SupplierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supplier, parent, false);
        return new SupplierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierViewHolder holder, int position) {
        SupplierModel supplier = suppliers.get(position);
        holder.supplierName.setText(supplier.getSupplierName());
        holder.address.setText(supplier.getAddress());
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    public static class SupplierViewHolder extends RecyclerView.ViewHolder {
        TextView supplierName, address;

        public SupplierViewHolder(@NonNull View itemView) {
            super(itemView);
            supplierName = itemView.findViewById(R.id.supplierName);
            address = itemView.findViewById(R.id.address);
        }
    }
}
