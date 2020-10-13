package com.example.sppucomptextbooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sppucomptextbooks.subjects.SubjectCG;
import com.example.sppucomptextbooks.subjects.SubjectDELD;
import com.example.sppucomptextbooks.subjects.SubjectDM;
import com.example.sppucomptextbooks.subjects.SubjectFDS;
import com.example.sppucomptextbooks.subjects.SubjectOOP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

public class FoldersScreen extends AppCompatActivity implements PaymentStatusListener {


    public static final String TAG = "Payment button";
    private TextView textPayStatus;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase myDB;
    DatabaseReference rootRef;
    String paymentGivenStatus;

    private LinearLayout linearPayLayout;
    private LinearLayout linearButtonLayout;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        textPayStatus = findViewById(R.id.text_pay_status);

        linearPayLayout = findViewById(R.id.linear_pay_layout);
        linearButtonLayout = findViewById(R.id.linear_btn_layout);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myDB = FirebaseDatabase.getInstance();
        rootRef = myDB.getReference("Users");

        Toast.makeText(this, "Please Wait Loading Data", Toast.LENGTH_SHORT).show();

        rootRef.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    if(data.getKey().equals("paymentGiven")){
                        String payStatus = data.getValue().toString();
                        Log.d("Payment Given Status : " , payStatus);
                        paymentGivenStatus = payStatus;
                        if (payStatus.equals("0")){
                            linearPayLayout.setVisibility(View.VISIBLE);
                            linearButtonLayout.setVisibility(View.GONE);
                        } else {
                            linearPayLayout.setVisibility(View.GONE);
                            linearButtonLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void openSubject(View view) {
        // Move to Subject Screen


        switch (view.getId()){
            case R.id.fds_btn:
                Intent intentFDS = new Intent(FoldersScreen.this, SubjectFDS.class);
                startActivity(intentFDS);
                break;
            case R.id.dm_btn:
                Intent intentDM = new Intent(FoldersScreen.this, SubjectDM.class);
                startActivity(intentDM);
                break;
            case R.id.oop_btn:
                Intent intentOOP = new Intent(FoldersScreen.this, SubjectOOP.class);
                startActivity(intentOOP);
                break;
            case R.id.cg_btn:
                Intent intentCG = new Intent(FoldersScreen.this, SubjectCG.class);
                startActivity(intentCG);
                break;
            case R.id.deld_btn:
                Intent intentDELD = new Intent(FoldersScreen.this, SubjectDELD.class);
                startActivity(intentDELD);
                break;

        }
    }

    public void payThroughUPI(View view){
        EasyUpiPayment.Builder builder = null;
        EasyUpiPayment easyUpiPayment = null;
        try {
            builder = new EasyUpiPayment.Builder(FoldersScreen.this)
                    .setPayeeVpa("8669297840@ybl")
                    .setPayeeName("Atharva Dhoot")
                    .setTransactionId("TCOMPSEB1406")
                    .setTransactionRefId("T123456789")
                    .setDescription("Test for UPI payment app")
                    .setAmount("5.00");
        } catch (IllegalStateException isExc){
            Log.d(TAG, "payThroughUPI: "+ isExc.getLocalizedMessage());
            Toast.makeText(this, isExc.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                easyUpiPayment = builder.build();
            } catch (AppNotFoundException e){
                Log.d(TAG, "payThroughUPI: "+ e.getLocalizedMessage());
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            } finally {
                easyUpiPayment.startPayment();
                easyUpiPayment.setPaymentStatusListener(this);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onTransactionCancelled() {
        textPayStatus.setText("Transaction cancelled!");
    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.d(TAG, "onTransactionCompleted: "+ transactionDetails.toString());
        textPayStatus.setText(transactionDetails.toString());

        // After Payment Update

        if (transactionDetails.getTransactionStatus().equals("SUCCESS")){
            rootRef.child("Users").child(currentUser.getUid()).child("paymentGiven").setValue(50).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(FoldersScreen.this, "Successful Payment Registered ... Restart app to access the folders", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(FoldersScreen.this, "Unsuccessful Payment Registered Contact the Developers", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}
