package com.example.sppucomptextbooks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class OOPScreen extends AppCompatActivity {

    Button downloadBtn;

    StorageReference mStorageRef;
    StorageReference storageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oop);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        downloadBtn = findViewById(R.id.downloadBtn);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });

    }

    public void download(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storageRef = mStorageRef.child("syllabus.pdf");

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                Intent intent = new Intent();
                intent.setDataAndType(Uri.parse(url), "application/pdf");
                Intent chooserIntent = Intent.createChooser(intent, "Open Report");
                startActivity(chooserIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

//    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url){
//
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName+fileExtension);
//        downloadManager.enqueue(request);
//
//    }


    @Override
    public void onBackPressed() {
//        finish();
        Intent a = new Intent(OOPScreen.this, FoldersScreen.class);
        startActivity(a);
    }

}
