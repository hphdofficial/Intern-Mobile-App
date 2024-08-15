package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.AttendanceModel;

import java.util.ArrayList;
import java.util.List;

public class Checked_adapter extends RecyclerView.Adapter<Checked_adapter.ViewHolder>{

    Context context;

    List<AttendanceModel.Attendance> checkinList = new ArrayList<>();

    public Checked_adapter(List<AttendanceModel.Attendance> checkinList, Context context) {
        this.checkinList = checkinList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checked, viewGroup, false);
        return new Checked_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtDate = checkinList.get(i).getDate();
        String txtDateOfWeek = checkinList.get(i).getDay_of_week();
        viewHolder.txtDate.setText(txtDate);
        String txtTimeIn = checkinList.get(i).getIn();
        viewHolder.txtTimeIn.setText(txtTimeIn);
        String txtHienDien = checkinList.get(i).getHienDien();
        if (txtTimeIn.equals("00:00")){
            viewHolder.txtChecked.setText(txtHienDien);
        }else{
            viewHolder.txtChecked.setText("Có mặt");
        }



    }

    @Override
    public int getItemCount() {
        return checkinList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtChecked;
        TextView txtTimeIn;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtChecked = itemView.findViewById(R.id.txtChecked);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
        }
    }
}
