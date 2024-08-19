package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.Checked_adapter;
import fascon.vovinam.vn.Model.AttendanceModel;
import fascon.vovinam.vn.Model.ClassModel;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.CheckinApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_member_checkin extends BaseActivity {
    private Checked_adapter checkedAdapter;
    private BlankFragment loadingFragment;
    private String timeClass;
    private List<AttendanceModel.Attendance> attendanceModelAll = new ArrayList<>();


    private int[] days;
    private TextView txtClassName;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_member_checkin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtClassName = findViewById(R.id.txtClassName);
        TextView txtSoNgayHienDien = findViewById(R.id.txtCheckin);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Xem tình trạng điểm danh");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "View attendance check in");
                myContentE.apply();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);


        showLoading();

        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();

        apiService.memberViewCheckin("Bearer "+token, "2024-07-20", formattedDate).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();

                    JsonObject dataObject = jsonObject.getAsJsonObject("data");

                    txtClassName.setText(dataObject.get("class_name").getAsString());

                    String dateJoin = dataObject.get("begin_date").getAsString();

                    Type AttendanceModelListType = new TypeToken<List<AttendanceModel.Attendance>>() {}.getType();
                    List<AttendanceModel.Attendance> attendanceModels = gson.fromJson(dataObject.get("attendance"), AttendanceModelListType);

                    //Fecth all day
                    apiService.memberViewClass("Bearer "+token).enqueue(new Callback<ClassModel[]>() {
                        @Override
                        public void onResponse(Call<ClassModel[]> call, Response<ClassModel[]> response) {
                            if(response.isSuccessful()){
                                ClassModel[] classModels = response.body();
                                for (ClassModel classModel : classModels){
                                    timeClass = classModel.getThoigian();
                                    String[] dates = parseSchedule(timeClass);
                                    days = new int[dates.length];
                                    for (int i = 0; i < dates.length; i++) {
                                        days[i] = Integer.parseInt(dates[i]);
                                    }

                                    ArrayList<String> dayToCheck = showAllDayCheckin(transDateToCalendar(transDate(dateJoin)), transDateToCalendar(transDate(formattedDate)), days);
                                    for(String string : dayToCheck){
                                        AttendanceModel.Attendance attendance = new AttendanceModel.Attendance();
                                        attendance.setHienDien("Vắng");
                                        attendance.setDate(string);
                                        attendance.setIn("00:00");
                                        attendance.setOut("00:00");
                                        attendance.setDay_of_week("T2");
                                        attendanceModelAll.add(attendance);
                                        System.out.println("Get some date"+string);
                                    }

                                    for(AttendanceModel.Attendance a : attendanceModelAll){
                                        for(AttendanceModel.Attendance b : attendanceModels){
                                            if(b.getDate().equals(a.getDate())){
                                                a.setIn(b.getIn());
                                            }
                                        }
                                    }

//                                    attendanceModels.addAll(attendanceModelAll);
                                    attendanceModelAll.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                                    checkedAdapter = new Checked_adapter(attendanceModelAll, getApplicationContext());
                                    RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recyclerView.setAdapter(checkedAdapter);


                                    txtSoNgayHienDien.setText(checkedAdapter.getItemCount() + " Ngày");
                                    hideLoading();
                                }
                            }else {
                                hideLoading();
                                Toast.makeText(activity_member_checkin.this, "Lấy dữ liệu thất bại, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ClassModel[]> call, Throwable throwable) {
                            hideLoading();
                            System.out.println("Active: Call Onfail");
                            Log.e("PostData", "Failure: " + throwable.getMessage());
                            Toast.makeText(activity_member_checkin.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
                    hideLoading();
                }else {
                    hideLoading();
                    Toast.makeText(activity_member_checkin.this, "Bạn chưa đăng ký lớp học, vui lòng đăng ký và quay lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                Toast.makeText(activity_member_checkin.this, "Lỗi kết nối mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
        textView20 = findViewById(R.id.textView9);
        textView9 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView7);

        if(languageS!= null){
            if(languageS.contains("en")){

                textView20.setText("Day");
                textView9.setText("Name");
                textView2.setText("Time check in");
            }
        }

    }
    private TextView textView20;
    private TextView textView9;
    private TextView textView2;

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

    //Các chức năng hiển thị ngày vắng

    private ArrayList<String> getDesiredDays(Calendar startDate, Calendar endDate, int[] desiredDays) {
        ArrayList<String> resultDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        while (startDate.before(endDate) || startDate.equals(endDate)) {
            int dayOfWeek = startDate.get(Calendar.DAY_OF_WEEK);
            for (int desiredDay : desiredDays) {
                if (dayOfWeek == desiredDay) {
                    resultDates.add(sdf.format(startDate.getTime()));
                    break;
                }
            }
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        return resultDates;
    }

    private ArrayList<String> showAllDayCheckin(Calendar startDate, Calendar endDate, int[] desiredDays){

        ArrayList<String> resultDates = getDesiredDays(startDate, endDate, desiredDays);
        return resultDates;
    }

    private String[] parseSchedule(String input) {
        // Tách chuỗi theo dấu ": "
        String[] parts = input.split(": ");
        String daysString = parts[0].split(" ")[1];
        String[] days = daysString.split("-");
        String timeString = parts[1];
        String startTime = timeString.split("-")[0];
        String endTime = timeString.split("-")[1];

        return days;
    }

    private int[] transDate(String dateString){
        String[] parts = dateString.split("-");
        int[] dateArray = new int[3];
        dateArray[0] = Integer.parseInt(parts[0]);
        dateArray[1] = Integer.parseInt(parts[1]);
        dateArray[2] = Integer.parseInt(parts[2]);
        return dateArray;
    }

    private Calendar transDateToCalendar(int[] date){
        Calendar startDate = Calendar.getInstance();
        if(date[1] == 1){
            startDate.set(date[0], Calendar.JANUARY, date[2]);
        }
        if(date[1] == 2){
            startDate.set(date[0], Calendar.FEBRUARY, date[2]);
        }
        if(date[1] == 3){
            startDate.set(date[0], Calendar.MARCH, date[2]);
        }
        if(date[1] == 4){
            startDate.set(date[0], Calendar.APRIL, date[2]);
        }
        if(date[1] == 5){
            startDate.set(date[0], Calendar.MAY, date[2]);
        }
        if(date[1] == 6){
            startDate.set(date[0], Calendar.JUNE, date[2]);
        }
        if(date[1] == 7){
            startDate.set(date[0], Calendar.JULY, date[2]);
        }
        if(date[1] == 8){
            startDate.set(date[0], Calendar.AUGUST, date[2]);
        }
        if(date[1] == 9){
            startDate.set(date[0], Calendar.SEPTEMBER, date[2]);
        }
        if(date[1] == 10){
            startDate.set(date[0], Calendar.OCTOBER, date[2]);
        }
        if(date[1] == 11){
            startDate.set(date[0], Calendar.NOVEMBER, date[2]);
        }
        if(date[1] == 12){
            startDate.set(date[0], Calendar.DECEMBER, date[2]);
        }

        return startDate;
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
}