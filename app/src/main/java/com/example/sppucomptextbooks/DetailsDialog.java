package com.example.sppucomptextbooks;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.sppucomptextbooks.pojo.StudentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DetailsDialog extends Dialog implements View.OnClickListener {

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase myDB;
    DatabaseReference rootRef;
    StudentData studentData;

    private Context context;
    private String phoneNumber;
    private EditText etName, etMsTeamsId;
    private LinearLayout linearLayoutBtnDialog;

    private int paymentGiven = 0;

    private static final String TAG = "LOG DetailsDialog:";

    public DetailsDialog(@NonNull Context context, String phoneNumber) {
        super(context);

        this.context = context;
        this.phoneNumber = phoneNumber;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myDB = FirebaseDatabase.getInstance();
        rootRef = myDB.getReference("Users");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_dialog);

        this.setCancelable(false);

        etName = findViewById(R.id.et_name);
        etMsTeamsId = findViewById(R.id.et_ms_teams_id);
        linearLayoutBtnDialog = findViewById(R.id.ll_button_dialog);
        Button btnRegister = findViewById(R.id.btn_start);
        Button btnCancel = findViewById(R.id.btn_cancel);

        checkRegisteredUser();

        btnRegister.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    private void checkRegisteredUser(){
//        check for any auth issues
        if (currentUser== null){
            Log.d(TAG, "checkRegisteredUser: currentUser is NULL");
            Toast.makeText(context, "Error logging in, Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }

        rootRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d(TAG, "onDataChange: USER ALREADY EXISTS");
                    studentData = snapshot.getValue(StudentData.class);
//                    set texts on edit texts
                    assert studentData != null;
                    etName.setText(studentData.getName());
                    etMsTeamsId.setText(studentData.getMsTeamsId());
                    paymentGiven = studentData.getPaymentGiven();
                } else {
                    Log.d(TAG, "onDataChange: USER IS NEW");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ERROR: "+ error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start){
            registerUser();
        } else {
            mAuth.signOut();
            dismiss();
        }
    }

    private void registerUser() {
        if (TextUtils.isEmpty(etName.getText())){
            etName.setError("Name should Not be empty!");
            return;
        }
        if (TextUtils.isEmpty(etMsTeamsId.getText())){
            etMsTeamsId.setError("MS Teams ID should not be empty!");
            return;
        }
        String name = etName.getText().toString();
        String msTeamsId = etMsTeamsId.getText().toString();

//        update values from edit texts if changed
        studentData = new StudentData(currentUser.getUid(), name,msTeamsId,phoneNumber, paymentGiven,true);

        updateDatabase(studentData);

    }

    private void updateDatabase(StudentData data){

        rootRef.child(data.getUid()).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(context, FoldersScreen.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });
    }
}
