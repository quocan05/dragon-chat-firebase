package com.quocanproject.dragonchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quocanproject.dragonchat.utils.AndroidUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Enter_otp_login extends AppCompatActivity {

    String phoneNumber;
    Long timeoutSecs = 60L;

    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText edtOtpInput;
    Button btnOtp;
    TextView tvResendOtp;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp_login);
        phoneNumber = getIntent().getExtras().getString("phone");
        Toast.makeText(getApplicationContext(), phoneNumber, Toast.LENGTH_SHORT).show();

        initUI();
        
        sendOTP(phoneNumber, false);

        btnOtp.setOnClickListener(view -> {
            String otpEntered = edtOtpInput.getText().toString().trim();
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otpEntered);
           signMeIn(credential);
        });

        resendOTP();

    }

    private void resendOTP() {
        tvResendOtp.setOnClickListener(view -> {
            sendOTP(phoneNumber, true);
        });
    }

    private void sendOTP(String phoneNumber, boolean isResend) {
        setTimeResendOTP();
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSecs, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signMeIn(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AndroidUtil.showToast(getApplicationContext(), "OTP verify failed");
                                Log.e("Failedverify", e.toString());
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AndroidUtil.showToast(getApplicationContext(), "Success");
                            }
                        });

        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void setTimeResendOTP() {
        tvResendOtp.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSecs--;
                tvResendOtp.setText("Resend OTP in "+ timeoutSecs +"secs");
                if (timeoutSecs <= 0){
                    timeoutSecs = 60L;
                    timer.cancel();
                    runOnUiThread(()->{
                        tvResendOtp.setEnabled(true);
                        tvResendOtp.setText("Send OTP again");
                    });
                }
            }
        },0,1000);
    }

    private void signMeIn(PhoneAuthCredential phoneAuthCredential) {
        //login and next activity here
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(Enter_otp_login.this, EnterUsername.class);
                    intent.putExtra("phone", phoneNumber);
                    startActivity(intent);
                } else {
                    AndroidUtil.showToast(getApplicationContext(), "OTP verifi failed");
                }
            }
        });
    }

    private void initUI() {
        edtOtpInput = findViewById(R.id.edtEnterOTP);
        btnOtp = findViewById(R.id.btnOtp);
        tvResendOtp = findViewById(R.id.tvResendOtp);
    }
}