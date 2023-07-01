package com.quocanproject.dragonchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;

public class LoginPhoneNumberActivity extends AppCompatActivity {

    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone_number);
        initUI();
        initPhoneNumber();
    }

    private void initPhoneNumber() {
        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        btnSendCode.setOnClickListener((v) ->{
            if(!countryCodePicker.isValidFullNumber()){
                phoneInput.setError("Phonenumber not valid");
                return;
            }
            Intent intent = new Intent(LoginPhoneNumberActivity.this, Enter_otp_login.class);
            intent.putExtra("phone", countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }

    private void initUI() {
        countryCodePicker = findViewById(R.id.countryCodePicker);
        phoneInput = findViewById(R.id.edtPhoneInput);
        btnSendCode = findViewById(R.id.btnSendOTP);
    }
}