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

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    Context context;
    private List<Class> classList;
    private String idClub = null;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView textView1;
        public Button btnRegister;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.txt_name_class);
            textView1 = view.findViewById(R.id.txt_name_teacher);
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
        holder.textView.setText(classList.get(position).getTen());
        SharedPreferences shared = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        languageS = shared.getString("language",null);

        holder.textView1.setText("Giảng viên: " + classList.get(position).getGiangvien());
        if(languageS!= null){
            if(languageS.contains("en")){
                holder.textView1.setText("Lecturer: " + classList.get(position).getGiangvien());
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