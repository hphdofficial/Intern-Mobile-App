package fascon.vovinam.vn.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import fascon.vovinam.vn.R;
import fascon.vovinam.vn.View.activity_chapters;
import fascon.vovinam.vn.Model.Lesson;

import java.util.ArrayList;

public class lesson_adapter extends RecyclerView.Adapter<lesson_adapter.ViewHolder>{
    Context context;

    ArrayList<Lesson> lessonList = new ArrayList<>();

    public lesson_adapter(Context context, ArrayList<Lesson> lessonList) {
        this.context = context;
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lesson, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String txtTitle = lessonList.get(i).getTitle();
        String txtObject = lessonList.get(i).getObject();
        String txtRequire = lessonList.get(i).getRequire();
        String txtTime = lessonList.get(i).getTime();

        viewHolder.txtTitle.setText(txtTitle);
        viewHolder.txtObject.setText(txtObject);
        viewHolder.txtRequire.setText(txtRequire);
        viewHolder.txtTime.setText(txtTime);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, activity_chapters.class);
                intent.putExtra("title", txtTitle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtObject, txtRequire, txtTime;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtObject = itemView.findViewById(R.id.txtObject);
            txtRequire = itemView.findViewById(R.id.txtRequire);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
