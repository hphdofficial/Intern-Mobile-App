package com.android.mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.mobile.models.ReviewModel;
import com.android.mobile.network.ApiServiceProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentWriteReviewActivity extends DialogFragment {

    private static final String ARG_PRODUCT_ID = "product_id";

    private RatingBar ratingBar;
    private EditText editReviewContent;
    private Button btnSubmitReview;
    private int productId;
    private OnReviewSubmittedListener listener;

    public static FragmentWriteReviewActivity newInstance(int productId) {
        FragmentWriteReviewActivity fragment = new FragmentWriteReviewActivity();
        Bundle args = new Bundle();
        args.putInt(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnReviewSubmittedListener) {
            listener = (OnReviewSubmittedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnReviewSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);

        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
        }

        ratingBar = view.findViewById(R.id.rating_bar);
        editReviewContent = view.findViewById(R.id.edit_review_content);
        btnSubmitReview = view.findViewById(R.id.btn_submit_review);

        btnSubmitReview.setOnClickListener(v -> submitReview());

        return view;
    }

    private void submitReview() {
        float ratingValue = ratingBar.getRating();
        String reviewContent = editReviewContent.getText().toString().trim();

        if (ratingValue == 0 || TextUtils.isEmpty(reviewContent)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);
        String userName = sharedPreferences.getString("ten", "User");
        String avatarUrl = sharedPreferences.getString("avatar_url", ""); // Lấy avatar URL
        String reviewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ReviewModel review = new ReviewModel();
        review.setProductID(productId);
        review.setRatingValue(String.valueOf(ratingValue));
        review.setReviewContent(reviewContent);
        review.setUserName(userName);
        review.setReviewDate(reviewDate);
        review.setAvatarUrl(avatarUrl); // Lưu avatar URL vào ReviewModel

        ApiServiceProvider.getProductApiService().addReview("Bearer " + token, review).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đánh giá đã được gửi", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onReviewSubmitted();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi gửi đánh giá", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi gửi đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnReviewSubmittedListener {
        void onReviewSubmitted();
    }
}
