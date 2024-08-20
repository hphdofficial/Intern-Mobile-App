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
import fascon.vovinam.vn.Model.AttendanceTeacher;

import java.util.ArrayList;
import java.util.List;

public class Checked_Teacher_adapter extends RecyclerView.Adapter<Checked_Teacher_adapter.ViewHolder>{

    Context context;
    private String languageS;
    List<AttendanceTeacher> checkinList = new ArrayList<>();

    public Checked_Teacher_adapter(Context context, List<AttendanceTeacher> checkinList) {
        this.checkinList = checkinList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teacher_checkin, viewGroup, false);
        return new Checked_Teacher_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SharedPreferences shared = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        languageS = shared.getString("language",null);
        String txtDateOfWeek = checkinList.get(i).getDay_of_week();
        String txtDate = checkinList.get(i).getDate();

        if(languageS != null){
            if(languageS.contains("en")){
                if(txtDateOfWeek.contains("2")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Mon");
                }
                if(txtDateOfWeek.contains("3")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Tue");
                }
                if(txtDateOfWeek.contains("4")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Wed");
                }
                if(txtDateOfWeek.contains("5")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Thu");
                }
                if(txtDateOfWeek.contains("6")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Fri");
                }
                if(txtDateOfWeek.contains("7")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Sat");
                }
                if(txtDateOfWeek.contains("CN")){
                    viewHolder.txtDate.setText(txtDate + " "+ "Sun");
                }
            }else{
                viewHolder.txtDate.setText(txtDate + " "+ txtDateOfWeek);
            }
        }else{
            viewHolder.txtDate.setText(txtDate + " "+ txtDateOfWeek);
        }

        String txtName = checkinList.get(i).getMember_name();
        viewHolder.txtName.setText(txtName);

        String txtTimeIn = checkinList.get(i).getIn();
        viewHolder.txtTimeIn.setText(txtTimeIn);

//        viewHolder.txtChecked.setText("Có mặt");
    }

    @Override
    public int getItemCount() {
        return checkinList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtName;
        TextView txtChecked;
        TextView txtTimeIn;

        ViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtName = itemView.findViewById(R.id.txtName);
//            txtChecked = itemView.findViewById(R.id.txtChecked);
            txtTimeIn = itemView.findViewById(R.id.txtTimeIn);
        }
    }

}
