package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.addressAdapter;
import fascon.vovinam.vn.Model.addressModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressP extends BaseActivity implements addressAdapter.ItemClickListener {

    private RecyclerView recyclerView;
    private addressAdapter adapter;
    private List<addressModel> list = new ArrayList<>();
    private Button addAddress;
    private LinearLayout layout1;
    private EditText text;
    private Button btn_add;
    private EditText text_phone;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address_p);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Địa chỉ giao hàng");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Delivery address");
                myContentE.apply();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        // tạo mẫu
        adapter = new addressAdapter(this, list);
        recyclerView = findViewById(R.id.recycler_shipping_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        addAddress = findViewById(R.id.add_address);
        layout1 = findViewById(R.id.layout1);
        text = findViewById(R.id.text_add);
        btn_add = findViewById(R.id.add_address1);
        text_phone = findViewById(R.id.text_phone);


        //get list address
        SharedPreferences sharedPreferences = getSharedPreferences("myAddress", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list",null);

        if(json != null){
            Type type = new TypeToken<ArrayList<addressModel>>() {}.getType();
            list = gson.fromJson(json, type);

            adapter.setData(list);
            adapter.notifyDataSetChanged();
        }
        layout1.setVisibility(View.GONE);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.GONE);
                addAddress.setVisibility(View.VISIBLE);
                String s = text.getText().toString();
                String phone = text_phone.getText().toString();

                if(phone.length() != 10){
                    Toast.makeText(getApplicationContext(),"Số điện thoại không hợp lệ",Toast.LENGTH_SHORT).show();
                }
                if(s.length()> 0 && phone.length() == 10){
                    SharedPreferences sharedPreferences = getSharedPreferences("myAddress", Context.MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("list",null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if(json != null){
                        Type type = new TypeToken<ArrayList<addressModel>>() {}.getType();
                        list = gson.fromJson(json, type);



                        // thay doi data
                        addressModel a = new addressModel(s,phone,0);

                        list.add(a);

                        //them data
                        String jsonadd = gson.toJson(list);
                        editor.putString("list", jsonadd);
                        editor.apply();

                        adapter.setData(list);
                        adapter.notifyDataSetChanged();

                    }

                }

            }
        });

        // thêm địa chỉ
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.size()> 4){
                    Toast.makeText(getApplicationContext(),"Vui lòng xóa bớt địa chỉ trước khi thêm",Toast.LENGTH_SHORT).show();
                }else {
                    layout1.setVisibility(View.VISIBLE);
                    addAddress.setVisibility(View.GONE);
                }

//goi lai data







            }
        });
       // getAddress();
        if(languageS != null){
            if(languageS.contains("en")){
                addAddress.setText("Add shipping address");
                btn_add.setText("Add");
                text.setHint("Enter Address");
                text_phone.setHint("Enter Phone");
            }
        }
    }
    private ActivityResultLauncher<Intent> mapActivityResultLauncher;
    public void getAddress(){
        mapActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri locationUri = result.getData().getData();
                        // Handle the selected location here
                        handleSelectedLocation(locationUri);
                    }
                });

        addAddress.setOnClickListener(v -> {
            double latitude = 48.8584; // Example latitude
            double longitude = 2.2945; // Example longitude
            openGoogleMapsForLocation(latitude, longitude);
        });
    }
    private void openGoogleMapsForLocation(double latitude, double longitude) {
        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

            mapActivityResultLauncher.launch(intent);

    }


    private void handleSelectedLocation(@Nullable Uri locationUri) {
        if (locationUri != null) {
            // Extract the latitude and longitude from the Uri
            String location = locationUri.getLastPathSegment();
            String[] latLng = location.split(",");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);

            // Do something with the location coordinates
            // For example, display them in a Toast
            Toast.makeText(this, "Selected location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(int position) {

        // Go back to the previous activity
        finish();
    }
    private TextView text1;
    public void onMenuItemClick(View view) {
        text1 = findViewById(R.id.languageText);
        String language = text1.getText()+"";
        if(view.getId() == R.id.btn_change){
            SharedPreferences sga = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor edit =  sga.edit();

            if(language.contains("VN")){
                edit.putString("language","en");
                text1.setText("ENG");
            }else {
                edit.putString("language","vn");
                text1.setText("VN");
            }
            edit.apply();
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
}