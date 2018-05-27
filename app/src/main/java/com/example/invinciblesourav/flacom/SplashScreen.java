package com.example.invinciblesourav.flacom;

import android.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.invinciblesourav.flacom.HomePage.profilePicture;

public class SplashScreen extends AppCompatActivity {
    private static final int MY_PERMISSIONS_DATA = 1;
    private FirebaseAuth mAuth;
    String name;
    private DatabaseReference databaseReference;
    private static int SPLASH_TIME_OUT = 3000;
    String imeiNumber, picUrl;
    TextView gerec;
    FirebaseUser currentUser;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int flag = 0;
    String userId;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        txt=(TextView) findViewById(R.id.splashText);
        txt.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        userId = pref.getString("username", "");
        imeiNumber = pref.getString("IMEI", "");

        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
                if (currentUser == null) {
                    Intent intent = new Intent(SplashScreen.this, SignUpActivity.class);
                    intent.putExtra("imei", imeiNumber);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {

                   //Toast.makeText(getBaseContext(), "Imei" + imeiNumber, Toast.LENGTH_SHORT).show();
                    databaseReference.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            name = dataSnapshot.child(imeiNumber).child("username").getValue(String.class);
                            picUrl = dataSnapshot.child(imeiNumber).child("profileURI").getValue(String.class);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Toast.makeText(getBaseContext(), imeiNumber, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SplashScreen.this, HomePage.class);
                        intent.putExtra("phoneNumber", currentUser.getPhoneNumber());
                        intent.putExtra("username", name);
                        intent.putExtra("picURL", picUrl);
                        intent.putExtra("imei", imeiNumber);
                        startActivity(intent);

                        finish();

                }

           }
        }, SPLASH_TIME_OUT);


        //Toast.makeText(getBaseContext(),imeiNumber,Toast.LENGTH_LONG).show();


    }
}


