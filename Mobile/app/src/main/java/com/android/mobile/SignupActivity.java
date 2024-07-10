package com.android.mobile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextBirthday = findViewById(R.id.edit_text_birthday);
        editTextBirthday.setInputType(InputType.TYPE_NULL); // Disable keyboard input
        editTextBirthday.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    SignupActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> editTextBirthday.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay),
                    year, month, day);
            datePickerDialog.show();
        });

        // Handle password visibility toggle
        ImageButton togglePasswordVisibility = findViewById(R.id.image_button_toggle_password_visibility);
        ImageButton toggleConfirmPasswordVisibility = findViewById(R.id.image_button_toggle_confirm_password_visibility);
        EditText passwordEditText = findViewById(R.id.edit_text_password);
        EditText confirmPasswordEditText = findViewById(R.id.edit_text_confirm_password);

        togglePasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(passwordEditText, togglePasswordVisibility);
        });

        toggleConfirmPasswordVisibility.setOnClickListener(v -> {
            togglePasswordVisibility(confirmPasswordEditText, toggleConfirmPasswordVisibility);
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        if (editText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.drawable.view); // Change to your view icon
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.drawable.hide); // Change back to your hide icon
        }
        editText.setSelection(editText.getText().length());
    }
}
