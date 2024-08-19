package fascon.vovinam.vn.ViewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.Model.services.OnItemClickListener;
import fascon.vovinam.vn.View.DetailClassActivity;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.Class;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    Context context;
    private List<Class> classList;
    private String idClub = null;
    private boolean isChangeClass;
    private OnItemClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtTime;
        public TextView txtFee;
        public Button btnRegister;
        public Button btnChangeClass;

        public ViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_name_class);
            txtTime = view.findViewById(R.id.txt_time);
            txtFee = view.findViewById(R.id.txt_fee);
            btnRegister = view.findViewById(R.id.btn_register_class);
            btnChangeClass = view.findViewById(R.id.btn_change_class);
        }
    }

    public ClassAdapter(Context context, List<Class> data, String idClub) {
        this.context = context;
        this.classList = data;
        this.idClub = idClub;
    }

    public ClassAdapter(Context context, List<Class> data, boolean isChangeClass, OnItemClickListener listener) {
        this.context = context;
        this.classList = data;
        this.isChangeClass = isChangeClass;
        this.listener = listener;
    }

    private String languageS;

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(classList.get(position).getTen());
        SharedPreferences shared = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        languageS = shared.getString("language",null);

        holder.txtTime.setText("Thời gian: " + classList.get(position).getThoigian());
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedFee = currencyFormat.format(classList.get(position).getGiatien());
        holder.txtFee.setText("Học phí: " + formattedFee);
        if (isChangeClass) {
            holder.txtFee.setText("Học phí: 500.000 đ");
        }
        if(languageS!= null){
            if(languageS.contains("en")){
                holder.txtTime.setText("Time: " + classList.get(position).getThoigian());
                holder.txtFee.setText("Fee: " + formattedFee);
                if (isChangeClass) {
                    holder.txtFee.setText("Fee: 500.000 VND");
                }
                holder.btnRegister.setText("Detail");
            }
        }
        if (!isChangeClass) {
            holder.btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailClassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id_class", String.valueOf(classList.get(position).getId()));
                    bundle.putString("id_club", idClub);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        } else {
            holder.btnRegister.setVisibility(View.GONE);
            holder.btnChangeClass.setVisibility(View.GONE);
            holder.btnChangeClass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        if (selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousSelectedPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();

                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onItemClick(position, classList.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public void setData(List<Class> newData) {
        classList.clear();
        classList.addAll(newData);
        notifyDataSetChanged();
    }
}