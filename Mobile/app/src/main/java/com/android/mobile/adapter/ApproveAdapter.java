package com.android.mobile.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.ApproveActivity;
import com.android.mobile.R;
import com.android.mobile.models.ApproveModel;
import com.android.mobile.models.OrderListModel;
import com.android.mobile.models.ReponseModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClassApiService;
import com.android.mobile.services.ClubApiService;
import com.android.mobile.services.OrderApiService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private List<ApproveModel> approveListClubClass = new ArrayList<>();
    private List<OrderListModel> approveListOrder = new ArrayList<>();
    private String viewType;

    public static class ClubClassViewHolder extends RecyclerView.ViewHolder {
        public TextView txtApprove;
        public TextView txtClubClass;
        public TextView txtMember;
        public TextView txtTime;
        public Button btnApproveRequest;

        public ClubClassViewHolder(View view) {
            super(view);
            txtApprove = view.findViewById(R.id.txt_name_approve);
            txtClubClass = view.findViewById(R.id.txt_name_clubclass);
            txtMember = view.findViewById(R.id.txt_name_member);
            txtTime = view.findViewById(R.id.txt_time_request);
            btnApproveRequest = view.findViewById(R.id.button_approve_request);
        }
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIdOrder;
        public TextView txtStatusOrder;
        public RecyclerView recyclerProductList;
        public Button btnApproveOrder;
        public TextView totalPrice;

        public OrderViewHolder(View view) {
            super(view);
            txtIdOrder = view.findViewById(R.id.txt_id_order);
            txtStatusOrder = view.findViewById(R.id.txt_status_order);
            recyclerProductList = view.findViewById(R.id.recycler_product_list);
            btnApproveOrder = view.findViewById(R.id.button_approve_order);
            totalPrice = view.findViewById(R.id.total_price);
        }
    }

    public ApproveAdapter(Context context, List<?> list, String viewType) {
        this.context = context;
        this.viewType = viewType;

        if (list != null && !list.isEmpty()) {
            if (list.get(0) instanceof String) {
                this.approveListClubClass = (List<ApproveModel>) list;
            } else if (list.get(0) instanceof OrderListModel) {
                this.approveListOrder = (List<OrderListModel>) list;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if ("joinclub".equals(this.viewType) || "joinclass".equals(this.viewType) || "leaveclub".equals(this.viewType) || "leaveclass".equals(this.viewType)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approve_clubclass, parent, false);
            return new ClubClassViewHolder(view);
        } else if ("confirmorder".equals(this.viewType) || "getorder".equals(this.viewType) || "shiporder".equals(this.viewType)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approve_order, parent, false);
            return new OrderViewHolder(view);
        }
        throw new IllegalArgumentException("Invalid viewType: " + this.viewType);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ClubClassViewHolder) {
            ApproveModel itemClubClass = approveListClubClass.get(position);
            ClubClassViewHolder clubClassViewHolder = (ClubClassViewHolder) holder;
            switch (viewType) {
                case "joinclub":
                    clubClassViewHolder.txtApprove.setTextColor(Color.BLUE);
                    clubClassViewHolder.txtApprove.setText("Yêu cầu tham gia");
                    clubClassViewHolder.txtClubClass.setText("Câu lạc bộ: " + itemClubClass.getTen_club());
                    clubClassViewHolder.txtMember.setText("Người yêu cầu: " + itemClubClass.getTen());
                    clubClassViewHolder.txtTime.setText("Thời gian yêu cầu: " + itemClubClass.getCreated_at());
                    clubClassViewHolder.btnApproveRequest.setBackgroundColor(Color.BLUE);
                    break;
                case "joinclass":
                    clubClassViewHolder.txtApprove.setTextColor(Color.BLUE);
                    clubClassViewHolder.txtApprove.setText("Yêu cầu tham gia");
                    clubClassViewHolder.txtClubClass.setText("Lớp học: " + itemClubClass.getClass_name());
                    clubClassViewHolder.txtMember.setText("Người yêu cầu: " + itemClubClass.getTen());
                    clubClassViewHolder.txtTime.setText("Thời gian yêu cầu: " + itemClubClass.getCreated_at());
                    clubClassViewHolder.btnApproveRequest.setBackgroundColor(Color.BLUE);
                    break;
                case "leaveclub":
                    clubClassViewHolder.txtApprove.setTextColor(Color.RED);
                    clubClassViewHolder.txtApprove.setText("Yêu cầu rời");
                    clubClassViewHolder.txtClubClass.setText("Câu lạc bộ: " + itemClubClass.getTen_club());
                    clubClassViewHolder.txtMember.setText("Người yêu cầu: " + itemClubClass.getTen());
                    clubClassViewHolder.txtTime.setText("Thời gian yêu cầu: " + itemClubClass.getCreated_at());
                    clubClassViewHolder.btnApproveRequest.setBackgroundColor(Color.RED);
                    break;
                case "leaveclass":
                    clubClassViewHolder.txtApprove.setTextColor(Color.RED);
                    clubClassViewHolder.txtApprove.setText("Yêu cầu rời");
                    clubClassViewHolder.txtClubClass.setText("Lớp học: " + itemClubClass.getClass_name());
                    clubClassViewHolder.txtMember.setText("Người yêu cầu: " + itemClubClass.getTen());
                    clubClassViewHolder.txtTime.setText("Thời gian yêu cầu: " + itemClubClass.getCreated_at());
                    clubClassViewHolder.btnApproveRequest.setBackgroundColor(Color.RED);
                    break;
                default:
                    break;
            }

            clubClassViewHolder.btnApproveRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clubClassViewHolder.btnApproveRequest.setEnabled(false);
                    switch (viewType) {
                        case "joinclub":
                            approveJoinClub(itemClubClass.getId_member(), itemClubClass.getId_club(), clubClassViewHolder.btnApproveRequest);
                            break;
                        case "joinclass":
                            approveJoinClass(itemClubClass.getId_member(), itemClubClass.getId_class(), clubClassViewHolder.btnApproveRequest);
                            break;
                        case "leaveclub":
                            approveLeaveClub(itemClubClass.getId_member(), itemClubClass.getId_club(), clubClassViewHolder.btnApproveRequest);
                            break;
                        case "leaveclass":
                            approveLeaveClass(itemClubClass.getId_member(), itemClubClass.getId_class(), clubClassViewHolder.btnApproveRequest);
                            break;
                        default:
                            break;
                    }
                }
            });
        } else if (holder instanceof OrderViewHolder) {
            OrderListModel itemOrder = approveListOrder.get(position);
            OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
            orderViewHolder.txtIdOrder.setText("Đơn hàng " + itemOrder.getTxn_ref());
            orderViewHolder.txtStatusOrder.setText(itemOrder.getGiao_hang().substring(0, 1).toUpperCase() + itemOrder.getGiao_hang().substring(1).toLowerCase());
            double totalPrice = 0;
            for (OrderListModel.DetailCart cart : itemOrder.getDetail_carts()) {
                totalPrice += cart.getProduct().getUnitPrice() * cart.getQuantity();
            }
            orderViewHolder.totalPrice.setText(String.format("Tổng tiền: %,.0f VND", totalPrice));
            OrderListModel finalItemOrder = itemOrder;
            orderViewHolder.btnApproveOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderViewHolder.btnApproveOrder.setEnabled(false);
                    switch (viewType) {
                        case "confirmorder":
                            approveConfirmOrder(finalItemOrder.getTxn_ref(), orderViewHolder.btnApproveOrder);
                            break;
                        case "getorder":
                            approveGetOrder(finalItemOrder.getTxn_ref(), orderViewHolder.btnApproveOrder);
                            break;
                        case "shiporder":
                            approveShipOrder(finalItemOrder.getTxn_ref(), orderViewHolder.btnApproveOrder);
                            break;
                        default:
                            break;
                    }
                }
            });

            Map<String, List<OrderListModel.DetailCart>> supplierProductMap = new LinkedHashMap<>();
            for (OrderListModel.DetailCart detailCart : itemOrder.getDetail_carts()) {
                String supplierName = detailCart.getProduct().getSupplierName();
                if (!supplierProductMap.containsKey(supplierName)) {
                    supplierProductMap.put(supplierName, new ArrayList<>());
                }
                supplierProductMap.get(supplierName).add(detailCart);
            }

            List<Object> items = new ArrayList<>();
            for (Map.Entry<String, List<OrderListModel.DetailCart>> entry : supplierProductMap.entrySet()) {
                items.add(entry.getKey());
                items.addAll(entry.getValue());
            }

            for (int i = 0; i < items.size(); i++) {
                Object itemss = items.get(i);

                if (itemss instanceof OrderListModel.DetailCart) {
                    OrderListModel.DetailCart detailCart = (OrderListModel.DetailCart) itemss;
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
            orderViewHolder.recyclerProductList.setLayoutManager(new LinearLayoutManager(context));
            orderViewHolder.recyclerProductList.setAdapter(new ProductOrderAdapter(context, items));
        }
    }

    private void approveJoinClub(int idMember, int idClub, Button button) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.approveJoinClub("Bearer " + accessToken, idMember, idClub);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Duyệt tham gia câu lạc bộ thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(0);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveJoinClass(int idMember, int idClass, Button button) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.approveJoinClass("Bearer " + accessToken, idMember, idClass);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Duyệt tham gia lớp học thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(1);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveLeaveClub(int idMember, int idClub, Button button) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<ReponseModel> call = service.approveLeaveClub("Bearer " + accessToken, idMember, idClub);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Duyệt rời câu lạc bộ thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(2);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveLeaveClass(int idMember, int idClass, Button button) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.approveLeaveClass("Bearer " + accessToken, idMember, idClass);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Duyệt rời lớp học thành công", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(3);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveConfirmOrder(String idOrder, Button button) {
        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<ReponseModel> call = service.updateConfirmOrder(idOrder);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Cập nhật thành công trạng thái chờ lấy hàng", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(4);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveGetOrder(String idOrder, Button button) {
        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<ReponseModel> call = service.updateGetOrder(idOrder);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Cập nhật thành công trạng thái đang giao hàng", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(5);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void approveShipOrder(String idOrder, Button button) {
        OrderApiService service = ApiServiceProvider.getOrderApiService();
        Call<ReponseModel> call = service.updateShipOrder(idOrder);

        Toast.makeText(context, "Đang xử lý...", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                button.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Cập nhật thành công trạng thái đã giao hàng", Toast.LENGTH_SHORT).show();
                    if (context instanceof ApproveActivity) {
                        ((ApproveActivity) context).selectSpinnerItem(6);
                    }
                    Log.i("Success", response.message());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                button.setEnabled(true);
                Log.e("Fail", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (viewType.equals("joinclub") || viewType.equals("joinclass") || viewType.equals("leaveclub") || viewType.equals("leaveclass")) {
            return approveListClubClass.size();
        } else if (viewType.equals("confirmorder") || viewType.equals("getorder") || viewType.equals("shiporder")) {
            return approveListOrder.size();
        } else {
            return approveListClubClass.size();
        }
    }

    public void setDataClubClass(List<ApproveModel> data) {
        approveListClubClass.clear();
        approveListClubClass.addAll(data);
        notifyDataSetChanged();
    }

    public void setDataOrder(List<OrderListModel> data) {
        approveListOrder.clear();
        approveListOrder.addAll(data);
        notifyDataSetChanged();
    }
}