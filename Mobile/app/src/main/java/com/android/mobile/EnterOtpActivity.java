package com.android.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EnterOtpActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;

    // Trong phương thức onCreate của EnterOtpActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        EdgeToEdge.enable(this);

        // Initialize EditText fields
        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        // Set listeners to move focus to the next EditText
        setupEditTextListeners();

        // Find and set onClickListener for Verify button
        findViewById(R.id.btnVerify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onVerifyButtonClick(v);
            }
        });
    }


    private void setupEditTextListeners() {
        inputCode1.addTextChangedListener(new FocusTextWatcher(inputCode1, inputCode2));
        inputCode2.addTextChangedListener(new FocusTextWatcher(inputCode2, inputCode3));
        inputCode3.addTextChangedListener(new FocusTextWatcher(inputCode3, inputCode4));
        inputCode4.addTextChangedListener(new FocusTextWatcher(inputCode4, inputCode5));
        inputCode5.addTextChangedListener(new FocusTextWatcher(inputCode5, inputCode6));
        inputCode6.addTextChangedListener(new FocusTextWatcher(inputCode6, null)); // Last EditText, no next EditText
    }

    // Example of a TextWatcher to move focus to the next EditText
    private static class FocusTextWatcher implements TextWatcher {
        private final EditText currentEditText;
        private final EditText nextEditText;

        FocusTextWatcher(EditText currentEditText, EditText nextEditText) {
            this.currentEditText = currentEditText;
            this.nextEditText = nextEditText;
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextEditText != null) {
                nextEditText.requestFocus();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    // Method to handle verification success and navigation
    public void onVerifyButtonClick(View view) {
        // Perform verification logic here (replace with your actual verification process)
        boolean verified = verifyOTP();

        if (verified) {
            // Show success message using Toast
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

            // Navigate to MenuActivity
            startActivity(new Intent(this, MenuActivity.class));
            finish(); // Finish this activity after navigation
        } else {
            // Handle verification failure
            // For example, show error message or retry OTP entry
        }
    }

    // Replace this method with your actual OTP verification logic
    private boolean verifyOTP() {
        // Example: Simulate successful verification
        // Replace with your actual OTP verification logic (e.g., compare entered OTP with sent OTP)
        return true; // Return true for demonstration
    }
}
