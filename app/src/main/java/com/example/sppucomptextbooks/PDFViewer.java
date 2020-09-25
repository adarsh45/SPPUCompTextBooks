package com.example.sppucomptextbooks;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PDFViewer extends AppCompatActivity {

    // TODO : Setup Pdf reader
    // TODO: like LCO downloadable but not transferable


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