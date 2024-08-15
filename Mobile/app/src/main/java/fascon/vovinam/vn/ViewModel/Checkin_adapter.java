package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.CheckinMemberModel;

import java.util.ArrayList;
import java.util.List;

public class Checkin_adapter extends RecyclerView.Adapter<Checkin_adapter.ViewHolder>{
    Context context;

    List<CheckinMemberModel> memberList = new ArrayList<>();

    public Checkin_adapter(Context context, List<CheckinMemberModel> memberList) {
        this.context = context;
        this.memberList = memberList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkin, viewGroup, false);
        return new Checkin_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CheckinMemberModel checkinMemberModel = memberList.get(i);
        String txtMemberName = memberList.get(i).getTen();
        viewHolder.txtMemberName.setText(txtMemberName);
        int txtMemberID = memberList.get(i).getId();
        viewHolder.txtMemberID.setText(txtMemberID+"");

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkinMemberModel.setChecked(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public List<CheckinMemberModel> getCheckinList() {
        return memberList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMemberName;
        TextView txtMemberID;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            txtMemberName = itemView.findViewById(R.id.txtMemberName);
            txtMemberID = itemView.findViewById(R.id.txtMemberID);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
