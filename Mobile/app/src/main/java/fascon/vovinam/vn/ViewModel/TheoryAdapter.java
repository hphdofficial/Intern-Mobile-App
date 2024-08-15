package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.R;
import fascon.vovinam.vn.View.activity_item_chapter;
import fascon.vovinam.vn.Model.TheoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TheoryAdapter extends RecyclerView.Adapter<TheoryAdapter.ViewHolder>{
    Context context;
    List<TheoryModel> theoryModelList;

    public TheoryAdapter(Context context, List<TheoryModel> theoryModelList) {
        this.theoryModelList = theoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false);
        return new TheoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtTitle = theoryModelList.get(i).getTenvi();
        viewHolder.txtTitle.setText(txtTitle);
        viewHolder.txtTitle.setTextSize(16);

        String imgTheory = theoryModelList.get(i).getPhoto();
        if (imgTheory != null) {
            Picasso.get().load(imgTheory).placeholder(R.drawable.photo3x4).into(viewHolder.imgTheory);
        }

        int idTheory = theoryModelList.get(i).getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_item_chapter.class);
                intent.putExtra("id", idTheory);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return theoryModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        ImageView imgTheory;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.news_title);
            imgTheory = itemView.findViewById(R.id.news_image);
        }
    }
}
