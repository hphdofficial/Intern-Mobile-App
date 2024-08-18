package fascon.vovinam.vn.ViewModel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.View.EditOrderFragment;
import fascon.vovinam.vn.View.OrderActivity;
import fascon.vovinam.vn.View.OrderDetailFragment;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.OrderListModel;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.OrderApiService;
import fascon.vovinam.vn.Model.services.UserApiService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;
    private UserApiService apiService;
    private List<OrderListModel> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIdOrder;
        public TextView txtStatusOrder;
        public TextView totalPrice;
        public RecyclerView recyclerProductList;
        public Button buttonDetail;
        public Button buttonCancel;
        public Button buttonEdit;

        public ViewHolder(View view) {
            super(view);
            txtIdOrder = view.findViewById(R.id.txt_id_order);
            txtStatusOrder = view.findViewById(R.id.txt_status_order);
            totalPrice = view.findViewById(R.id.total_price);
            recyclerProductList = view.findViewById(R.id.recycler_product_list);
            buttonDetail = view.findViewById(R.id.button_detail);
            buttonCancel = view.findViewById(R.id.button_cancel);
            buttonEdit = view.findViewById(R.id.button_edit);
        }
    }

    public OrderAdapter(Context context, List<OrderListModel> data) {
        this.context = context;
        this.data = data;
        apiService = ApiServiceProvider.getUserApiService();
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        OrderListModel order = data.get(position);

        holder.txtIdOrder.setText("Đơn hàng " + order.getTxn_ref());
        holder.txtStatusOrder.setText(order.getGiao_hang().substring(0, 1).toUpperCase() + order.getGiao_hang().substring(1).toLowerCase());

        if (order.getGiao_hang().equals("chờ xác nhận")) {
            holder.buttonEdit.setVisibility(View.VISIBLE);
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    EditOrderFragment dialogFragment = EditOrderFragment.newInstance(order);
                    dialogFragment.show(fragmentManager, "OrderDetailsDialogFragment");
                }
            });
            holder.buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] reasons = {
                            "Tôi muốn cập nhật thông tin nhận hàng",
                            "Tôi muốn thay đổi mã giảm giá",
                            "Tôi muốn thay đồi sản phẩm",
                            "Tôi tìm thấy cửa hàng khác tốt hơn",
                            "Tôi không có nhu cầu mua nữa",
                            "Tôi không tìm thấy lý do hủy phù hợp"};

                    final int[] selectedReason = {-1};

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Chọn lý do hủy đơn hàng");

                    builder.setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            selectedReason[0] = which;
                        }
                    });

                    builder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setPositiveButton("Xác nhận hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (selectedReason[0] != -1) {
                                String reason = reasons[selectedReason[0]];
                                cancelOrder(order.getTxn_ref());
                            } else {
                                Toast.makeText(v.getContext(), "Vui lòng chọn một lý do để hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.create().show();
                }
            });
        }

        // Tính tổng tiền (trước khi cập nhật danh sách sản phẩm)
        double totalPrice = 0;
        for (OrderListModel.DetailCart cart : order.getDetail_carts()) {
            double unitPrice = cart.getProduct().getUnitPrice();
            String sale = cart.getProduct().getSale();
            double percent = sale != null ? Double.parseDouble(sale) : 0;
            double priceAfterDiscount = unitPrice - (unitPrice * percent);
            totalPrice += priceAfterDiscount * cart.getQuantity();
        }
        holder.totalPrice.setText(String.format("Tổng tiền: %,.0f VND", totalPrice));

        // Group products by supplier
        Map<String, List<OrderListModel.DetailCart>> supplierProductMap = new LinkedHashMap<>();
        for (OrderListModel.DetailCart detailCart : order.getDetail_carts()) {
            String supplierName = detailCart.getProduct().getSupplierName();
            if (!supplierProductMap.containsKey(supplierName)) {
                supplierProductMap.put(supplierName, new ArrayList<>());
            }
            supplierProductMap.get(supplierName).add(detailCart);
        }

        // Create a list of items including supplier names and their products
        List<Object> items = new ArrayList<>();
        for (Map.Entry<String, List<OrderListModel.DetailCart>> entry : supplierProductMap.entrySet()) {
            items.add(entry.getKey()); // Add supplier name as a header
            items.addAll(entry.getValue()); // Add products of the supplier
        }

        // Cập nhật danh sách sản phẩm (cập nhật số lượng và loại bỏ sản phẩm trùng lặp)
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);

            if (item instanceof OrderListModel.DetailCart) {
                OrderListModel.DetailCart detailCart = (OrderListModel.DetailCart) item;
                boolean found = false;

                for (int j = i + 1; j < items.size(); j++) {
                    Object compareItem = items.get(j);

                    if (compareItem instanceof OrderListModel.DetailCart) {
                        OrderListModel.DetailCart compareDetailCart = (OrderListModel.DetailCart) compareItem;

                        if (detailCart.getProduct().getProductID() == compareDetailCart.getProduct().getProductID()) {
                            detailCart.setQuantity(detailCart.getQuantity() + compareDetailCart.getQuantity());
                            items.remove(j);
                            j--;
                            found = true;
                        }
                    }
                }
            }
        }

        // Setup product list with grouped items
        holder.recyclerProductList.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerProductList.setAdapter(new ProductOrderAdapter(context, items));

        // Set the order details button
        holder.buttonDetail.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            OrderDetailFragment dialogFragment = OrderDetailFragment.newInstance(order);
            dialogFragment.show(fragmentManager, "OrderDetailsDialogFragment");
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<OrderListModel> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    public void cancelOrder(String idOrder) {
        if (context instanceof OrderActivity) {
            ((OrderActivity) context).switchToTab(4);
        }
        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<ReponseModel> call = service.cancelOrder(idOrder, "cancel");

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
}