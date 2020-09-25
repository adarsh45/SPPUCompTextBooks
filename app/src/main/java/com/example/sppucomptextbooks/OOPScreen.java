package com.example.sppucomptextbooks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OOPScreen extends AppCompatActivity {

    // OOP Folder screen - Will contain pdf ....

    Button sampleBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oop);

        sampleBtn = findViewById(R.id.sampleBtn);

        sampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OOPScreen.this, PDFViewer.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
//        finish();
        Intent a = new Intent(OOPScreen.this, FoldersScreen.class);
        startActivity(a);
    }

}
