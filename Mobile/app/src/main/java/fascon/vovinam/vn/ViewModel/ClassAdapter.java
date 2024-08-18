package fascon.vovinam.vn.ViewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtTime;
        public TextView txtFee;
        public Button btnRegister;

        public ViewHolder(View view) {
            super(view);
            txtName = view.findViewById(R.id.txt_name_class);
            txtTime = view.findViewById(R.id.txt_time);
            txtFee = view.findViewById(R.id.txt_fee);
            btnRegister = view.findViewById(R.id.btn_register_class);

        }
    }

    public ClassAdapter(Context context, List<Class> data, String idClub) {
        this.context = context;
        this.classList = data;
        this.idClub = idClub;
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
        if(languageS!= null){
            if(languageS.contains("en")){
                holder.txtTime.setText("Time: " + classList.get(position).getThoigian());
                holder.txtFee.setText("Fee: " + formattedFee);
                holder.btnRegister.setText("Detail");
            }
        }
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