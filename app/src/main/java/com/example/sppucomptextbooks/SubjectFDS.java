package com.example.sppucomptextbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SubjectFDS extends AppCompatActivity {

    private TextView subjName;
    private Button chap1Btn, chap2Btn, chap3Btn, chap4Btn, chap5Btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        subjName = findViewById(R.id.textSubj);
        subjName.setText(R.string.subject_fds);

        chap1Btn = findViewById(R.id.chap1_btn);
        chap1Btn.setText(R.string.fds_chap1);

        chap2Btn = findViewById(R.id.chap2_btn);
        chap2Btn.setText(R.string.fds_chap2);

        chap3Btn = findViewById(R.id.chap3_btn);
        chap3Btn.setText(R.string.fds_chap3);

        chap4Btn = findViewById(R.id.chap4_btn);
        chap4Btn.setText(R.string.fds_chap4);

        chap5Btn = findViewById(R.id.chap5_btn);
        chap5Btn.setText(R.string.fds_chap5);

    }

//     PDF buttons
    public void openPdf(View view) {

        Intent intent = new Intent(SubjectFDS.this, PDFViewer.class);
        switch (view.getId()){
            case R.id.chap1_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;

            case R.id.chap2_btn:
                intent.putExtra("pdfName", "dm_chap2.pdf");
                break;
            case R.id.chap3_btn:
                intent.putExtra("pdfName", "syllabus.pdf");

                break;
            case R.id.chap4_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;
            case R.id.chap5_btn:
                intent.putExtra("pdfName", "syllabus.pdf");
                break;
        }
        startActivity(intent);
        Toast.makeText(SubjectFDS.this, "Please Wait", Toast.LENGTH_SHORT).show();
    }
}
