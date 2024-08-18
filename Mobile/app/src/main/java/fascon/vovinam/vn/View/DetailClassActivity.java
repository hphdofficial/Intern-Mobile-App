package fascon.vovinam.vn.View;

import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.Model.services.ClubApiService;
import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.Model.Class;
import fascon.vovinam.vn.Model.ReponseModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClassApiService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailClassActivity extends BaseActivity {
    private SharedPreferences sharedPreferences;
    private TextView txtNameClass;
    private TextView txtTeacherClass;
    private TextView txtTimeLearn;
    private TextView txtAddressClass;
    private TextView txtInfoClass;
    private Button btnJoinClassPending;
    private Button btnCancelClassPending;
    private Button btnLeaveClassPending;
    //    private Button btnDirectClass;
    private String idClass = null;
    private String idClub = null;
    private List<Class> listClassPending = new ArrayList<>();
    private boolean isPending = false;
    private String name = "";
    private String nameClass = "";
    private int fee = 0;
    private BlankFragment loadingFragment;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        languageS = sharedPreferences.getString("language",null);
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Chi tiết lớp học");
        myContentE.apply();
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Class Detail");
                myContentE.apply();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        txtNameClass = findViewById(R.id.nameClass);
        txtTeacherClass = findViewById(R.id.nameTeacher);
        txtAddressClass = findViewById(R.id.addressLearn);
        txtTimeLearn = findViewById(R.id.timeLearn);
        txtInfoClass = findViewById(R.id.infoClass);
        btnJoinClassPending = findViewById(R.id.btn_join_class_pending);
        btnCancelClassPending = findViewById(R.id.btn_cancel_class_pending);
        btnLeaveClassPending = findViewById(R.id.btn_leave_class_pending);
//        btnDirectClass = findViewById(R.id.btn_direct_class);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                idClass = bundle.getString("id_class");
                idClub = bundle.getString("id_club");
            }
        }


        btnJoinClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClassPending();
            }
        });

        btnCancelClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClassPending();
            }
        });

        btnLeaveClassPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveClassPending();
            }
        });

//        btnDirectClass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                directJoinedClass();
//            }
//        });

        getDetailClass();

        textViewNgaysinhLabel = findViewById(R.id.textViewNgaysinhLabel);
        textViewTenLabel = findViewById(R.id.textViewTenLabel);
        textViewDienthoaiLabel = findViewById(R.id.textViewDienthoaiLabel);
        textViewDiachiLabel = findViewById(R.id.textViewDiachiLabel);
        textViewGioitinhLabel = findViewById(R.id.textViewGioitinhLabel);
        if(languageS != null){
            if(languageS.contains("en")){

                textViewTenLabel.setText("Lecturer");
                textViewDienthoaiLabel.setText("School schedule");
                textViewDiachiLabel.setText("Address");
                textViewGioitinhLabel.setText("Contact information");
                textViewNgaysinhLabel.setText("Class Name");

                btnCancelClassPending.setText("Cancel participation request");
                btnLeaveClassPending.setText("Register to participate");
            }
        }
    }

    public void getDetailClass() {
        showLoading();

        getListClassPending();

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<Class> call = service.getDetailClassofClub(Integer.parseInt(idClass));

        call.enqueue(new Callback<Class>() {
            @Override
            public void onResponse(Call<Class> call, Response<Class> response) {
                if (response.isSuccessful()) {
                    Class dataClass = response.body();
                    nameClass = dataClass.getTen();
                    name = dataClass.getGiangvien();
                    fee = dataClass.getHocPhi();
                    txtNameClass.setText(dataClass.getTen());
                    txtTeacherClass.setText(dataClass.getGiangvien());
                    txtAddressClass.setText(dataClass.getClub());
                    txtTimeLearn.setText(dataClass.getThoigian());
                    txtInfoClass.setText(dataClass.getGiangvien());
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<Class> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }
    private TextView textViewTenLabel;
    private TextView textViewDienthoaiLabel;
    private TextView textViewDiachiLabel;
    private TextView textViewGioitinhLabel;
    private TextView textViewNgaysinhLabel;

    public void getListClassPending() {
        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<List<Class>> call = service.getListClassPending("Bearer" + token);

        call.enqueue(new Callback<List<Class>>() {
            @Override
            public void onResponse(Call<List<Class>> call, Response<List<Class>> response) {
                hideLoading();

                if (response.isSuccessful()) {
                    listClassPending = response.body();
                    if (!listClassPending.isEmpty()) {
                        for (Class clb : listClassPending) {
                            if (String.valueOf(clb.getId_class()).equals(idClass)) {
                                isPending = true;
                                break;
                            }
                            isPending = false;
                        }
                    }
                    setupButton();
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Class>> call, Throwable t) {
                hideLoading();

                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void setupButton() {
        if (!Objects.equals(decodeRoleFromToken(sharedPreferences.getString("access_token", null)), "0")){
            btnJoinClassPending.setVisibility(View.GONE);
            btnCancelClassPending.setVisibility(View.GONE);
            btnLeaveClassPending.setVisibility(View.GONE);
        } else {
            String idClassShared = sharedPreferences.getString("id_class_shared", null);

            if (idClassShared == null) {
                btnJoinClassPending.setVisibility(isPending ? View.GONE : View.VISIBLE);
                btnCancelClassPending.setVisibility(isPending ? View.VISIBLE : View.GONE);
            } else if (!idClass.equals(idClassShared)) {
//            btnDirectClass.setVisibility(View.VISIBLE);
            } else {
                btnLeaveClassPending.setVisibility(View.VISIBLE);
            }
        }

    }

    public void joinClassPending() {
        btnJoinClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_LONG).show();

        isPending = true;
        setupButton();
        btnJoinClassPending.setEnabled(true);
        Toast.makeText(DetailClassActivity.this, "Đã gửi yêu cầu tham gia lớp học", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_class", idClass);
        intent.putExtra("name", name);
        intent.putExtra("nameClass", nameClass);
        intent.putExtra("idClass", idClass);
        intent.putExtra("fee", fee);
        intent.putExtras(bundle);
        startActivity(intent);

        String token = sharedPreferences.getString("access_token", null);
        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> callclass = service.joinClassPending("Bearer" + token, Integer.parseInt(idClass));

        callclass.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void cancelClassPending() {
        btnCancelClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_LONG).show();

        isPending = false;
        setupButton();
        btnCancelClassPending.setEnabled(true);
        Toast.makeText(DetailClassActivity.this, "Đã hủy yêu cầu tham gia lớp học", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);
        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.cancelClassPending("Bearer" + token, Integer.parseInt(idClass));

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                } else {
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Log.e("Fail", t.getMessage());
            }
        });
    }

    private void leaveClassPending() {
        btnLeaveClassPending.setEnabled(false);
        Toast.makeText(DetailClassActivity.this, "Đang xử lý...", Toast.LENGTH_SHORT).show();

        String token = sharedPreferences.getString("access_token", null);

        ClassApiService service = ApiServiceProvider.getClassApiService();
        Call<ReponseModel> call = service.leaveClassPending("Bearer" + token);

        call.enqueue(new Callback<ReponseModel>() {
            @Override
            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
                if (response.isSuccessful()) {
                    isPending = false;
                    setupButton();
                    btnLeaveClassPending.setEnabled(true);
                    btnLeaveClassPending.setVisibility(View.GONE);
                    Toast.makeText(DetailClassActivity.this, "Đã gửi yêu cầu rời lớp học", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailClassActivity.this, "Yêu cầu của bạn đang chờ duyệt", Toast.LENGTH_SHORT).show();
                    Log.e("Error", response.message());
                }
            }

            @Override
            public void onFailure(Call<ReponseModel> call, Throwable t) {
                Toast.makeText(DetailClassActivity.this, "Yêu cầu của bạn đang chờ duyệt", Toast.LENGTH_SHORT).show();
                Log.e("Fail", t.getMessage());
            }
        });
    }

    public void directJoinedClass() {
        Intent intent = new Intent(DetailClassActivity.this, DetailClassActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id_class", sharedPreferences.getString("id_class_shared", null));
        intent.putExtras(bundle);
        startActivity(intent);
    }

//    public void getMyClass() {
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClassApiService service = ApiServiceProvider.getClassApiService();
//        Call<Class> call = service.getDetailClassMember("Bearer" + token);
//
//        call.enqueue(new Callback<Class>() {
//            @Override
//            public void onResponse(Call<Class> call, Response<Class> response) {
//                if (response.isSuccessful()) {
//                    Class classs = response.body();
//
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_class_shared", String.valueOf(classs != null ? classs.getId() : null));
//                    editor.apply();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Class> call, Throwable t) {
//                Toast.makeText(DetailClassActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void registerClass() {
//        Intent intent = new Intent(DetailClassActivity.this, RegisterClass.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("id_class", idClass);
//        intent.putExtra("name", name);
//        intent.putExtra("nameClass", nameClass);
//        intent.putExtra("idClass", idClass);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
//
//    public void leaveClass() {
//        showLoading();
//
//        String token = sharedPreferences.getString("access_token", null);
//
//        ClassApiService service = ApiServiceProvider.getClassApiService();
//        Call<ReponseModel> call = service.leaveClass("Bearer" + token);
//
//        call.enqueue(new Callback<ReponseModel>() {
//            @Override
//            public void onResponse(Call<ReponseModel> call, Response<ReponseModel> response) {
//                hideLoading();
//
//                if (response.isSuccessful()) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("id_class_shared", null);
//                    editor.apply();
//
//                    btnCancelClassPending.setVisibility(View.GONE);
//                    btnJoinClassPending.setVisibility(View.VISIBLE);
//
//                    Toast.makeText(DetailClassActivity.this, "Rời lớp học thành công", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ReponseModel> call, Throwable t) {
//                hideLoading();
//
//                Toast.makeText(DetailClassActivity.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getSupportFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
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

    public static String decodeRoleFromToken(String jwtToken) {
        try {
            // Tách token thành các phần: header, payload, và signature
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // Phần payload là phần thứ 2
            String payload = parts[1];

            // Giải mã payload từ Base64Url
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decodedPayload = new String(decodedBytes, StandardCharsets.UTF_8);

            // Chuyển đổi payload thành JSONObject
            JSONObject jsonObject = new JSONObject(decodedPayload);

            // Trích xuất role từ payload
            return jsonObject.getString("role");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}