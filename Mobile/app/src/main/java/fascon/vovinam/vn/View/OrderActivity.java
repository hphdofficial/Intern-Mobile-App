package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderActivity extends BaseActivity {
    private String languageS;
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
        setContentView(R.layout.activity_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Lịch sử mua hàng");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Purchase History ");
                myContentE.apply();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        OrderPagerAdapter adapter = new OrderPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Chờ xác nhận");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                tab.setText("Wait confirmation");
                            }
                        }
                        break;
                    case 1:
                        tab.setText("Chờ lấy hàng");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                tab.setText("Waiting delivery");
                            }
                        }
                        break;
                    case 2:
                        tab.setText("Đang giao hàng");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                tab.setText("Delivering");
                            }
                        }
                        break;
                    case 3:
                        tab.setText("Đã giao");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                tab.setText("Delivered");
                            }
                        }
                        break;
                    case 4:
                        tab.setText("Đã hủy");
                        if(languageS!= null){
                            if(languageS.contains("en")){
                                tab.setText("Canceled");
                            }
                        }
                        break;
                }
            }
        }).attach();
    }

    public void switchToTab(int tabPosition) {
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setCurrentItem(tabPosition);
    }
}