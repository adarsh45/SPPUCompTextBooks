package com.example.sppucomptextbooks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sppucomptextbooks.pojo.StudentData;
import com.example.sppucomptextbooks.subjects.SubjectCG;
import com.example.sppucomptextbooks.subjects.SubjectDELD;
import com.example.sppucomptextbooks.subjects.SubjectDM;
import com.example.sppucomptextbooks.subjects.SubjectFDS;
import com.example.sppucomptextbooks.subjects.SubjectOOP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.easyupipayment.EasyUpiPayment;
import com.shreyaspatil.easyupipayment.exception.AppNotFoundException;
import com.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import com.shreyaspatil.easyupipayment.model.PaymentApp;
import com.shreyaspatil.easyupipayment.model.TransactionDetails;

import java.util.Objects;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;


public class FoldersScreen extends AppCompatActivity implements PaymentStatusListener, NavigationView.OnNavigationItemSelectedListener{


    public static final String TAG = "Payment button";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private View header;
    private ActionBarDrawerToggle mToggle;
    private TextView textUserName, textUserPhone, textUserTeamsID;

    private TextView textWelcomeUser, textPayStatus, textPayAmount;
    private LinearLayout layoutPayment, layoutPaymentStatus, layoutSubjects;

//    String trId = currentUser.

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase myDB;
    DatabaseReference rootRef;
    String paymentGivenStatus;

    private StudentData studentData = null;

    private Random random = new Random();


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

//        shared pref
//        SharedPreferences sp = getSharedPreferences("paymentCounter", Context.MODE_PRIVATE);

        initialize();
        MotionToast.Companion.setInfoBackgroundColor(R.color.custom_info_color);
        MotionToast.Companion.setInfoColor(R.color.info_color);

        MotionToast.Companion.createToast(this, "Loading Data....",
                MotionToast.TOAST_INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.helveticabold));



        rootRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    studentData = snapshot.getValue(StudentData.class);
                    String welcome = "Welcome, " + studentData.getName() + "!";
                    textWelcomeUser.setText(welcome);
                    textUserName.setText(studentData.getName());
                    textUserPhone.setText(studentData.getPhoneNumber());
                    textUserTeamsID.setText(studentData.getMsTeamsId());

                    paymentGivenStatus = String.valueOf(studentData.getPaymentGiven());
                    if (studentData.getPaymentGiven() > 0){
                        textPayStatus.setText("SUCCESS");
                        textPayAmount.setText(R.string.fifty_rs);
                        layoutPayment.setVisibility(View.GONE);
                        layoutPaymentStatus.setVisibility(View.VISIBLE);
                        layoutSubjects.setVisibility(View.VISIBLE);
                    } else {
                        layoutPayment.setVisibility(View.VISIBLE);
                        layoutPaymentStatus.setVisibility(View.GONE);
                        layoutSubjects.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(FoldersScreen.this, "Error logging in! Please Logout and then try again!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoldersScreen.this, "Something went wrong! Please restart the App!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initialize() {
//        views
        layoutPayment = findViewById(R.id.layout_payment);
        layoutPaymentStatus = findViewById(R.id.layout_payment_status);
        layoutSubjects = findViewById(R.id.layout_subjects);

        textWelcomeUser = findViewById(R.id.text_welcome_user);
        textPayStatus = findViewById(R.id.text_pay_status);
        textPayAmount = findViewById(R.id.text_pay_amount);

//        firebase initialization
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        myDB = FirebaseDatabase.getInstance();
        rootRef = myDB.getReference("Users");

//        navigation & toolbar
        //setting new toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //init navigationView for onClicks on menu items
        navigationView = findViewById(R.id.navigation_layout);
        header = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        //init drawer_layout and toggle_button
        mDrawerLayout = findViewById(R.id.home_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

//        trying to change hamburger menu color
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

//        navbar texts
        textUserName = header.findViewById(R.id.nav_user_name);
        textUserPhone = header.findViewById(R.id.nav_user_phone);
        textUserTeamsID = header.findViewById(R.id.nav_user_teams_id);
    }


    public void openSubject(View view) {
        // Move to Subject Screen


        switch (view.getId()){
            case R.id.fds_btn:
                Intent intentFDS = new Intent(FoldersScreen.this, SubjectFDS.class);
                startActivity(intentFDS);
                break;
            case R.id.dm_btn:
                Intent intentDM = new Intent(FoldersScreen.this, SubjectDM.class);
                startActivity(intentDM);
                break;
            case R.id.oop_btn:
                Intent intentOOP = new Intent(FoldersScreen.this, SubjectOOP.class);
                startActivity(intentOOP);
                break;
            case R.id.cg_btn:
                Intent intentCG = new Intent(FoldersScreen.this, SubjectCG.class);
                startActivity(intentCG);
                break;
            case R.id.deld_btn:
                Intent intentDELD = new Intent(FoldersScreen.this, SubjectDELD.class);
                startActivity(intentDELD);
                break;

        }
    }

    public void payThroughUPI(View view){
        EasyUpiPayment.Builder builder = null;
        EasyUpiPayment easyUpiPayment = null;

        String randomPaymentCounter = String.format("%04d", random.nextInt(10000));

        try {
            builder = new EasyUpiPayment.Builder(FoldersScreen.this)
                    .with(PaymentApp.ALL)
                    .setPayeeVpa("9552412863@ybl")
                    .setPayeeName("Adarsh Shete")
                    .setTransactionId(studentData.getUid()+ randomPaymentCounter)
                    .setTransactionRefId(studentData.getMsTeamsId()+ randomPaymentCounter)
                    .setDescription("Comp-Books app subscription")
                    .setAmount("59.00");
        } catch (IllegalStateException isExc){
            Log.d(TAG, "payThroughUPI: "+ isExc.getLocalizedMessage());
            Toast.makeText(this, isExc.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                easyUpiPayment = builder.build();
            } catch (AppNotFoundException e){
                Log.d(TAG, "payThroughUPI: "+ e.getLocalizedMessage());
                Toast.makeText(this, "UPI app not found", Toast.LENGTH_SHORT).show();
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
        textPayStatus.setText("Cancelled");

        MotionToast.Companion.darkColorToast(
                this,
                "Transaction Cancelled!",
                MotionToast.TOAST_INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.helvetica_regular)
        );

    }

    @Override
    public void onTransactionCompleted(TransactionDetails transactionDetails) {
        Log.d(TAG, "onTransactionCompleted: "+ transactionDetails.toString());
        textPayStatus.setText(transactionDetails.getTransactionStatus().toString());
        textPayAmount.setText(transactionDetails.getAmount());

//        Toast.makeText(this, transactionDetails.toString(), Toast.LENGTH_SHORT).show();

        // update value in DB

        if (transactionDetails.getTransactionStatus().toString().equals("SUCCESS")){
            Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
            rootRef.child(currentUser.getUid()).child("paymentGiven").setValue(59).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){

                        MotionToast.Companion.darkColorToast(
                                FoldersScreen.this,
                                "Successful Payment Registered ... Restart app to access the folders",
                                MotionToast.TOAST_SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(FoldersScreen.this, R.font.helveticabold)
                        );


                    } else {

                        MotionToast.Companion.darkColorToast(
                                FoldersScreen.this,
                                "Unsuccessful Payment Registered Contact the Developers",
                                MotionToast.TOAST_ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(FoldersScreen.this, R.font.helveticabold)
                        );

                    }
                }
            });
        } else {

            MotionToast.Companion.darkColorToast(
                    FoldersScreen.this,
                    "Something wrong with transaction! If amount is deducted contact our developers",
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(FoldersScreen.this, R.font.helveticabold)
            );

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                Toast.makeText(this, "Section is under development", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                mAuth.signOut();
                rootRef.child(currentUser.getUid()).child("alreadyLoggedIn").setValue(false);
                Intent i = new Intent(FoldersScreen.this, MainActivity.class);
                startActivity(i);
                finish();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
