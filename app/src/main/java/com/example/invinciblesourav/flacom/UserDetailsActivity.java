package com.example.invinciblesourav.flacom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailsActivity extends AppCompatActivity {
Intent intent;
    String phoneNumber;
    TextView caption1,caption2;
    EditText getUsername,getPassword,getConfirmPass;
    ImageButton proceed;
    int errorFlag;
    LayoutInflater inflater;
    private String imeiNumber;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        errorFlag=0;
        intent=getIntent();
        phoneNumber=intent.getStringExtra("phoneNumber");
        imeiNumber=intent.getStringExtra("imei");
        Toast.makeText(getBaseContext(),"phone:"+phoneNumber,Toast.LENGTH_LONG).show();
        caption1=(TextView) findViewById(R.id.userdetls_caption1);
        caption2=(TextView) findViewById(R.id.userdetls_caption2);
        getUsername=(EditText) findViewById(R.id.userdtls_name);
        getPassword=(EditText) findViewById(R.id.userdtls_password);
        getConfirmPass=(EditText) findViewById(R.id.userdtls_confrmpswdd);
        proceed=(ImageButton) findViewById(R.id.userdetls_proceed);
        caption1.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        caption2.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        getUsername.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        getPassword.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        getConfirmPass.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getUsername.getText().toString().equals("")) {
                    errorFlag=1;
                }else if(getPassword.getText().toString().equals("")){
                    errorFlag=2;
                }else if(!getPassword.getText().toString().equals(getConfirmPass.getText().toString())){
                    errorFlag=3;
                }else {
                    Intent intent = new Intent(UserDetailsActivity.this, ProfilePicSet.class);
                    intent.putExtra("username", getUsername.getText().toString());
                    editor.putString("username",getUsername.getText().toString());
                    editor.commit();
                    intent.putExtra("password", getPassword.getText().toString());
                    intent.putExtra("phonenumber",phoneNumber);
                    intent.putExtra("imei",imeiNumber);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
                switch (errorFlag){
                    case 1: AlertDialog.Builder errordialogBuilder1 = new AlertDialog.Builder(UserDetailsActivity.this);
                        inflater=UserDetailsActivity.this.getLayoutInflater();
                        View errordialogView1 = inflater.inflate(R.layout.usernameerr, null);
                        errordialogBuilder1.setView(errordialogView1);
                        AlertDialog errorDialog1 = errordialogBuilder1.create();
                        errorDialog1.show();
                        TextView errorCaption1=(TextView) errorDialog1.findViewById(R.id.errortext);
                        errorCaption1.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                        errorCaption1.setTextSize(20);
                        getUsername.setFocusable(true);
                        break;
                    case 2: AlertDialog.Builder errordialogBuilder2 = new AlertDialog.Builder(UserDetailsActivity.this);
                        inflater=UserDetailsActivity.this.getLayoutInflater();
                        View errordialogView2 = inflater.inflate(R.layout.passworderr, null);
                        errordialogBuilder2.setView(errordialogView2);
                        AlertDialog errorDialog2 = errordialogBuilder2.create();
                        errorDialog2.show();
                        TextView errorCaption2=(TextView) errorDialog2.findViewById(R.id.errortext);
                        errorCaption2.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                        errorCaption2.setTextSize(20);
                        getPassword.setFocusable(true);
                        break;
                    case 3: AlertDialog.Builder errordialogBuilder3 = new AlertDialog.Builder(UserDetailsActivity.this);
                        inflater=UserDetailsActivity.this.getLayoutInflater();
                        View errordialogView3 = inflater.inflate(R.layout.pswdmatcherr, null);
                        errordialogBuilder3.setView(errordialogView3);
                        AlertDialog errorDialog3 = errordialogBuilder3.create();
                        errorDialog3.show();
                        TextView errorCaption3=(TextView) errorDialog3.findViewById(R.id.errortext);
                        errorCaption3.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                        errorCaption3.setTextSize(20);
                        getPassword.setFocusable(true);
                        break;
                }

            }
        });
    }
}
