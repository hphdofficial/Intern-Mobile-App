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

import java.util.Calendar;

public class RegisterClass extends BaseActivity {
    private EditText editTextDateOfBirth;

    private TextView money;
    private Button buttonRegister;
    private RadioGroup date_learn;
    private TextView name_class;
    private  RadioGroup note;
    private String noteText = "";
    private String classId = null;
    private BlankFragment loadingFragment;
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
        // giả sử class được chọn là 1

    }


    public void eventClickRadio(){



        date_learn.clearCheck();
        date_learn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Xử lý khi một RadioButton được chọn
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();

               if(selectedText.contains("3")){
                   money.setText("1.000.000đ");
               }
                if(selectedText.contains("6")){
                    money.setText("1.950.000đ");
                }
                if(selectedText.contains("12")){
                    money.setText("3.800.000đ");
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