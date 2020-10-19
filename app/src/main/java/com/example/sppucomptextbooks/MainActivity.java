package com.example.sppucomptextbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sppucomptextbooks.pojo.StudentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // SignIn Screen :
    private ConstraintLayout layout;
//    private ProgressBar progressBar;
    private EditText etPhone , etOtp;
    private Button btnSendOTP, btnVerify;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference rootRef;

    private String phone,  codeSent;
    private boolean isAlreadyLoggedIn = false;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(MainActivity.this, FoldersScreen.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference("Users");

        layout = findViewById(R.id.signup_root_layout);
        etPhone = findViewById(R.id.et_phone);
        etOtp = findViewById(R.id.et_otp);
        btnSendOTP = findViewById(R.id.btn_send_otp);
        btnVerify = findViewById(R.id.btn_verify);

        btnVerify.setOnClickListener(this);
        btnSendOTP.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
//        sign-out user if back btn pressed
        mAuth.signOut();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send_otp:
                getPhoneNumber();
                checkIfAlreadyLoggedIn();
                sendOtp(view);
                break;
            case R.id.btn_verify:
                verifyOtp(view);
                break;
            default:
                Toast.makeText(this, "This is not a button!", Toast.LENGTH_LONG).show();
        }
    }

    private void getPhoneNumber() {
        if (TextUtils.isEmpty(etPhone.getText())){
            etPhone.setError("Please enter phone number!");
            return;
        }
        phone = etPhone.getText().toString();
        if (phone.length()<10) {
            etPhone.setError("Enter valid phone number!");
            return;
        }
        phone = "+91"+phone;
    }

    private boolean checkIfAlreadyLoggedIn() {
        Query selectUserByPhoneNumber = rootRef.orderByChild("phoneNumber").equalTo(phone);
        selectUserByPhoneNumber.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    isAlreadyLoggedIn = false;
                    return;
                }
                for (DataSnapshot snap: snapshot.getChildren()){
                    StudentData studentData = snap.getValue(StudentData.class);
                    assert studentData != null;
                    isAlreadyLoggedIn = studentData.isAlreadyLoggedIn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return isAlreadyLoggedIn;
    }

    public void sendOtp(View view) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback
        );

    }

    public void verifyOtp(View view) {
        //        otp entered and button clicked for verification
        if (TextUtils.isEmpty(etOtp.getText())){
            etOtp.setError("Please enter OTP!");
            return;
        }
        String codeEntered = etOtp.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeEntered);

        signInWithCredentials(credential);

    }

//    CALLBACK method
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//            verification completed without sending OTP
//            direct sign in
            signInWithCredentials(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
//            verification of number failed
            Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    @Override
    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
        super.onCodeSent(s, forceResendingToken);
        codeSent = s;
        Toast.makeText(MainActivity.this, "Code sent successfully", Toast.LENGTH_SHORT).show();
    }
};

//    SIGN IN method
    private void signInWithCredentials(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (isAlreadyLoggedIn){
//                                user account is already present
                                Toast.makeText(MainActivity.this, "You are already logged in different device!\n" +
                                        "Mistake? Please contact developers!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            } else {
                                Toast.makeText(MainActivity.this, "Sign in success!", Toast.LENGTH_SHORT).show();
                                DetailsDialog detailsDialog = new DetailsDialog(MainActivity.this, phone);
                                detailsDialog.show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}