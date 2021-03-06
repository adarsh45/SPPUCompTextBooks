package com.example.sppucomptextbooks.subjects;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.sppucomptextbooks.PDFViewer;
import com.example.sppucomptextbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import www.sanju.motiontoast.MotionToast;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SubjectCG extends AppCompatActivity {
    private TextView subjName;
    private Button chap1Btn, chap2Btn, chap3Btn, chap4Btn, chap5Btn, chap6Btn;
    private ImageButton btn1, btn2, btn3, btn4, btn5, btn6;

    String pdfName;

    StorageReference mStorageRef;
    StorageReference ref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        //        set title and back button on action bar
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.subject_cg);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//        subjName = findViewById(R.id.textSubj);
//        subjName.setText(R.string.subject_cg);

        chap1Btn = findViewById(R.id.chap1_btn);
        chap1Btn.setText(R.string.cg_unit1);

        chap2Btn = findViewById(R.id.chap2_btn);
        chap2Btn.setText(R.string.cg_unit2);

        chap3Btn = findViewById(R.id.chap3_btn);
        chap3Btn.setText(R.string.cg_unit3);

        chap4Btn = findViewById(R.id.chap4_btn);
        chap4Btn.setText(R.string.cg_unit4);

        chap5Btn = findViewById(R.id.chap5_btn);
        chap5Btn.setText(R.string.cg_unit5);

        chap6Btn = findViewById(R.id.chap6_btn);
        chap6Btn.setText(R.string.cg_unit6);

        btn1 = findViewById(R.id.downloadBtn_1);
        btn2 = findViewById(R.id.downloadBtn_2);
        btn3 = findViewById(R.id.downloadBtn_3);
        btn4 = findViewById(R.id.downloadBtn_4);
        btn5 = findViewById(R.id.downloadBtn_5);
        btn6 = findViewById(R.id.downloadBtn_6);

    }

    //     PDF buttons
    public void openPdf(View view) {
        Intent intent = new Intent(SubjectCG.this, PDFViewer.class);
        switch (view.getId()){
            case R.id.chap1_btn:
                pdfName = "CG_UNIT_1_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn1);
                break;
            case R.id.chap2_btn:
                pdfName = "CG_UNIT_2_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn2);
                break;
            case R.id.chap3_btn:
                pdfName = "CG_UNIT_3_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn3);
                break;
            case R.id.chap4_btn:
                pdfName = "CG_UNIT_4_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn4);
                break;
            case R.id.chap5_btn:
                pdfName = "CG_UNIT_5_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn5);
                break;
            case R.id.chap6_btn:
                pdfName = "CG_UNIT_6_protected.pdf";
                checkIfFileDownloaded(pdfName, intent, btn6);
                break;
        }

    }

    private void checkIfFileDownloaded(String pdfName, Intent intent, ImageButton btnID){
        final File tempFile = new File(this.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), pdfName);
        if ( tempFile.exists() ) {
            intent.putExtra("pdfName", pdfName);
            startActivity(intent);
            btnID.setVisibility(View.GONE);
        }
        else{
            btnID.setVisibility(View.VISIBLE);
            MotionToast.Companion.darkColorToast(this, "Download the file first and then Tap Here again to open!",
                    MotionToast.TOAST_INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(this, R.font.helvetica_regular));
//            Toast.makeText(SubjectCG.this,"Please Download the file to prevent data usage", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadFile(View view){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mStorageRef.child("CG").child(pdfName);
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFileManager(SubjectCG.this, pdfName.replace(".pdf", ""), ".pdf", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SubjectCG.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFileManager(Context context, String fileName, String fileExtension, String destinationDirectory, String url){

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

    }

    BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            MotionToast.Companion.darkColorToast(SubjectCG.this,"The PDF can only be accessed through the APP",
                    MotionToast.TOAST_WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(SubjectCG.this,R.font.helvetica_regular));
        }
    };
}
