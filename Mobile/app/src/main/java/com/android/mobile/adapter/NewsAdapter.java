package com.android.mobile.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.NewsDetailActivity;
import com.android.mobile.R;
import com.android.mobile.models.NewsModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsModel> newsList;
    private OnNewsClickListener listener;

    public NewsAdapter(List<NewsModel> newsList, OnNewsClickListener listener) {
        this.newsList = newsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsModel news = newsList.get(position);
        holder.newsTitle.setText(news.getTenvi());

        // Construct the correct full URL for the image
        String imageUrl = "http://tambinh.websinhvien.net/thumbs/340x280x1/upload/news/" + news.getPhoto();

        // Log the URL to check if it's correct
        Log.d("NewsAdapter", "Loading image from URL: " + imageUrl);

        // Load image using Glide with fitCenter
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .fitCenter()
                .error(R.drawable.ic_launcher_foreground) // Set an error image in case the URL is wrong
                .into(holder.newsImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), NewsDetailActivity.class);
            intent.putExtra("NewsTitle", news.getTenvi());
            intent.putExtra("NewsContent", news.getNoidungvi());
            intent.putExtra("NewsImage", imageUrl); // Pass the full image URL
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
