package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.OptionSupplier;

import java.util.List;

public class OptionCheckBoxAdapter2 extends RecyclerView.Adapter<OptionCheckBoxAdapter2.ViewHolder>{

    Context context;
    private String languageS;
    private List<OptionSupplier> optionList;

    public OptionCheckBoxAdapter2(Context context, List<OptionSupplier> optionList) {
        this.optionList = optionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_option_checkbox, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SharedPreferences shared = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        languageS = shared.getString("language",null);
        OptionSupplier option = optionList.get(i);
        viewHolder.checkBox.setText(option.getSupplierName());
        viewHolder.checkBox.setChecked(option.isChecked());

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            option.setChecked(isChecked);
        });

        if(languageS != null){
            if(languageS.contains("en")){
                viewHolder.checkBox.setText(option.getSupplierNameEn());
            }
        }

    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public List<OptionSupplier> getOptionList() {
        return optionList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
