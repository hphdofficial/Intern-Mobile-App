package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.adapter.ReviewAdapter;
import com.android.mobile.models.ReviewModel;
import com.android.mobile.models.ProfileModel;
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
    private int productId;
    private TextView ratingValue;
    private RatingBar ratingBar;
    private TextView filterAll, filter5Star, filter4Star, filter3Star, filter2Star, filter1Star;
    private TextView[] filterButtons;

    private BlankFragment loadingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Đánh giá sản phẩm");
        myContentE.apply();

        // Chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ratingValue = findViewById(R.id.rating_value);
        ratingBar = findViewById(R.id.rating_bar);

        reviewsRecyclerView = findViewById(R.id.reviews_recycler_view);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        productId = getIntent().getIntExtra("productId", -1);

        filterAll = findViewById(R.id.filter_all);
        filter5Star = findViewById(R.id.filter_5_star);
        filter4Star = findViewById(R.id.filter_4_star);
        filter3Star = findViewById(R.id.filter_3_star);
        filter2Star = findViewById(R.id.filter_2_star);
        filter1Star = findViewById(R.id.filter_1_star);

        filterButtons = new TextView[]{filterAll, filter5Star, filter4Star, filter3Star, filter2Star, filter1Star};

        filterAll.setOnClickListener(v -> {
            setActiveFilter(0);
            filterReviews(0);
        });
        filter5Star.setOnClickListener(v -> {
            setActiveFilter(5);
            filterReviews(5);
        });
        filter4Star.setOnClickListener(v -> {
            setActiveFilter(4);
            filterReviews(4);
        });
        filter3Star.setOnClickListener(v -> {
            setActiveFilter(3);
            filterReviews(3);
        });
        filter2Star.setOnClickListener(v -> {
            setActiveFilter(2);
            filterReviews(2);
        });
        filter1Star.setOnClickListener(v -> {
            setActiveFilter(1);
            filterReviews(1);
        });

        loadReviews();

        findViewById(R.id.btn_write_review).setOnClickListener(v -> {
            FragmentWriteReviewActivity fragment = FragmentWriteReviewActivity.newInstance(productId);
            fragment.show(getSupportFragmentManager(), "WriteReview");
        });
    }


    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }

    private void loadReviews() {
        showLoading();
        ApiServiceProvider.getProductApiService().getProductReviews(productId).enqueue(new Callback<List<ReviewModel>>() {
            @Override
            public void onResponse(Call<List<ReviewModel>> call, Response<List<ReviewModel>> response) {
                hideLoading();
                if (response.isSuccessful() && response.body() != null) {
                    allReviews = response.body();
                    Collections.reverse(allReviews);
                    loadUserProfiles();
                } else {
                    hideLoading();
                    Toast.makeText(ReviewActivity.this, "Không có đánh giá nào.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewModel>> call, Throwable t) {
                hideLoading();
                Toast.makeText(ReviewActivity.this, "Lỗi khi lấy đánh giá.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserProfiles() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        for (ReviewModel review : allReviews) {
            int memberId = review.getId_atg_members();
            ApiServiceProvider.getUserApiService().getProfileViaId("Bearer " + token, memberId).enqueue(new Callback<ProfileModel>() {
                @Override
                public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileModel profile = response.body();
                        review.setUserName(profile.getUsername());
                        review.setAvatarUrl(profile.getAvatar());
                    }
                    reviewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ProfileModel> call, Throwable t) {
                    Toast.makeText(ReviewActivity.this, "Lỗi khi lấy thông tin hồ sơ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        reviewAdapter = new ReviewAdapter(ReviewActivity.this, allReviews);
        reviewsRecyclerView.setAdapter(reviewAdapter);
        updateRatingSummary();
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
        ApiServiceProvider.getProductApiService().getProductReviews(productId).enqueue(new Callback<List<ReviewModel>>() {
            @Override
            public void onResponse(Call<List<ReviewModel>> call, Response<List<ReviewModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReviewModel> newReviews = response.body();
                    Collections.reverse(newReviews);
                    ReviewModel newReview = newReviews.get(0);

                    SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString("access_token", null);
                    int memberId = newReview.getId_atg_members();

                    ApiServiceProvider.getUserApiService().getProfileViaId("Bearer " + token, memberId).enqueue(new Callback<ProfileModel>() {
                        @Override
                        public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                ProfileModel profile = response.body();
                                newReview.setUserName(profile.getUsername());
                                newReview.setAvatarUrl(profile.getAvatar());

                                allReviews.add(0, newReview);
                                reviewAdapter.notifyItemInserted(0);
                                updateRatingSummary();
                                reviewsRecyclerView.scrollToPosition(0);
                            } else {
                                Toast.makeText(ReviewActivity.this, "Lỗi khi lấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileModel> call, Throwable t) {
                            Toast.makeText(ReviewActivity.this, "Lỗi khi lấy thông tin người dùng.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ReviewActivity.this, "Lỗi khi lấy đánh giá mới.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewModel>> call, Throwable t) {
                Toast.makeText(ReviewActivity.this, "Lỗi khi lấy đánh giá mới.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setActiveFilter(int starRating) {
        for (TextView filterButton : filterButtons) {
            filterButton.setBackgroundResource(R.drawable.filter_unselected_background);
        }

        switch (starRating) {
            case 0:
                filterAll.setBackgroundResource(R.drawable.filter_selected_background);
                break;
            case 5:
                filter5Star.setBackgroundResource(R.drawable.filter_selected_background);
                break;
            case 4:
                filter4Star.setBackgroundResource(R.drawable.filter_selected_background);
                break;
            case 3:
                filter3Star.setBackgroundResource(R.drawable.filter_selected_background);
                break;
            case 2:
                filter2Star.setBackgroundResource(R.drawable.filter_selected_background);
                break;
            case 1:
                filter1Star.setBackgroundResource(R.drawable.filter_selected_background);
                break;
        }
    }
}
