package fascon.vovinam.vn.ViewModel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import fascon.vovinam.vn.View.Belt_Payment;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.Belt;
import fascon.vovinam.vn.Model.BeltModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Register_belt extends RecyclerView.Adapter<Adapter_Register_belt.ViewHolder>{
    Context context;
    private String languageS;
    BeltModel b;
    List<Belt> chapterList = new ArrayList<>();

    public Adapter_Register_belt(Context context, List<Belt> list) {
        this.context = context;
        this.chapterList = list;
    }

    public void loadList(List<Belt> list){
        this.chapterList = list;
    }
    public void loadBelt(BeltModel b){
       this.b = b;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_belt, viewGroup, false);
        SharedPreferences shared = context.getSharedPreferences("login_prefs", context.MODE_PRIVATE);
        languageS = shared.getString("language",null);

         name = view.findViewById(R.id.name);
         status = view.findViewById(R.id.status);
         image = view.findViewById(R.id.imgBelt);
        item_belt = view.findViewById(R.id.item_belt);
        current = view.findViewById(R.id.current);


        return new ViewHolder(view);
    }
    private TextView name;
    private  TextView status;
    private ImageView image;
    private TextView current;
    private ConstraintLayout item_belt;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtTitle = chapterList.get(i).getName();
        String s = chapterList.get(i).getColor();
        status.setText(s);
        name.setText(txtTitle);
        Picasso.get().load(chapterList.get(i).getLinkImage()).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(image);


        if(Integer.parseInt(b.getId()) == chapterList.get(i).getId()){
            current.setText("Đai hiện tại");
            if(languageS!= null){
                if(languageS.contains("en")){
                    current.setText("Current belt");
                }
            }
        }
        else {
            current.setText("");
        }

        int id = chapterList.get(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Belt_Payment.class);
                intent.putExtra("id",id);
                intent.putExtra("current",b.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtInfo;

        ViewHolder(View itemView) {
            super(itemView);
            txtInfo = itemView.findViewById(R.id.txtInfo);
        }
    }
}
