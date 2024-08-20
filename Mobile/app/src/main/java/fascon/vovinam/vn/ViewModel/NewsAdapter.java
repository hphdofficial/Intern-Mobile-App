package fascon.vovinam.vn.ViewModel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.View.NewsDetailActivity;
import fascon.vovinam.vn.R;
import fascon.vovinam.vn.Model.NewsModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsModel> newsList;
    private OnNewsClickListener listener;
    private String languageS;
    public NewsAdapter(List<NewsModel> newsList, OnNewsClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        SharedPreferences shared =   view.getContext().getSharedPreferences("login_prefs", view.getContext().MODE_PRIVATE);
        languageS = shared.getString("language",null);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);


        holder.newsTitle.setText(news.getTenvi());
        if (languageS != null && languageS.contains("en")) {
            holder.newsTitle.setText(news.getTenen());
        } else {
            holder.newsTitle.setText(news.getTenvi());
        }

        // Khai báo biến imageUrl trước khối if
        final String imageUrl;

        String photoPath = news.getPhoto();
        if (photoPath != null && !photoPath.isEmpty()) {
            if (photoPath.startsWith("http://") || photoPath.startsWith("https://")) {
                imageUrl = photoPath; // photoPath đã là một URL đầy đủ
            } else {
                imageUrl = "http://tambinh.websinhvien.net/thumbs/340x280x1/upload/news/" + photoPath;
            }

            // Log the URL to check if it's correct
            Log.d("NewsAdapter", "Loading image from URL: " + imageUrl);

            // Load image using Glide with fitCenter
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image
                    .error(R.drawable.newsvovietdao) // Error image
                    .fitCenter()
                    .into(holder.newsImage);
        } else {
            holder.newsImage.setImageResource(R.drawable.newsvovietdao);
            imageUrl = null;
        }

        // Sử dụng biến imageUrl trong OnClickListener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), NewsDetailActivity.class);
            // Truyền cả 4 trường qua Intent
            intent.putExtra("NewsTitleVi", news.getTenvi());
            intent.putExtra("NewsContentVi", news.getNoidungvi());
            intent.putExtra("NewsTitleEn", news.getTenen());
            intent.putExtra("NewsContentEn", news.getNoidungen());
            intent.putExtra("NewsImage", imageUrl);
            holder.itemView.getContext().startActivity(intent);
        });
    }





    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView newsImage;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
        }
    }

    public interface OnNewsClickListener {
        void onNewsClick(NewsModel news);
    }
}
