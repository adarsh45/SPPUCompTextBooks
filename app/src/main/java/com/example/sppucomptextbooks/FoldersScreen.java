package com.example.sppucomptextbooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

public class FoldersScreen extends AppCompatActivity implements PaymentStatusListener {

    public static final String TAG = "Payment button";

    // Folders Screen - Buttons to move to Specific Subject


    private TextView textPayStatus;


//TODO : PDF test to be done with intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        textPayStatus = findViewById(R.id.text_pay_status);

        FirebaseAuth.getInstance();


    }

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
    }



}
