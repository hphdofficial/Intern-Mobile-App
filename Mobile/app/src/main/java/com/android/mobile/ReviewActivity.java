package com.android.mobile;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.ReviewAdapter;
import com.android.mobile.models.ReviewModel;
import com.android.mobile.network.ApiServiceProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends BaseActivity implements FragmentWriteReviewActivity.OnReviewSubmittedListener {

    private RecyclerView reviewsRecyclerView;
    private ReviewAdapter reviewAdapter;
    private List<ReviewModel> allReviews;
    private int productId = 1;
    private TextView ratingValue;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ratingValue = findViewById(R.id.rating_value);
        ratingBar = findViewById(R.id.rating_bar);

        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup filter TextViews
        findViewById(R.id.filter_all).setOnClickListener(v -> filterReviews(0));
        findViewById(R.id.filter_5_star).setOnClickListener(v -> filterReviews(5));
        findViewById(R.id.filter_4_star).setOnClickListener(v -> filterReviews(4));
        findViewById(R.id.filter_3_star).setOnClickListener(v -> filterReviews(3));
        findViewById(R.id.filter_2_star).setOnClickListener(v -> filterReviews(2));
        findViewById(R.id.filter_1_star).setOnClickListener(v -> filterReviews(1));

        loadReviews();

        findViewById(R.id.btn_write_review).setOnClickListener(v -> {
            FragmentWriteReviewActivity fragment = FragmentWriteReviewActivity.newInstance(productId);
            fragment.show(getSupportFragmentManager(), "WriteReview");
        });
    }

    private void loadReviews() {
        ApiServiceProvider.getProductApiService().getProductReviews(productId).enqueue(new Callback<List<ReviewModel>>() {
            @Override
            public void onResponse(Call<List<ReviewModel>> call, Response<List<ReviewModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allReviews = response.body();
                    // Đảo ngược thứ tự của danh sách đánh giá
                    Collections.reverse(allReviews);
                    reviewAdapter = new ReviewAdapter(ReviewActivity.this, allReviews);
                    reviewsRecyclerView.setAdapter(reviewAdapter);
                    updateRatingSummary();
                } else {
                    Toast.makeText(ReviewActivity.this, "Không có đánh giá nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewModel>> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "Lỗi khi lấy đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterReviews(int starRating) {
        List<ReviewModel> filteredReviews = new ArrayList<>();
        if (starRating == 0) {
            filteredReviews = allReviews;
        } else {
            for (ReviewModel review : allReviews) {
                if (Math.round(Float.parseFloat(review.getRatingValue())) == starRating) {
                    filteredReviews.add(review);
                }
            }
        }
        reviewAdapter.updateReviews(filteredReviews);
    }

    private void updateRatingSummary() {
        if (allReviews == null || allReviews.isEmpty()) {
            ratingValue.setText("0.0");
            ratingBar.setRating(0.0f);
            return;
        }

        float totalRating = 0.0f;
        for (ReviewModel review : allReviews) {
            totalRating += Float.parseFloat(review.getRatingValue());
        }

        float averageRating = totalRating / allReviews.size();
        ratingValue.setText(String.format("%.1f", averageRating));
        ratingBar.setRating(averageRating);
    }

    @Override
    public void onReviewSubmitted() {
        loadReviews(); // Tải lại danh sách đánh giá sau khi đánh giá mới được thêm
    }
}
