package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.StatusRegister;
import fascon.vovinam.vn.Model.network.APIServicePayment;
import fascon.vovinam.vn.Model.services.PaymentAPI;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment extends BaseActivity {
/*    private FrameLayout frameLayout;
    private ImageView imageView;
    private View borderView;*/
    private Button payment;
    private ImageView sub_menu;
    private String languageS;

    private TextView textViewFeeAmount;
    private TextView textViewHealthStatus;
    private TextView textViewClass;
    private TextView textViewInstructorName;
    String id_class = null;
    private String link = null;
    private ImageView qrcode;
    private TextView Title;
    private TextView textViewHealthStatusLabel;
    private  TextView textViewInstructor;
    private TextView textViewCourseFee;
    private TextView text;
    public void onMenuItemClick(View view) {
        text = findViewById(R.id.languageText);
        String language = text.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text.setText("ENG");
            }else {
                edit.putString("language","vn");
                text.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);

/*        frameLayout = findViewById(R.id.frameLayout);
        imageView = findViewById(R.id.payment_momo);
        borderView = findViewById(R.id.borderView);*/
        payment = findViewById(R.id.payment);
/*        textViewFullName = findViewById(R.id.textViewFullName);
        textViewPhoneNumber = findViewById(R.id.textViewPhoneNumber);
        textViewDOB = findViewById(R.id.textViewDOB);*/
        textViewFeeAmount = findViewById(R.id.textViewFeeAmount);
        textViewHealthStatus = findViewById(R.id.textViewHealthStatus);
        textViewHealthStatusLabel = findViewById(R.id.textViewHealthStatusLabel);
        textViewClass = findViewById(R.id.textViewClass);
        textViewInstructorName = findViewById(R.id.textViewInstructorName);
        textViewInstructor = findViewById(R.id.textViewInstructor);
        textViewCourseFee = findViewById(R.id.textViewCourseFee);
        qrcode = findViewById(R.id.qrcode);
        Title = findViewById(R.id.Title);
        if(languageS != null){
            if(languageS.contains("en")){
                Title.setText("Register Information");
                textViewHealthStatusLabel.setText("Health Status");
                textViewInstructor.setText("Lecturer");
                textViewCourseFee.setText("Payment amount");
                payment.setText("payment online ");
            }
        }


        SharedPreferences infor = getSharedPreferences("infor", Context.MODE_PRIVATE);

/*        textViewFullName.setText(infor.getString("name","null"));
        textViewPhoneNumber.setText(infor.getString("phone","null"));
        textViewDOB.setText(infor.getString("birthday","null"));*/

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String total = "";

        if (extras != null) {
            // Lấy dữ liệu từ Bundle
            String className = extras.getString("className");
            String teacherName = extras.getString("teacherName");
            Double money = extras.getDouble("money");
            String note =  extras.getString("note");

            textViewHealthStatus.setText(note);
            textViewClass.setText("Lớp học: \n"+className);
            if(languageS != null){
                if(languageS.contains("en")){
                    textViewClass.setText("Class: \n"+className);
                }
            }
            textViewInstructorName.setText(teacherName);
            textViewFeeAmount.setText(money.toString()+" VND");
            total = money.toString();
        }
        displayQRCode("https://api.vietqr.io/image/970425-0937759311-cG4PADy.jpg&amount=" + total+

                "&addInfo=" + "thanh toan class");


/*        onImageViewClicked(imageView);*/
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("access_token", null);
                Double money = 0.0;

                if (extras != null) {


                   money = extras.getDouble("money");

                   id_class = extras.getString("idClass");
                }



//                     id_class = "1";
                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
                Call<ResponseBody> call = apiService.RegisterClass("Bearer" + token,id_class + "",money);


                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            assert response.body() != null;

                            try {
                               link = response.body().string();

                                if(link != null){


                                    String url = link;

                                    // Tạo một Intent với action ACTION_VIEW và URL dưới dạng Uri
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));

                                    // Bắt đầu Activity với Intent
                                    startActivity(intent);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        }else {
                            Toast.makeText(getApplicationContext(),"Loi",Toast.LENGTH_SHORT).show();
                            Log.e("errorTo",response.message());
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"fails",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //chèn fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();
    }
/*    public void onImageViewClicked(ImageView view) {
        // Kiểm tra nếu imageView đã có border thì loại bỏ, ngược lại thêm border
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (borderView.getVisibility() == View.INVISIBLE) {
                    borderView.setVisibility(View.VISIBLE);
                } else {
                    borderView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }*/
/*    public void GetEventFrament(){
        titleFragment myFragment = (titleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        sub_menu = myFragment.getView().findViewById(R.id.img_menu);
        sub_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"abc",Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void displayQRCode(String qrCodeUrl) {
        ImageView qrCodeImageView = findViewById(R.id.qrcode);
        Glide.with(this)
                .load(qrCodeUrl)
                .into(qrCodeImageView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        GetStatus();
    }
    public void GetStatus(){
            if(link != null){
                String s = link.replace("https://vovinammoi-4bedb6dd1c05.herokuapp.com/pay_clb/show?id=","");
                String arr[] = s.split("&");
                PaymentAPI apiService = APIServicePayment.getPaymentApiService();
                Call<StatusRegister> call = apiService.GetStatusRegister(Integer.parseInt(arr[0]));
                call.enqueue(new Callback<StatusRegister>() {
                    @Override
                    public void onResponse(Call<StatusRegister> call, Response<StatusRegister> response) {
                        if(response.isSuccessful()){
                            StatusRegister status = response.body();
                            if(status.getStatus().contains("thành công")){
                                Toast.makeText(getApplicationContext(),"Thanh toán thành công, mã hóa đơn " +arr[0],Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(),"Thanh toán thát bại",Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<StatusRegister> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"fail nn",Toast.LENGTH_SHORT).show();
                    }
                });

            }
    }
}