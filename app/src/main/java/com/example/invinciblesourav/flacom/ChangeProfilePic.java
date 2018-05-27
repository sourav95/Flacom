package com.example.invinciblesourav.flacom;

import android.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfilePic extends AppCompatActivity {
    private Uri mCropImageUri,resultUri,downloadUri;
    private ImageButton changeProfilePic,submitButton;
    private StorageReference storageReference;
    DatabaseReference databaseReference;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TinyDB tinyDB;
    private Button back;
    private Button deletePic;
    String picURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB=new TinyDB(getBaseContext());
        setContentView(R.layout.activity_change_profile_pic);
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        changeProfilePic=(ImageButton) findViewById(R.id.changepic);
        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectImageClick(view);
            }
        });
        submitButton=(ImageButton) findViewById(R.id.change_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        back=(Button) findViewById(R.id.backbuttonChange);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChangeProfilePic.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        });
        deletePic=(Button) findViewById(R.id.deletePic);
        deletePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImage();
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
        storageReference = FirebaseStorage.getInstance().getReference();
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
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                ((de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.changeprofilepic)).setImageURI(result.getUri());
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
    private void uploadImage() {
        if (resultUri != null) {
            final String imeiNumber = pref.getString("IMEI", "");
            StorageReference mRef = storageReference.child("profilepics/" + System.currentTimeMillis() + "." + "jpg");
            mRef.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getBaseContext(), "Profile Pic uploaded", Toast.LENGTH_SHORT).show();
                    downloadUri=taskSnapshot.getDownloadUrl();
                    databaseReference.child("Users").child(imeiNumber).child("profileURI").setValue(downloadUri.toString());
                    tinyDB.putString("picurl",downloadUri.toString());


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });


        }
    }
    private void deleteImage() {

            final String imeiNumber = pref.getString("IMEI", "");
            //StorageReference mRef = storageReference.child("profilepics/" + System.currentTimeMillis() + "." + "jpg");

                    Toast.makeText(getBaseContext(), "Profile Pic deleted", Toast.LENGTH_SHORT).show();
                    ((de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.changeprofilepic)).setImageResource(R.drawable.userdefault);

                    databaseReference.child("Users").child(imeiNumber).child("profileURI").setValue("");

                    tinyDB.putString("picurl","");








    }
    @Override
    protected void onStart() {
        final Context context=getBaseContext();
        super.onStart();
        final TinyDB tinyDB=new TinyDB(getBaseContext());
        picURL=tinyDB.getString("picurl");
        System.out.println(picURL);
        //profilePicture = (CircleImageView) findViewById(R.id.profilepic);
        final CircleImageView profileImage = (CircleImageView) findViewById(R.id.changeprofilepic);
        if(picURL!=null&&!picURL.equalsIgnoreCase("")) {

            Picasso.with(this)
                    .load(picURL)
                    .placeholder(R.drawable.userdefault)
                    .into(profileImage);
        }
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        final String imeiNumber = pref.getString("IMEI", "");
        Log.d("IMEI",imeiNumber);
        databaseReference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String imei=dataSnapshot.getKey();
                if(imei.equalsIgnoreCase(imeiNumber)) {
                    picURL = dataSnapshot.child("profileURI").getValue(String.class);
                    System.out.println(picURL);
                    tinyDB.putString("picurl", picURL);
                    if (picURL != null && !picURL .equalsIgnoreCase("")) {

                        Picasso.with(context)
                                .load(picURL)
                                .placeholder(R.drawable.userdefault)
                                .into(profileImage);
                    }
                }

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



    }

}
