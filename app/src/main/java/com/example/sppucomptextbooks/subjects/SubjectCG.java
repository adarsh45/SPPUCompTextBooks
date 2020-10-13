package com.example.sppucomptextbooks.subjects;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sppucomptextbooks.PDFViewer;
import com.example.sppucomptextbooks.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class SubjectCG extends AppCompatActivity {
    private TextView subjName;
    private Button chap1Btn, chap2Btn, chap3Btn, chap4Btn, chap5Btn;

    StorageReference mStorageRef;
    StorageReference ref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        subjName = findViewById(R.id.textSubj);
        subjName.setText(R.string.subject_cg);

        chap1Btn = findViewById(R.id.chap1_btn);
        chap1Btn.setText(R.string.cg_chap1);

        chap2Btn = findViewById(R.id.chap2_btn);
        chap2Btn.setText(R.string.cg_chap2);

        chap3Btn = findViewById(R.id.chap3_btn);
        chap3Btn.setText(R.string.cg_chap3);

        chap4Btn = findViewById(R.id.chap4_btn);
        chap4Btn.setText(R.string.cg_chap4);

        chap5Btn = findViewById(R.id.chap5_btn);
        chap5Btn.setText(R.string.cg_chap5);

    }

    //     PDF buttons
    public void openPdf(View view) {
        String pdfName;
        Intent intent = new Intent(SubjectCG.this, PDFViewer.class);
        switch (view.getId()){
            case R.id.chap1_btn:
                pdfName = "crack.pdf";
                checkIfFileDownloaded(pdfName, intent);
                break;
            case R.id.chap2_btn:
                pdfName = "crack.pdf";
                checkIfFileDownloaded(pdfName, intent);
                break;
            case R.id.chap3_btn:
                pdfName = "crack.pdf";
                checkIfFileDownloaded(pdfName, intent);
                break;
            case R.id.chap4_btn:
                pdfName = "crack.pdf";
                checkIfFileDownloaded(pdfName, intent);
                break;
            case R.id.chap5_btn:
                pdfName = "crack.pdf";
                checkIfFileDownloaded(pdfName, intent);
                break;
        }

    }

    private void checkIfFileDownloaded(String pdfName, Intent intent){
        final File tempFile = new File(this.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), pdfName);
        if ( tempFile.exists() ) {
            intent.putExtra("pdfName", pdfName);
            startActivity(intent);
            Toast.makeText(SubjectCG.this, "Please Wait", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(SubjectCG.this,"Please Download the file to prevent data usage", Toast.LENGTH_SHORT).show();
        }
    }

    public void downloadFile(View view){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ref = mStorageRef.child("crack.pdf");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFileManager(SubjectCG.this, "crack", ".pdf", DIRECTORY_DOWNLOADS, url);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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

    }
}
