package com.example.sppucomptextbooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // SignIn Screen :

    EditText mUserID , mUserPassword;
    Button mSignInButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            Intent intent = new Intent(MainActivity.this, FoldersScreen.class);
            startActivity(intent);
            Toast.makeText(this, "Signing You IN", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please SignIn", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserID = findViewById(R.id.user_id);
        mUserPassword = findViewById(R.id.user_password);
        mSignInButton = findViewById(R.id.signInBtn);

        mAuth = FirebaseAuth.getInstance();

        if(mUserID.getText().toString().isEmpty() || mUserPassword.getText().toString().isEmpty()){
            mUserID.setError("Enter Your Email");
            mUserPassword.setError("Enter Your Password");
        }else{}

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignIN();
            }
        });

    }

    public void onSignIN() {
        final String idUser = mUserID.getText().toString();
        final String passUser = mUserPassword.getText().toString();

        if (idUser.isEmpty() && passUser.isEmpty()) {
            Toast.makeText(MainActivity.this, "Empty ID or Password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(idUser, passUser).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "SUCCESSFUL SignIN", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, FoldersScreen.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "INCORRECT ID or PASSWORD", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}