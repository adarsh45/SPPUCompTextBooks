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
        Button btnRegister = findViewById(R.id.btn_start);
        Button btnCancel = findViewById(R.id.btn_cancel);

        btnRegister.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

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
        String userId = currentUser.getUid();
        int paymentGiven = 0;
        
        
        rootRef.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Log.d(TAG, "onDataChange: "+ snapshot.toString());
                    Log.d(TAG, "onDataChange: EXISTING USER");
                } else {
                    Log.d(TAG, "onDataChange: NEW USER");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
        
        studentData = new StudentData(userId, name, msTeamsId, phoneNumber, 0, true);

        rootRef.child(userId).setValue(studentData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
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
