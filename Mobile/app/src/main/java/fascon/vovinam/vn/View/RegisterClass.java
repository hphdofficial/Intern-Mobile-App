package fascon.vovinam.vn.View;import fascon.vovinam.vn.R;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import fascon.vovinam.vn.ViewModel.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

public class RegisterClass extends BaseActivity {
    private EditText editTextDateOfBirth;

    private TextView money;
    private Button buttonRegister;
    private RadioGroup date_learn;
    private TextView name_class;
    private int fee;
    private  RadioGroup note;
    private String noteText = "";
    private String classId = null;
    private BlankFragment loadingFragment;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SharedPreferences shared = getSharedPreferences("login_prefs", MODE_PRIVATE);
        languageS = shared.getString("language",null);
        buttonRegister = findViewById(R.id.buttonRegister);
        name_class = findViewById(R.id.name_class);
        money = findViewById(R.id.total);
        note = findViewById(R.id.radioGroup);
        date_learn = findViewById(R.id.radio_group_gender);

        CheckBox checkBoxOption1 = findViewById(R.id.checkBoxOption1);
        CheckBox checkBoxOption3 = findViewById(R.id.checkBoxOption3);
        CheckBox checkBoxOption4 = findViewById(R.id.checkBoxOption4);
        CheckBox checkBoxOption2 = findViewById(R.id.checkBoxOption2);

        CheckBox[] checkbox = {checkBoxOption1,checkBoxOption2,checkBoxOption3,checkBoxOption4};


       // editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
       // eventDate();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),payment.class));
            }
        });
        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Đăng ký lớp học");
        myContentE.apply();
        if(languageS!= null){
            if(languageS.contains("en")){
                myContentE.putString("title", "Register Class");
                myContentE.apply();
            }
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();


        if (extras != null) {
            // Lấy dữ liệu từ Bundle
            String name = extras.getString("name");
            String className = extras.getString("nameClass");
            classId = extras.getString("idClass");
            fee = extras.getInt("fee");
            name_class.setText(name+" - " + className);
        }


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gửi thông tin qua activity
                Intent itent = new Intent(getApplicationContext(), payment.class);
                itent.putExtra("title","registerclass");
                itent.putExtra("idClass",classId);
                if (extras != null) {
                    // Lấy dữ liệu từ Bundle
                    String name = extras.getString("name");
                    String className = extras.getString("nameClass");
                    itent.putExtra("teacherName",name);
                    itent.putExtra("className",className);

                }
                double total = Double.parseDouble(money.getText().toString().replace("đ","").replace(".",""));

                itent.putExtra("money",total);

                String s = "";


                noteText = "";
                for (CheckBox checkBox : checkbox) {
                    if(checkBox.isChecked()){
                            String selectedText = checkBox.getText().toString();
                            noteText = noteText + selectedText + "\n";
                    }
                }
                itent.putExtra("note",noteText);
                if(total >0 )
                startActivity(itent);
                else {
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn thời gian học",Toast.LENGTH_LONG).show();
                }
            }
        });

        eventClickRadio();
        textViewUsernameLabel = findViewById(R.id.textViewUsernameLabel);
        textViewTenLabel = findViewById(R.id.textViewTenLabel);
        select_health = findViewById(R.id.select_health);
        radio_button_3 = findViewById(R.id.radio_button_3);
        textViewDiachiLabel = findViewById(R.id.textViewDiachiLabel);
        radio_button_6 = findViewById(R.id.radio_button_6);
        radio_button_12 = findViewById(R.id.radio_button_12);
        if(languageS != null){
            if(languageS.contains("en")){

                textViewUsernameLabel.setText("Class Information");
                textViewTenLabel.setText("Time Learn");
                select_health.setText("Health Status");
                textViewDiachiLabel.setText("Sum Money Payment");
                buttonRegister.setText("Register");
                radio_button_3.setText("3 Months");
                radio_button_6.setText("6 Months");
                radio_button_12.setText("12 Months");
                for (CheckBox checkBox : checkbox) {
                   checkBox.setText(TranslateText(checkBox.getText().toString(),1));
                }
            }
        }
        // giả sử class được chọn là 1

    }
    public String TranslateText(String text, int k){
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.language);

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            String s = "";
            while ((line = reader.readLine()) != null) {
                if(line.contains(text)){
                    String temp[] = line.split(",");
                    if(k == 0){
                        s =  temp[0];
                    } else s= temp[1];
                    break;
                }

            }

            reader.close();
            return s;
            // Use the fileContent string
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private RadioButton radio_button_3;
    private RadioButton radio_button_6;
    private RadioButton radio_button_12;
    private TextView textViewUsernameLabel;
    private TextView textViewTenLabel;
    private TextView select_health;
    private TextView textViewDiachiLabel;


    public void eventClickRadio(){



        date_learn.clearCheck();
        date_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Xử lý khi một RadioButton được chọn
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();

                if (selectedText.contains("3")) {
                    money.setText(fee * 3 + " đ");
                }
                if (selectedText.contains("6")) {
                    money.setText(fee * 6 + " đ");
                }
                if (selectedText.contains("12")) {
                    money.setText(fee * 12 + " đ");
                }
            }
        });
    }
    public void eventDate(){
        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        // Lấy ngày tháng năm hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày tháng năm
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextDateOfBirth.setText(date);
                    }
                }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
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
}