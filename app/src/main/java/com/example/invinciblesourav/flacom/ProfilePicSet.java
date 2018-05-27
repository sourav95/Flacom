package com.example.invinciblesourav.flacom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProfilePicSet extends AppCompatActivity {
private ImageButton uploadPic;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private Uri mCropImageUri,resultUri,downloadUri;
    private TextView caption1,caption2;
    private ImageButton finalProceed;
    DatabaseReference databaseReference;
    private String userName,password,phoneNumber,imeiNumber;
    private StorageReference storageReference;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Bitmap realImage;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic_set);
        tinyDB=new TinyDB(getBaseContext());
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        Intent i = new Intent();
        i = getIntent();
        userName = i.getStringExtra("username");
        password = i.getStringExtra("password");
        phoneNumber = i.getStringExtra("phonenumber");
        imeiNumber=i.getStringExtra("imei");
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
               get_imei_data();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                        1);
            }
        }*/


            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
            storageReference = FirebaseStorage.getInstance().getReference();
            circleImageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profilepic);
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSelectImageClick(view);
                }
            });
            uploadPic = (ImageButton) findViewById(R.id.uploadpic);
            uploadPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    onSelectImageClick(view);
                }
            });
            caption1 = (TextView) findViewById(R.id.profilepic_caption1);
            caption2 = (TextView) findViewById(R.id.profilepic_caption2);
            caption1.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            caption2.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            finalProceed = (ImageButton) findViewById(R.id.final_proceed);
            finalProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child("Users").child(imeiNumber);
                    databaseReference.child("Users").child(imeiNumber).child("username").setValue(userName);
                    databaseReference.child("Users").child(imeiNumber).child("password").setValue(password);
                    databaseReference.child("Users").child(imeiNumber).child("mobile").setValue(phoneNumber);
                    uploadImage();

                }
            });

        }


    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                ((de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profilepic)).setImageURI(result.getUri());
                resultUri=result.getUri();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
        /*switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    // you may now do the action that requires this permission

                } else {
                    // permission denied
                }
                return;
            }
        }*/

    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAllowRotation(true)
                .start(this);

    }
    public void uploadImage() {
        if (resultUri != null) {
            editor.putString("imageData",resultUri.toString());
            editor.commit();
            StorageReference mRef = storageReference.child("profilepics/" + System.currentTimeMillis() + "." + "jpg");
            mRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Toast.makeText(getBaseContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    downloadUri=taskSnapshot.getDownloadUrl();
                    databaseReference.child("Users").child(imeiNumber).child("profileURI").setValue(downloadUri.toString());
                    tinyDB.putString("picurl",downloadUri.toString());
                    Intent intent=new Intent(ProfilePicSet.this,HomePage.class);
                    startActivity(intent);
                    finish();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfilePicSet.this, "Failed to upload profile image", Toast.LENGTH_LONG).show();
                            tinyDB.putString("picurl","");
                            e.printStackTrace();
                        }
                    });


        }else{
            tinyDB.putString("picurl","");
        }
    }



}