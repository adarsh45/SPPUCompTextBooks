package com.example.sppucomptextbooks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    private ScrollView scrollView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        textPayStatus = findViewById(R.id.text_pay_status);

        scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.linearLayout);

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
                            linearLayout.setVisibility(View.VISIBLE);
                            scrollView.setVisibility(View.GONE);
                        } else {
                            linearLayout.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // PDF buttons
    public void openPdf(View view) {

        Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
        switch (view.getId()){
            case R.id.fds_pdf1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;

            case R.id.dm_pdf1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;
            case R.id.deld_pdf1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");

                break;
            case R.id.oop_pdf1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;
            case R.id.cg_pdf1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;
        }
        startActivity(intent);
        Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();
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
        // After
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
