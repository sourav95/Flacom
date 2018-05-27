package com.example.invinciblesourav.flacom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;


public class SignUpActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private ImageButton proceedBtn;
    private Button verifyBtn;
    private EditText getPhoneNumber,getOtp;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private Animation moveDown;
    private TextView captionGet,captionReg;
    private LayoutInflater inflater;
    private  AlertDialog progressDialog,otpDialog;
    private String imeiNumber;
    CountDownTimer timer,timer1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        Intent intent=getIntent();
        imeiNumber=intent.getStringExtra("imei");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }else {
            TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            imeiNumber = telemamanger.getDeviceId();
            //Toast.makeText(getBaseContext(), "Imei" + imeiNumber, Toast.LENGTH_SHORT).show();
            editor.putString("IMEI", imeiNumber);
            editor.commit();
        }
        proceedBtn = (ImageButton) findViewById(R.id.signin_proceed);
        getPhoneNumber = (EditText) findViewById(R.id.signin_mobile);
        //getOtp = (EditText) findViewById(R.id.signin_otp);
        //verifyBtn = (Button) findViewById(R.id.signon_verify);
        captionGet=(TextView) findViewById(R.id.signin_caption2);
        captionReg=(TextView) findViewById(R.id.signin_caption1);
        captionGet.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        captionReg.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        getPhoneNumber.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        mAuth = FirebaseAuth.getInstance();


        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(SignUpActivity.this);
        alertdialogbuilder.setMessage("Please enter a valid 10 digit mobile number");
        final AlertDialog alertDialog=alertdialogbuilder.create();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
        inflater=this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progressdialog, null);
        dialogBuilder.setView(dialogView);
        progressDialog = dialogBuilder.create();

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNo = getPhoneNumber.getText().toString();

                    if(isValidPhone(phoneNo)&& phoneNo.length()==10) {
                        phoneNo="+91"+phoneNo;
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNo,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                SignUpActivity.this,// Activity (for callback binding)
                                mCallbacks);        // OnVerificationStateChangedCallbacks
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        final TextView progressCaption=(TextView) progressDialog.findViewById(R.id.progressdlgtext);
                        progressCaption.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                        progressCaption.setTextSize(20);
                        timer=new CountDownTimer(60000,1000) {
                            @Override
                            public void onTick(long l) {
                                progressCaption.setText("Sending OTP in "+String.valueOf(l/1000)+" seconds");
                            }

                            @Override
                            public void onFinish() {
                                progressDialog.dismiss();
                                AlertDialog.Builder errordialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                                inflater=SignUpActivity.this.getLayoutInflater();
                                View errordialogView = inflater.inflate(R.layout.errordialog, null);
                                errordialogBuilder.setView(errordialogView);
                                AlertDialog errorDialog = errordialogBuilder.create();
                                errorDialog.show();
                                TextView errorCaption=(TextView) errorDialog.findViewById(R.id.errortext);
                                errorCaption.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                                errorCaption.setTextSize(20);
                            }
                        }.start();
                    }else {
                        alertDialog.show();
                        TextView errorMsg=(TextView) alertDialog.findViewById(android.R.id.message);
                        errorMsg.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                        errorMsg.setTextSize(20);
                        errorMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;
                progressDialog.dismiss();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                inflater=SignUpActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.otpdialog, null);
                dialogBuilder.setView(dialogView);
                otpDialog = dialogBuilder.create();
                otpDialog.show();
                otpDialog.setCanceledOnTouchOutside(false);
                otpDialog.setCancelable(false);
                TextView otpCaption=(TextView) otpDialog.findViewById(R.id.otpdlgcaption);
                otpCaption.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                otpCaption.setTextSize(20);

            }
        };
        /*verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String vfCode = getOtp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vfCode);
                signInWithPhoneAuthCredential(credential);
            }
        });*/
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            //Toast.makeText(getBaseContext(), "Imei" + imeiNumber, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, UserDetailsActivity.class);
                            intent.putExtra("phoneNumber",user.getPhoneNumber());
                            intent.putExtra("imei",imeiNumber);
                            startActivity(intent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            finish();


                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            //Log.w("kk", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });


    }
    private boolean isValidPhone(String phone){
        if(TextUtils.isEmpty(phone)){
            return false;
        }else{
            return Patterns.PHONE.matcher(phone).matches();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }



    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int readStorage=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        int readPhonestate=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        int readContacts=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (readStorage!=PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (readPhonestate!=PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readContacts!=PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    TelephonyManager telemamanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    imeiNumber = telemamanger.getDeviceId();
                    Toast.makeText(getBaseContext(), "Imei" + imeiNumber, Toast.LENGTH_SHORT).show();
                    editor.putString("IMEI", imeiNumber);
                    editor.commit();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String message = intent.getStringExtra("message");
                String otp="";
                for(int i=0;i<message.length();i++){
                    if(message.charAt(i)=='0' || message.charAt(i)=='1'|| message.charAt(i)=='2'|| message.charAt(i)=='3'|| message.charAt(i)=='4'||message.charAt(i)=='5'||message.charAt(i)=='6'||message.charAt(i)=='7'||message.charAt(i)=='8'||message.charAt(i)=='9'){
                        otp+=String.valueOf(message.charAt(i));
                    }
                    else {
                        break;
                    }
                }
                ProgressBar otpProgress=(ProgressBar) otpDialog.findViewById(R.id.otpprogress);
                otpProgress.setVisibility(View.INVISIBLE);
                final EditText otpText=(EditText) otpDialog.findViewById(R.id.otptext);
                otpText.setText(otp);
                timer.cancel();
                otpText.setEnabled(true);
                otpText.setTextSize(20);
                otpText.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                Button otpVerify=(Button) otpDialog.findViewById(R.id.otpVerify);
                otpVerify.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
                otpVerify.setTextSize(20);
                otpVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String vfCode = otpText.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, vfCode);
                        signInWithPhoneAuthCredential(credential);

                    }
                });
            }
        }
    };

}

