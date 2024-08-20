
package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.SupplierModel;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierViewHolder> {

    private List<SupplierModel> suppliers;
    private OnSupplierClickListener listener;

    public SupplierAdapter(List<SupplierModel> suppliers, OnSupplierClickListener listener) {
        this.suppliers = suppliers;
        this.listener = listener;
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

        // Lấy ngôn ngữ hiện tại từ SharedPreferences
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String languageS = sharedPreferences.getString("language", "vn");

        // Kiểm tra ngôn ngữ và hiển thị tên nhà cung cấp cùng địa chỉ tương ứng
        if (languageS != null && languageS.contains("en")) {
            holder.supplierName.setText(supplier.getTenen());  // Tên nhà cung cấp bằng tiếng Anh
            holder.address.setText(supplier.getDiachien());  // Địa chỉ bằng tiếng Anh
        } else {
            holder.supplierName.setText(supplier.getSupplierName());  // Tên nhà cung cấp mặc định
            holder.address.setText(supplier.getAddress());  // Địa chỉ mặc định
        }

        holder.itemView.setOnClickListener(v -> listener.onSupplierClick(supplier));
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

    public interface OnSupplierClickListener {
        void onSupplierClick(SupplierModel supplier);
    }
}
