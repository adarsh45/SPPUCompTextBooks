package com.example.sppucomptextbooks;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileOutputStream;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class PDFViewer extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

    }

    @Override
    public void onBackPressed() {
//        finish();
        Intent a = new Intent(PDFViewer.this, OOPScreen.class);
        startActivity(a);
    }
}