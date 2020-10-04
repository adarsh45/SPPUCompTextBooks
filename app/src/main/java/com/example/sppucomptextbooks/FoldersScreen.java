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

public class FoldersScreen extends AppCompatActivity {

    // Folders Screen - Buttons to move to Specific Subject

    TextView textSubject, textFDS, textDM, textOOP, textDELD, textCG;

    Button fdsBtn1, dmBtn1, oopBtn1, deldBtn1, cgBtn1;

//TODO : PDF test to be done with intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);


        FirebaseAuth.getInstance();

        textSubject = findViewById(R.id.textSubj);
        textFDS = findViewById(R.id.textFDS);
        textDM = findViewById(R.id.textDM);
        textDELD = findViewById(R.id.textDELD);
        textOOP = findViewById(R.id.textOOP);
        textCG = findViewById(R.id.textCG);

        fdsBtn1 = findViewById(R.id.fds_pdf1_btn);
        dmBtn1 = findViewById(R.id.dm_pdf1_btn);
        oopBtn1 = findViewById(R.id.oop_pdf1_btn);
        deldBtn1 = findViewById(R.id.deld_pdf1_btn);
        cgBtn1 = findViewById(R.id.cg_pdf1_btn);

        fdsBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
                intent.putExtra("pdfName", "syllabus.pdf");
                startActivity(intent);
                Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();

            }
        });


        dmBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
                intent.putExtra("pdfName", "syllabus.pdf");
                startActivity(intent);
                Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        oopBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
                intent.putExtra("pdfName", "syllabus.pdf");
                startActivity(intent);
                Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        cgBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
                intent.putExtra("pdfName", "syllabus.pdf");
                startActivity(intent);
                Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        deldBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoldersScreen.this, PDFViewer.class);
                intent.putExtra("pdfName", "syllabus.pdf");
                startActivity(intent);
                Toast.makeText(FoldersScreen.this, "Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
