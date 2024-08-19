package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import fascon.vovinam.vn.ViewModel.BaseActivity;
import fascon.vovinam.vn.ViewModel.Checked_Teacher_adapter;
import fascon.vovinam.vn.Model.ApiResponse;
import fascon.vovinam.vn.Model.AttendanceTeacher;
import fascon.vovinam.vn.Model.ClassData;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.CheckinApiService;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_teacher_checkin extends BaseActivity {
    private static final String TAG = "MainActivity";
    private List<AttendanceTeacher> listChecked = new ArrayList<>();
    private BlankFragment loadingFragment;
    private EditText editTextDateStart;
    private EditText editTextDateEnd;
    private TextView txtClassName;
    private Button btnFilter, btnExport;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String languageS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_checkin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);
        // Lấy ngày 1 tháng trước
        LocalDate day1MonthBefore = getFirstDayOfPreviousMonth(currentDate);
        String beforeFormattedDate = day1MonthBefore.format(formatter);
        System.out.println("Day before: "+ beforeFormattedDate);



        txtClassName = findViewById(R.id.txtClassName);

        Intent intent = getIntent();
        int idClass = intent.getIntExtra("id", -1);

        // Lưu tên trang vào SharedPreferences
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Giao diện lịch sử điểm danh");
        myContentE.apply();
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Attendance history interface");
                myContentE.apply();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

// Thêm hoặc thay thế Fragment mới
        titleFragment newFragment = new titleFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.fragment_container, newFragment);
//        fragmentTransaction.addToBackStack(null); // Để có thể quay lại Fragment trước đó
        fragmentTransaction.commit();

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("access_token", null);

        ImageButton btnShowFilter = findViewById(R.id.btnShowFilter);
        btnShowFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View filterDialogView = LayoutInflater.from(activity_teacher_checkin.this).inflate(R.layout.bottom_sheet_dialog_filter_checkin, null);
                BottomSheetDialog filterDialog = new BottomSheetDialog(activity_teacher_checkin.this);
                filterDialog.setContentView(filterDialogView);
                filterDialog.setCanceledOnTouchOutside(true);
                filterDialog.setDismissWithAnimation(true);

                Button btnFilterByName = filterDialog.findViewById(R.id.btnFilterNameMember);
                Button btnFilterById = filterDialog.findViewById(R.id.btnFilterIdMember);
                Button btnFilterBySDT = filterDialog.findViewById(R.id.btnFilterSDTMember);

                EditText editFilterByName = filterDialog.findViewById(R.id.editTextNameMember);
                EditText editFilterById = filterDialog.findViewById(R.id.editTextIdMember);
                EditText editFilterBySDT = filterDialog.findViewById(R.id.editTextSDTMember);

                btnFilterByName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nameMember = editFilterByName.getText().toString();
                        if(!nameMember.isEmpty()){
                            filterCheckedByName("Bearer "+token, beforeFormattedDate, formattedDate, idClass, nameMember);
                        }else{
                            Toast.makeText(activity_teacher_checkin.this, "Nhập tên học viên để tiếp tục", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnFilterById.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!editFilterById.getText().toString().isEmpty()){
                            if(editFilterById.getText().toString().length() < 9){
                                int idMember = Integer.parseInt(editFilterById.getText().toString());
                                filterCheckedById("Bearer "+token, beforeFormattedDate, formattedDate, idClass, idMember);
                            }else{
                                Toast.makeText(activity_teacher_checkin.this, "Nhập số ký tự quá dài, mời nhập lại", Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            Toast.makeText(activity_teacher_checkin.this, "Nhập mã số học viên để tiếp tục", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnFilterBySDT.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String SDTMember = editFilterBySDT.getText().toString();
                        System.out.println("SDT: "+SDTMember);
                        if(!SDTMember.isEmpty()){
                            filterCheckedBySDT("Bearer "+token, beforeFormattedDate, formattedDate, idClass, SDTMember);
                        }else{
                            Toast.makeText(activity_teacher_checkin.this, "Nhập SĐT học viên để tiếp tục", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                filterDialog.show();
            }
        });

        editTextDateEnd = findViewById(R.id.editTextDate3);
        editTextDateStart = findViewById(R.id.editTextDate2);


        editTextDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogStart();
            }
        });

        editTextDateStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!isValidDate(input)) {
                    editTextDateStart.setError("Ngày nhập vào ko hợp lệ. Use yyyy-MM-dd");
                }
            }

            private boolean isValidDate(String date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setLenient(false);
                try {
                    sdf.parse(date);
                    return true;
                } catch (ParseException e) {
                    return false;
                }
            }
        });

        editTextDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialogEnd();
            }
        });

        editTextDateEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!isValidDate(input)) {
                    editTextDateEnd.setError("Ngày nhập vào ko hợp lệ. Use yyyy-MM-dd");
                }
            }

            private boolean isValidDate(String date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setLenient(false);
                try {
                    sdf.parse(date);
                    return true;
                } catch (ParseException e) {
                    return false;
                }
            }
        });

        btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = editTextDateStart.getText().toString();
                String endDate = editTextDateEnd.getText().toString();
                filterCheckedByDate("Bearer "+token, startDate, endDate, idClass);
            }
        });

        btnExport = findViewById(R.id.btnExport);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    exportExcel(listChecked);
                } else {
                    requestPermission();
                }
            }
        });


        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckin("Bearer "+token, beforeFormattedDate, formattedDate, idClass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }
                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                                listChecked.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
        textView20 = findViewById(R.id.textView20);
        textView9 = findViewById(R.id.textView9);
        textView2 = findViewById(R.id.textView2);
        if(languageS!= null){
            if(languageS.contains("en")){
                editTextDateEnd.setHint("Enter date end");
                editTextDateStart.setHint("Enter date start");
                textView20.setText("Day");
                textView9.setText("Name");
                textView2.setText("Time check in");
                btnFilter.setText("Filter");
                btnExport.setText("Export excel");
            }
        }
    }
    private TextView textView20;
    private TextView textView9;
    private TextView textView2;

    private void filterCheckedByDate(String token ,String startDate, String endDate, int idClass){
        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckin("Bearer "+token, startDate, endDate, idClass).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    //Xóa list đang tồn tại
                    listChecked.clear();
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }

                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                                listChecked.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

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
    private void showDatePickerDialogStart() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog và thiết lập sự kiện khi người dùng chọn ngày
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity_teacher_checkin.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tạo một đối tượng Calendar và thiết lập ngày được chọn
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Định dạng ngày theo yyyy-MM-dd
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        editTextDateStart.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showDatePickerDialogEnd() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog và thiết lập sự kiện khi người dùng chọn ngày
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity_teacher_checkin.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Tạo một đối tượng Calendar và thiết lập ngày được chọn
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(Calendar.YEAR, year);
                        selectedDate.set(Calendar.MONTH, month);
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Định dạng ngày theo yyyy-MM-dd
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = dateFormat.format(selectedDate.getTime());

                        editTextDateEnd.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportExcel(listChecked);
            } else {
                Toast.makeText(this, "Quyền bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void exportExcel(List<AttendanceTeacher> listChecked) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Demo Sheet");

        // Tạo một dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        Cell headerCell1 = headerRow.createCell(0);
        headerCell1.setCellValue("Ngày");
        Cell headerCell2 = headerRow.createCell(1);
        headerCell2.setCellValue("Tên");
        Cell headerCell3 = headerRow.createCell(2);
        headerCell3.setCellValue("Điểm danh lúc");

        // Thêm dữ liệu vào dòng tiếp theo
//        Row dataRow = sheet.createRow(1);
//        Cell dataCell1 = dataRow.createCell(0);
//        dataCell1.setCellValue("John Doe");
//        Cell dataCell2 = dataRow.createCell(1);
//        dataCell2.setCellValue(29);
        for (int i = 0; i < listChecked.size(); i++) {
            Row dataRow = sheet.createRow(i+1);
            Cell dataCell1 = dataRow.createCell(0);
            dataCell1.setCellValue(listChecked.get(i).getDate() +" "+listChecked.get(i).getDay_of_week());
            Cell dataCell2 = dataRow.createCell(1);
            dataCell2.setCellValue(listChecked.get(i).getMember_name());
            Cell dataCell3 = dataRow.createCell(2);
            dataCell3.setCellValue(listChecked.get(i).getIn());
        }

        // Lưu file Excel vào bộ nhớ ngoài
        String fileName = "Checkin_excel.xlsx";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
            workbook.close();
            Toast.makeText(this, "File Excel đã được lưu tại: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi lưu file Excel", Toast.LENGTH_LONG).show();
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

    private void filterCheckedByName(String token ,String startDate, String endDate, int idClass, String name){
        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckinByName("Bearer "+ token, startDate, endDate, idClass, name).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    //Xóa list đang tồn tại
                    listChecked.clear();
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }

                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                                listChecked.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void filterCheckedById(String token ,String startDate, String endDate, int idClass, int idMember){
        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckinById("Bearer "+ token, startDate, endDate, idClass, idMember).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    //Xóa list đang tồn tại
                    listChecked.clear();
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }

                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                                listChecked.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    private void filterCheckedBySDT(String token ,String startDate, String endDate, int idClass, String SDT){
        showLoading();
        CheckinApiService apiService = ApiServiceProvider.getCheckinApiService();
        apiService.teacherViewCheckinByPhone("Bearer "+ token, startDate, endDate, idClass, SDT).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    //Xóa list đang tồn tại
                    listChecked.clear();
                    JsonObject jsonObject = response.body();
                    if(jsonObject!=null){
                        Gson gson = new Gson();
                        Type apiResponseType = new TypeToken<ApiResponse>(){}.getType();
                        ApiResponse apiResponse = gson.fromJson(jsonObject, apiResponseType);

                        if (apiResponse != null && "Thành công".equals(apiResponse.getSuccess())) {
                            List<String> stringList = new ArrayList<>();
                            List<ClassData> classDataList = apiResponse.getData();
                            for (ClassData classData : classDataList) {
                                //Tên lớp
                                txtClassName.setText(classData.getClassName());

                                for (Map.Entry<String, List<AttendanceTeacher>> entry : classData.getAttendance().entrySet()) {

                                    String date = entry.getKey();
                                    stringList.add(date);
                                    List<AttendanceTeacher> attendanceList = entry.getValue();
                                    for (AttendanceTeacher attendance : attendanceList) {
                                        attendance.setDate(date);
                                        listChecked.add(attendance);
                                    }

                                }
                                for (String s : stringList){
                                    System.out.println("Date: "+ s);
                                }
                                listChecked.sort((event1, event2) -> event2.getDate().compareTo(event1.getDate()));
                            }
                            Checked_Teacher_adapter checkedTeacherAdapter = new Checked_Teacher_adapter(getApplicationContext(), listChecked);
                            RecyclerView recyclerView = findViewById(R.id.recycler_checkin);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(checkedTeacherAdapter);

                            hideLoading();
                        }
                    }


                }else {
                    System.out.println("Active: Call onResponse");
                    Log.e("PostData", "Error: " + response.message());

                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                hideLoading();
                System.out.println("Active: Call Onfail");
                Log.e("PostData", "Failure: " + throwable.getMessage());
            }
        });
    }

    public static LocalDate getFirstDayOfPreviousMonth(LocalDate currentDate) {
        // Lấy ngày đầu tiên của tháng hiện tại
        LocalDate firstDayOfCurrentMonth = currentDate.withDayOfMonth(1);

        // Trừ đi 1 tháng từ ngày đầu tiên của tháng hiện tại
        LocalDate firstDayOfPreviousMonth = firstDayOfCurrentMonth.minusMonths(1);

        return firstDayOfPreviousMonth;
    }
}