package com.android.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.R;
import com.android.mobile.models.ReviewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<ReviewModel> reviewList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        ReviewModel review = reviewList.get(position);
        holder.userName.setText(review.getUserName());
        holder.reviewDate.setText(review.getReviewDate());
        holder.reviewContent.setText(review.getReviewContent());
        holder.userRating.setRating(Float.parseFloat(review.getRatingValue()));

        // Load avatar từ URL
        Picasso.get().load(review.getAvatarUrl()).placeholder(R.drawable.photo3x4).error(R.drawable.photo3x4).into(holder.userAvatar);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void updateReviews(List<ReviewModel> reviews) {
        this.reviewList = reviews;
        notifyDataSetChanged();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView userAvatar;
        TextView userName, reviewDate, reviewContent;
        RatingBar userRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            userName = itemView.findViewById(R.id.user_name);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewContent = itemView.findViewById(R.id.review_content);
            userRating = itemView.findViewById(R.id.user_rating);
        }
    }
}
