package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fascon.vovinam.vn.Model.ReviewModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;

import java.io.IOException;
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

    private BlankFragment loadingFragment;
    private String languageS;


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
private TextView reviewP;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_review, container, false);
        SharedPreferences shared = getContext().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        languageS = shared.getString("language",null);

        if (getArguments() != null) {
            productId = getArguments().getInt(ARG_PRODUCT_ID);
        }

        ratingBar = view.findViewById(R.id.rating_bar);
        editReviewContent = view.findViewById(R.id.edit_review_content);
        btnSubmitReview = view.findViewById(R.id.btn_submit_review);
        reviewP = view.findViewById(R.id.reviewP);

        btnSubmitReview.setOnClickListener(v -> submitReview());
        if(languageS!= null){
            if(languageS.contains("en")){
                reviewP.setText("Product reviews");
                editReviewContent.setHint("Enter my review");
                btnSubmitReview.setText("Send review");
            }
        }
        return view;
    }

    private void showLoading() {
        if (loadingFragment == null) {
            loadingFragment = new BlankFragment();
            loadingFragment.show(getActivity().getSupportFragmentManager(), "loading");
        }
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
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
        int memberId = sharedPreferences.getInt("member_id", -1);
        if (token == null || memberId == -1) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để gửi đánh giá", Toast.LENGTH_SHORT).show();
            return;
        }



        String reviewDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        ReviewModel review = new ReviewModel();
        review.setProductID(productId);  // Đảm bảo rằng ProductID được đặt đúng cách
        review.setRatingValue(String.valueOf(Math.round(ratingValue)));  // Đặt RatingValue dưới dạng chuỗi
        review.setReviewContent(reviewContent);
        review.setReviewDate(reviewDate);
        review.setId_atg_members(memberId);
        review.setRatingCount(1);  // Đặt giá trị RatingCount

        showLoading();
        ApiServiceProvider.getProductApiService().addReview("Bearer " + token, productId, review).enqueue(new Callback<ReviewModel>() {
            @Override
            public void onResponse(Call<ReviewModel> call, Response<ReviewModel> response) {
                hideLoading();
                if (response.isSuccessful()) {
                    ReviewModel createdReview = response.body();
                    Toast.makeText(getContext(), "Đánh giá đã được gửi", Toast.LENGTH_SHORT).show();
                    if (createdReview != null) {
                        Log.i("ReviewSubmit", "Created ReviewID: " + createdReview.getReviewID());
                    }
                    if (listener != null) {
                        listener.onReviewSubmitted();
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi gửi đánh giá: " + response.message(), Toast.LENGTH_SHORT).show();
                    // Thêm log để kiểm tra lỗi chi tiết hơn
                    Log.e("ReviewSubmit", "Error response code: " + response.code() + " - " + response.message());
                    try {
                        Log.e("ReviewSubmit", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Toast.makeText(getContext(), "Lỗi khi gửi đánh giá. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewModel> call, Throwable t) {
                hideLoading();
                Toast.makeText(getContext(), "Lỗi khi gửi đánh giá: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ReviewSubmit", "Error: ", t);
            }
        });
    }

    public interface OnReviewSubmittedListener {
        void onReviewSubmitted();
    }
}
