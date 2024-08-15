package fascon.vovinam.vn.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.addressModel;
import com.google.gson.Gson;

import java.util.List;

public class addressAdapter extends RecyclerView.Adapter<addressAdapter.ViewHolder> {
    Activity context;
    private List<addressModel> clubList;

    public interface ItemClickListener {
        void onItemClick(int position);
    }
    ConstraintLayout position;
    private TextView address;
    private RadioButton get;
    private Button delete;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View view) {
            super(view);

        }
    }

    public addressAdapter(Activity context, List<addressModel> clubList) {
        this.context = context;
        this.clubList = clubList;
    }

    @Override
    public addressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        address = view.findViewById(R.id.address);
        get = view.findViewById(R.id.get);
        delete = view.findViewById(R.id.delete);
        position = view.findViewById(R.id.position);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int it) {
                String name = clubList.get(it).getAddress();
                int slection = clubList.get(it).getSelection();

                address.setText(name);
                if(slection == 1){
                    get.setChecked(true);
                }else {
                    get.setChecked(false);
                }
                int i = it;
                position.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(clubList.get(i).getSelection() == 0 ){
                            for (addressModel value : clubList){
                                value.setSelection(0);
                            }
                          //  Toast.makeText(context.getApplicationContext(),clubList.get(i).getAddress(),Toast.LENGTH_SHORT).show();
                            clubList.get(i).setSelection(1);
                            notifyDataSetChanged();
                            SharedPreferences sharedPreferences = context.getSharedPreferences("myAddress", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            String json = gson.toJson(clubList);
                            editor.putString("list", json);
                            editor.apply();
                            context.finish();

                        }
                    }
                });
/*        get.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(clubList.get(i).getSelection() == 0 ){
                    for (addressModel value : clubList){
                        value.setSelection(0);
                    }
                    //  Toast.makeText(context.getApplicationContext(),clubList.get(i).getAddress(),Toast.LENGTH_SHORT).show();
                    clubList.get(i).setSelection(1);

                    SharedPreferences sharedPreferences = context.getSharedPreferences("myAddress", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    String json = gson.toJson(clubList);
                    editor.putString("list", json);
                    editor.apply();
                    context.finish();

                }
            }
        });*/

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SharedPreferences sharedPreferences = context.getSharedPreferences("myAddress", Context.MODE_PRIVATE);

                        clubList.remove(i);
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        String json = gson.toJson(clubList);
                        editor.putString("list", json);
                        editor.apply();
                        notifyDataSetChanged();
                    }
                });



    }


    @Override
    public int getItemCount() {
        return clubList.size();
    }

    public void setData(List<addressModel> newData) {
        clubList.clear();
        clubList.addAll(newData);
        notifyDataSetChanged();
    }
}