package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.OptionCategory;

import java.util.List;

public class OptionCheckBoxAdapter extends RecyclerView.Adapter<OptionCheckBoxAdapter.ViewHolder>{

    Context context;
    private List<OptionCategory> optionList;

    public OptionCheckBoxAdapter(Context context, List<OptionCategory> optionList) {
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
        OptionCategory option = optionList.get(i);
        viewHolder.checkBox.setText(option.getCategoryName());
        viewHolder.checkBox.setChecked(option.isChecked());

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            option.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public List<OptionCategory> getOptionList() {
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
