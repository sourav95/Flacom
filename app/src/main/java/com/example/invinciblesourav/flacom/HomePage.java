package com.example.invinciblesourav.flacom;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private static final int MY_PERMISSIONS_DATA = 1;
    Button signout;
    int flag;
    FirebaseUser user;
    private TabLayout tabLayout;
    public static ViewPager viewPager;
    ViewFlipper viewFlipper;
    DatabaseReference databaseReference;
    String  picURL, imeiNumber;
    public static String userName;
    public static CircleImageView profilePicture, profileImage;
    int currentView = 1;
    public static TextView notifier, username, welcomeMsg;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String prevoiusImage;
    ImageButton sendFileBtn;
    LayoutInflater inflater;
    public static AlertDialog filePrompt;
    StorageReference storageReference;
    static Uri filePath;
    int tranasactionId;
    String downloadURi;
    static String size, time, filename;
    EditText getReceiverName;
    public static long childCount;
    long children=0;
    public static long sentCount;
    FilePicker filePicker;
    public static String userMobileNo;
    public static String recvName="",recvNum="";
    public static ImageView delete,forward,moreInfo,mark,close;
    public static LinearLayout bottomPopUp,viewFlipperSelector;
    static ArrayList<String> forwardName=new ArrayList<>();
    static ArrayList<String> forwardContacts=new ArrayList<>();
    public static int pressedCaller;
    public static LogDisplay logDisplay;
    public static Context homecontext;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
            homecontext=this.getBaseContext();
            tinyDB=new TinyDB(getBaseContext());
            //Toast.makeText(getBaseContext(),"In homepage",Toast.LENGTH_LONG).show();
            //stopService(new Intent(HomePage.this,Background.class));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            final Intent intent = getIntent();
            userName = intent.getStringExtra("username");
            logDisplay=new LogDisplay();
            logDisplay.inflater=this.getLayoutInflater();
            pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            editor = pref.edit();
            prevoiusImage = pref.getString("imageData", "");
            userName = pref.getString("username", "");
            //picURL = intent.getStringExtra("picURL");
            imeiNumber = intent.getStringExtra("imei");
            //Toast.makeText(this, "ReceiverName"+recvName, Toast.LENGTH_SHORT).show();
            imeiNumber = pref.getString("IMEI", "");
            sentCount = Long.parseLong(pref.getString("sentCount", "0"));
        //Toast.makeText(this, ""+userName+" "+picURL+" "+imeiNumber, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        //Toast.makeText(getBaseContext(),"Granted",Toast.LENGTH_LONG).show();

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                                MY_PERMISSIONS_DATA);
                        //  Toast.makeText(getBaseContext(),"Granted",Toast.LENGTH_LONG).show();
                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    //Toast.makeText(getBaseContext(),"Denied",Toast.LENGTH_LONG).show();
                }
            }

            //signout=(Button) findViewById(R.id.signout);


            databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
            storageReference = FirebaseStorage.getInstance().getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // User is signed in
                //Toast.makeText(getBaseContext(),user.getPhoneNumber(),Toast.LENGTH_LONG).show();
            } else {
                // No user is signed in
            }
            flag = 0;
            //Initializing the tablayout
            tabLayout = (TabLayout) findViewById(R.id.mainpage_tab);

            //Adding the tabs using addTab() method
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.addTab(tabLayout.newTab());
            //tabLayout.addTab(tabLayout.newTab());
            //tabLayout.addTab(tabLayout.newTab().setText("tab3"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            //Initializing viewPager
            viewPager = (ViewPager) findViewById(R.id.mainpage_viewpager);
            viewPager.setOffscreenPageLimit(4);
            //Creating our pager adapter
            MainPager adapter = new MainPager(getSupportFragmentManager(), tabLayout.getTabCount());

            //Adding adapter to pager
            viewPager.setAdapter(adapter);

            tabLayout.setupWithViewPager(viewPager);


            tabLayout.getTabAt(0).setIcon(R.drawable.receivedicon);
            tabLayout.getTabAt(1).setIcon(R.drawable.senticon);
            tabLayout.getTabAt(2).setIcon(R.drawable.erroricon);
            tabLayout.getTabAt(3).setIcon(R.drawable.contacts);

            //tabLayout.getTabAt(2).setIcon(R.drawable.user_icon);

            ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
            int tabscount = vg.getChildCount();
            for (int j = 0; j < tabscount; j++) {
                ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
                int tabchildsCount = vgTab.getChildCount();
                for (int i = 0; i < tabchildsCount; i++) {
                    View tabViewChild = vgTab.getChildAt(i);
                    if (tabViewChild instanceof TextView) {
                        //((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(),"OpenSans-Regular.ttf"));
                    }
                }
            }
            //Adding onTabSelectedListener to swipe views
            tabLayout.setOnTabSelectedListener(this);
            welcomeMsg = (TextView) findViewById(R.id.welcometext);
            welcomeMsg.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            username = (TextView) findViewById(R.id.username);
            username.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            username.setText(userName);
            final TextView homebutton = (TextView) findViewById(R.id.mainpage_homebutton);
            homebutton.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            final TextView settingsButton = (TextView) findViewById(R.id.mainpage_settbutton);
            settingsButton.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            final TextView profileButton = (TextView) findViewById(R.id.mainpage_profilebtn);
            profileButton.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            notifier = (TextView) findViewById(R.id.mainpage_notifier);
            notifier.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            notifier.setVisibility(View.GONE);
            viewFlipper = (ViewFlipper) findViewById(R.id.mainpage_flipper);
            final ImageView homeIcon = (ImageView) findViewById(R.id.mainpage_homeicon);
            final ImageView settingsIcon = (ImageView) findViewById(R.id.mainpage_settingsicon);
            final ImageView profileIcon = (ImageView) findViewById(R.id.mainpage_profileicon);
            LinearLayout homeBtn = (LinearLayout) findViewById(R.id.mainpage_homebtn);
            LinearLayout profileBtn = (LinearLayout) findViewById(R.id.mainpage_profilebtnbtn);
            final LinearLayout settingsBtn = (LinearLayout) findViewById(R.id.mainpage_settingsbtn);
            homebutton.setTextColor(Color.parseColor("#3690C4"));
            settingsButton.setTextColor(Color.WHITE);
            profileButton.setTextColor(Color.WHITE);
            homeIcon.setImageResource(R.drawable.homefocus);
            settingsIcon.setImageResource(R.drawable.settingsunfocused);
            profileIcon.setImageResource(R.drawable.profileunfocused);
            homeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homebutton.setTextColor(Color.parseColor("#3690C4"));
                    settingsButton.setTextColor(Color.WHITE);
                    profileButton.setTextColor(Color.WHITE);
                    homeIcon.setImageResource(R.drawable.homefocus);
                    settingsIcon.setImageResource(R.drawable.settingsunfocused);
                    profileIcon.setImageResource(R.drawable.profileunfocused);
                    showPager(1);
                    currentView = 1;
                }
            });
            settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homebutton.setTextColor(Color.WHITE);
                    settingsButton.setTextColor(Color.parseColor("#3690C4"));
                    profileButton.setTextColor(Color.WHITE);
                    homeIcon.setImageResource(R.drawable.homeunfocus);
                    settingsIcon.setImageResource(R.drawable.settingsfocused);
                    profileIcon.setImageResource(R.drawable.profileunfocused);
                    showPager(2);
                    currentView = 2;
                }

            });
            profileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homebutton.setTextColor(Color.WHITE);
                    settingsButton.setTextColor(Color.WHITE);
                    profileButton.setTextColor(Color.parseColor("#3690C4"));
                    homeIcon.setImageResource(R.drawable.homeunfocus);
                    settingsIcon.setImageResource(R.drawable.settingsunfocused);
                    profileIcon.setImageResource(R.drawable.profilefocused);
                    showPager(3);
                    currentView = 3;
                }
            });
            TextView profileName = (TextView) findViewById(R.id.profile_name);
            TextView userIMEI = (TextView) findViewById(R.id.profile_imei);
            TextView userMobile = (TextView) findViewById(R.id.profile_mobileno);
            profileName.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            profileName.setText("User name: " + userName);
            userIMEI.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            userIMEI.setText("IMEI number: " + imeiNumber);
            userMobile.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            userMobile.setText("Mobile Number " + user.getPhoneNumber());
            userMobileNo = user.getPhoneNumber();
            TextView changePic = (TextView) findViewById(R.id.settings_cppc);

            changePic.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            changePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1=new Intent(HomePage.this,ChangeProfilePic.class);
                    startActivity(intent1);
                    finish();
                }
            });
            /*profilePicture = (CircleImageView) findViewById(R.id.profilepic);
            profileImage = (CircleImageView) findViewById(R.id.profile_pic);

            Picasso.with(this)
                    .load(prevoiusImage)
                    .placeholder(R.drawable.userdefault)
                    .into(profilePicture, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        /*Picasso.with(this)
                .load(picURL)
                .placeholder(R.drawable.userdefault)
                .into(profilePicture);*/
           /* Picasso.with(this)
                    .load(prevoiusImage)
                    .placeholder(R.drawable.userdefault)
                    .placeholder(R.drawable.userdefault)
                    .into(profileImage);
        /*Picasso.with(this)
                .load(picURL)
                .placeholder(R.drawable.userdefault)
                .into(profileImage);*/


            sendFileBtn = (ImageButton) findViewById(R.id.sendfile);
            sendFileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("*/*");
                    startActivityForResult(intent, 9);

                }


            });

        AlertDialog.Builder fileDialogBuilder = new AlertDialog.Builder(HomePage.this);
        inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.sendfileprompt, null);
        fileDialogBuilder.setView(dialogView);
        filePrompt = fileDialogBuilder.create();
        filePrompt.show();
        filePrompt.setCanceledOnTouchOutside(false);
        filePrompt.setCancelable(false);
        filePrompt.dismiss();
            if (!recvNum.equals("")) {

                filePrompt.show();
                final TextView showPath = (TextView) filePrompt.findViewById(R.id.filepath);
                showPath.setText("Filepath: " + filePath.toString());
                getReceiverName = (EditText) filePrompt.findViewById(R.id.receivername);
                getReceiverName.setFocusable(false);
                getReceiverName.setText(recvName + " " + "(" + recvNum + ")");
                getReceiverName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SearchSender.class);
                        startActivity(intent);
                    }
                });
                Button dismissButton=(Button) filePrompt.findViewById(R.id.dismissBtn);
                dismissButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getReceiverName.setText("");
                        showPath.setText("");
                        recvName="";
                        recvNum="";
                        filePrompt.dismiss();
                    }
                });
                Button uploadButton = (Button) filePrompt.findViewById(R.id.uploadbtn);
                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        filePrompt.dismiss();
                        Random r = new Random();
                        tranasactionId = r.nextInt(1000000000);
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        time = dateFormat.format(calendar.getTime());
                        FileOperations fileOperations=new FileOperations();
                        String timeStamp="";
                        String senderNo=userMobileNo;
                        if(senderNo.startsWith("+91")){
                            senderNo=senderNo.substring(3,senderNo.length());
                        }
                        String receiverNo=recvNum;
                        if(receiverNo.startsWith("+91")){
                            receiverNo=receiverNo.substring(3,receiverNo.length());
                        }
                        Log.d("sendedr",senderNo+"  "+receiverNo);
                        String fileInput= null;
                        /*try {
                            fileInput = FileOperations.readFile(filePath.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                        //Log.d("Filekgjuyh",fileInput);
                        /*try {
                            Test.testify(fileInput);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        int dotIndex = filePath.toString().indexOf('.');
                        StoreKeys.length.clear();
                        StoreKeys.sumArray.clear();
                        StoreKeys.selectionKeys.clear();
                        StoreKeys.selectionPoint.clear();
                        StoreKeys.seqStartDepth.clear();
                        StoreKeys.seqStartRow.clear();
                        StoreKeys.rowTrKey.clear();
                        StoreKeys.depthTrKey.clear();
                        StoreKeys.decGroup.clear();
                        StoreKeys.messageDigest.clear();
                        StoreKeys.picEncryptKey="";

                        String format = filePath.toString().substring(dotIndex, filePath.toString().length());
                        logDisplay.createDialog(HomePage.this);
                        if(format.equals(".txt")) {
                            try {
                                String text;
                                logDisplay.addLine("Text file input",HomePage.this);
                                text = InvokeEncryption.encryptFeeder(filePath.toString(), senderNo, receiverNo);
                                //Log.d("Cipher text",text);
                                HomePage.logDisplay.addLine("Cipher text: "+text,HomePage.homecontext);
                                File direct = new File(Environment.getExternalStorageDirectory() + "/SendSec/temp");

                                if (!direct.exists()) {
                                    File wallpaperDirectory = new File("/sdcard/SendSec/temp");
                                    wallpaperDirectory.mkdirs();
                                }
                                timeStamp = String.valueOf(System.currentTimeMillis());
                                FileOperations.writeFile("/sdcard/SendSec/temp/" + timeStamp + ".txt", text);
                                //FileOperations.writeFile("/storage/emulated/0/SendSec/temp/121234.txt",text);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath();


                            uploadImage("file://" + path + "/SendSec/temp/" + timeStamp + ".txt",false);
                        }else if(format.equals(".jpg")){
                            String timestampSend=String.valueOf(System.currentTimeMillis());;
                            try {
                                HomePage.logDisplay.addLine("JPG input",HomePage.homecontext);
                                FileWriter fw=null;
                                String line="";
                                String [][]bits=EncryptPic.getBitStream(filePath.toString().substring(5,filePath.toString().length()));
                                HomePage.logDisplay.addLine("Writing encoded file to bits....",HomePage.homecontext);
                                for(int i=0;i< EncryptPic.height;i++){
                                    line="";
                                    for(int j=0;j< EncryptPic.cols;j++){
                                        line+=bits[i][j]+" ";
                                        //System.out.println("Rows"+i+" "+"columns"+j);
                                    }
                                    //System.out.print(line);
                                    //System.out.println();

                                    fw=new FileWriter("/sdcard/SendSec/temp/"+timestampSend+".txt",true);
                                    fw.write(line+"\n");
                                    fw.close();

                                }


                                Log.d("Size", EncryptPic.height+" "+EncryptPic.cols);

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                                String encryptionKey=InvokeEncryption.encryptFeeder(EncryptPic.randomKey,senderNo,receiverNo);
                                StoreKeys.picEncryptKey=encryptionKey;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                            uploadImage("file://" + path +"/SendSec/temp/"+timestampSend+".txt",true);
                        }else{
                            String timestampSend2=String.valueOf(System.currentTimeMillis());
                            OtherFileHandler.generateRandomString();
                            String byteString=OtherFileHandler.getXoredByteString(filePath.toString().substring(5,filePath.toString().length()));
                            System.out.println("BYTESTRING "+byteString);
                            System.out.println("RANDOMKEY "+OtherFileHandler.randomString);
                            System.out.println(byteString);
                            try {
                                FileOperations.writeFile("/sdcard/SendSec/temp/"+timestampSend2+".txt",byteString );
                                String encryptionKey=InvokeEncryption.encryptFeeder(OtherFileHandler.randomString,senderNo,receiverNo);
                                StoreKeys.picEncryptKey=encryptionKey;
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                            uploadImage("file://" + path +"/SendSec/temp/"+timestampSend2+".txt",true);

                        }

                        /*SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
                        String strdate = dateFormat1.format(calendar.getTime());
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId));
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("auxFilename").setValue(strdate+System.currentTimeMillis()+".txt");
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("status").setValue("ST");
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverName").setValue(recvName);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("timestamp").setValue(time);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("index").setValue(String.valueOf(sentCount));
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("filename").setValue(filename);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("size").setValue(size);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderName").setValue(userName);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderID").setValue(userMobileNo);
                        databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverID").setValue("+91"+recvNum);

                        sentCount += 1;
                        editor.putString("sentCount", String.valueOf(sentCount));
                        editor.commit();
                        recvNum="";*/
                        //filePrompt.dismiss();

                    }
                });
            }
            bottomPopUp=(LinearLayout) findViewById(R.id.longHoldops);
            viewFlipperSelector=(LinearLayout) findViewById(R.id.linearLayout);
            forward=(ImageView) findViewById(R.id.forwardIcon);
            delete=(ImageView) findViewById(R.id.deleteIcon);
            moreInfo=(ImageView) findViewById(R.id.moreInfo);
            mark=(ImageView) findViewById(R.id.markIcon);
            close=(ImageView) findViewById(R.id.closeIcon);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pressedCaller==0) {
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Received.longPress = false;
                        Received.selectedItems.clear();
                        Received rcv = new Received();
                        rcv.deselect();
                    }else if(pressedCaller==1){
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Sent.sentLongpress = false;
                        Sent.sentSelectedItems.clear();
                        Sent sent = new Sent();
                        sent.deselect(HomePage.this.getBaseContext());
                    }
                    pressedCaller=-1;
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pressedCaller==0) {
                        Received received1 = new Received();
                        received1.deleteRecord();
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Received.longPress = false;
                        Received.selectedItems.clear();
                    }else if(pressedCaller==1){
                        Sent sent = new Sent();
                        sent.deleteRecord(HomePage.this.getBaseContext());
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Sent.sentLongpress = false;
                        Sent.sentSelectedItems.clear();
                    }
                }
            });
            forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    makeDialog();
                }
            });
            mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pressedCaller==0) {
                        Received received = new Received();
                        received.marker();
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Received.longPress = false;
                        Received.markedList.clear();
                        Received.savedMarkedList.clear();
                    }else if(pressedCaller==1){
                        Sent sent = new Sent();
                        sent.marker(HomePage.this.getBaseContext());
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Sent.sentLongpress = false;
                        Sent.sentMarkedMarkList.clear();
                        Sent.sentSavedMarkList.clear();
                    }
                    pressedCaller=-1;
                }
            });
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    moreDetails();
                }
            });

    }

    private void showPager(int i) {

        if (currentView == 1 && i == 2) {
            notifier.setVisibility(View.GONE);
            username.setText("");
            welcomeMsg.setText("Settings");
            viewFlipper.showNext();
            sendFileBtn.setVisibility(View.GONE);

        } else if (currentView == 1 && i == 3) {
            notifier.setVisibility(View.GONE);
            username.setText("");
            welcomeMsg.setText("Profile");
            viewFlipper.showNext();
            viewFlipper.showNext();
            sendFileBtn.setVisibility(View.GONE);
        } else if (currentView == 2 && i == 1) {
            username.setText(userName);
            welcomeMsg.setText("Welcome,");

            if(viewPager.getCurrentItem()==0){
                notifier.setVisibility(View.INVISIBLE);
            }else{
                notifier.setVisibility(View.VISIBLE);
            }
            viewFlipper.showPrevious();
            sendFileBtn.setVisibility(View.VISIBLE);
        } else if (currentView == 2 && i == 3) {
            username.setText("");
            welcomeMsg.setText("Profile");
            notifier.setVisibility(View.GONE);
            viewFlipper.showNext();
            sendFileBtn.setVisibility(View.GONE);
        } else if (currentView == 3 && i == 1) {
            username.setText(userName);
            welcomeMsg.setText("Welcome,");
            notifier.setVisibility(View.VISIBLE);
            if(viewPager.getCurrentItem()==0){
                notifier.setVisibility(View.INVISIBLE);
            }else{
                notifier.setVisibility(View.VISIBLE);
            }
            viewFlipper.showPrevious();
            viewFlipper.showPrevious();
            sendFileBtn.setVisibility(View.VISIBLE);
        } else if (currentView == 3 && i == 2) {
            username.setText("");
            welcomeMsg.setText("Settings");
            notifier.setVisibility(View.GONE);
            viewFlipper.showPrevious();
            sendFileBtn.setVisibility(View.GONE);
        }


    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
        if(viewPager.getCurrentItem()==0){
            notifier.setVisibility(View.GONE);
            Received.recordCount=0;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getBaseContext(),String.valueOf(resultCode),Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {
            case 9:
                if (resultCode == RESULT_OK) {

                    filePath = data.getData();

                    final File file = new File(filePath.toString());
                    ContentResolver contentResolver = getContentResolver();
                    try {
                        InputStream inputStream = contentResolver.openInputStream(filePath);
                        size = String.valueOf(((float) inputStream.available()) / 1000);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    filename = file.getName();
                    //Toast.makeText(HomePage.this, PathHolder, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder fileDialogBuilder = new AlertDialog.Builder(HomePage.this);
                    inflater = this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.sendfileprompt, null);
                    fileDialogBuilder.setView(dialogView);
                    filePrompt = fileDialogBuilder.create();
                    filePrompt.show();
                    filePrompt.setCancelable(false);
                    filePrompt.setCanceledOnTouchOutside(false);
                    final TextView showPath = (TextView) filePrompt.findViewById(R.id.filepath);
                    showPath.setText("Filepath: " + filePath.toString());
                    getReceiverName = (EditText) filePrompt.findViewById(R.id.receivername);
                    getReceiverName.setFocusable(false);
                    if(recvNum=="") {
                        getReceiverName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), SearchSender.class);
                                startActivity(intent);
                            }
                        });
                    }else{
                        getReceiverName.setText(recvName+"("+recvNum+")");
                    }
                    Button dismissButton=(Button) filePrompt.findViewById(R.id.dismissBtn);
                    dismissButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getReceiverName.setText("");
                            showPath.setText("");
                            recvName="";
                            recvNum="";
                            filePrompt.dismiss();
                        }
                    });
                    Button uploadButton = (Button) filePrompt.findViewById(R.id.uploadbtn);
                    uploadButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(recvNum!="") {
                                filePrompt.dismiss();
                                Random r = new Random();
                                tranasactionId = r.nextInt(1000000000);
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                time = dateFormat.format(calendar.getTime());
                                FileOperations fileOperations = new FileOperations();
                                String timeStamp = "";
                                String senderNo = userMobileNo;
                                if (senderNo.startsWith("+91")) {
                                    senderNo = senderNo.substring(3, senderNo.length());
                                }
                                String receiverNo = recvNum;
                                if (receiverNo.startsWith("+91")) {
                                    receiverNo = receiverNo.substring(3, receiverNo.length());
                                }
                                Log.d("sendedr", senderNo + "  " + receiverNo);
                                String fileInput = null;
                        /*try {
                            fileInput = FileOperations.readFile(filePath.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }*/
                                //Log.d("Filekgjuyh",fileInput);
                        /*try {
                            Test.testify(fileInput);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                                int dotIndex = filePath.toString().indexOf('.');
                                StoreKeys.length.clear();
                                StoreKeys.sumArray.clear();
                                StoreKeys.selectionKeys.clear();
                                StoreKeys.selectionPoint.clear();
                                StoreKeys.seqStartDepth.clear();
                                StoreKeys.seqStartRow.clear();
                                StoreKeys.rowTrKey.clear();
                                StoreKeys.depthTrKey.clear();
                                StoreKeys.decGroup.clear();
                                StoreKeys.messageDigest.clear();
                                StoreKeys.picEncryptKey = "";

                                String format = filePath.toString().substring(dotIndex, filePath.toString().length());
                                logDisplay.createDialog(HomePage.this);
                                if (format.equals(".txt")) {
                                    try {
                                        String text;
                                        logDisplay.addLine("Text file received", HomePage.this);
                                        text = InvokeEncryption.encryptFeeder(filePath.toString(), senderNo, receiverNo);
                                        //Log.d("Cipher text",text);
                                        HomePage.logDisplay.addLine("Cipher text: " + text, HomePage.homecontext);
                                        File direct = new File(Environment.getExternalStorageDirectory() + "/SendSec/temp");

                                        if (!direct.exists()) {
                                            File wallpaperDirectory = new File("/sdcard/SendSec/temp");
                                            wallpaperDirectory.mkdirs();
                                        }
                                        timeStamp = String.valueOf(System.currentTimeMillis());
                                        FileOperations.writeFile("/sdcard/SendSec/temp/" + timeStamp + ".txt", text);
                                        //FileOperations.writeFile("/storage/emulated/0/SendSec/temp/121234.txt",text);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();


                                    uploadImage("file://" + path + "/SendSec/temp/" + timeStamp + ".txt", false);
                                } else if (format.equals(".jpg")) {
                                    String timestampSend = String.valueOf(System.currentTimeMillis());
                                    ;
                                    try {
                                        FileWriter fw = null;
                                        String line = "";
                                        String[][] bits = EncryptPic.getBitStream(filePath.toString().substring(5, filePath.toString().length()));
                                        for (int i = 0; i < EncryptPic.height; i++) {
                                            line = "";
                                            for (int j = 0; j < EncryptPic.cols; j++) {
                                                line += bits[i][j] + " ";
                                                //System.out.println("Rows"+i+" "+"columns"+j);
                                            }
                                            //System.out.print(line);
                                            //System.out.println();

                                            fw = new FileWriter("/sdcard/SendSec/temp/" + timestampSend + ".txt", true);
                                            fw.write(line + "\n");
                                            fw.close();

                                        }


                                        Log.d("Size", EncryptPic.height + " " + EncryptPic.cols);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        String encryptionKey = InvokeEncryption.encryptFeeder(EncryptPic.randomKey, senderNo, receiverNo);
                                        StoreKeys.picEncryptKey = encryptionKey;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    uploadImage("file://" + path + "/SendSec/temp/" + timestampSend + ".txt", true);
                                } else {
                                    String timestampSend2 = String.valueOf(System.currentTimeMillis());
                                    OtherFileHandler.generateRandomString();
                                    String byteString = OtherFileHandler.getXoredByteString(filePath.toString().substring(5, filePath.toString().length()));
                                    System.out.println("BYTESTRING " + byteString);
                                    System.out.println("RANDOMKEY " + OtherFileHandler.randomString);
                                    System.out.println(byteString);
                                    try {
                                        FileOperations.writeFile("/sdcard/SendSec/temp/" + timestampSend2 + ".txt", byteString);
                                        String encryptionKey = InvokeEncryption.encryptFeeder(OtherFileHandler.randomString, senderNo, receiverNo);
                                        StoreKeys.picEncryptKey = encryptionKey;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                                    uploadImage("file://" + path + "/SendSec/temp/" + timestampSend2 + ".txt", true);

                                }

                            }
                            //uploadImage();
                            //Random r = new Random();
                            //tranasactionId = r.nextInt(1000000000);
                            //Calendar calendar = Calendar.getInstance();
                            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            //time = dateFormat.format(calendar.getTime());
                            /*SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
                            String strdate = dateFormat1.format(calendar.getTime());
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId));
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("auxFilename").setValue(strdate+System.currentTimeMillis()+".txt");
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("status").setValue("ST");
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverName").setValue(recvName);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("timestamp").setValue(time);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("index").setValue(String.valueOf(sentCount));
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("filename").setValue(filename);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("size").setValue(size);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderName").setValue(userName);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderID").setValue(userMobileNo);
                            databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverID").setValue("+91"+recvNum);


                            sentCount+=1;
                            editor.putString("sentCount",String.valueOf(sentCount));
                            editor.commit();
                            Sent sent = new Sent();
                            Sent.sentFilename.add(filename);
                            Sent.sentFileRecv.add(recvName);
                            Sent.sentFileTime.add(time);
                            Sent.sentFileSize.add(size);
                            Sent.sentFileStat.add("Sent");
                            sent.populateSent(HomePage.this);*/


                       }
                    });
                }
                break;
        }

    }



    public void uploadImage(final String fileSource, final boolean otherFlag) {
        if (fileSource != null) {
            HomePage.logDisplay.logHeader.setText("Uploading File");
            StorageReference mRef = storageReference.child("filestore/" + System.currentTimeMillis() + "." + "txt");
            final Uri fileResoruce=Uri.parse(fileSource);
            mRef.putFile(fileResoruce).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String storeRowTransKey="";
                    String storeDepthTranskey="";
                    String storeRowSeqStart="";
                    String storeDepthSeqStart="";
                    String storeLength="";
                    String storeSelectionKey="";
                    String storeSelectionPoint="";
                    String storeSum="";
                    String decPoints="";
                    String msgDigests="";
                    for(int i=0;i<StoreKeys.rowTrKey.size();i++){
                        storeRowTransKey+=StoreKeys.rowTrKey.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.depthTrKey.size();i++){
                        storeDepthTranskey+=StoreKeys.depthTrKey.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.seqStartRow.size();i++){
                        storeRowSeqStart+=StoreKeys.seqStartRow.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.seqStartDepth.size();i++){
                        storeDepthSeqStart+=StoreKeys.seqStartDepth.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.length.size();i++){
                        storeLength+=StoreKeys.length.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.selectionKeys.size();i++){
                        storeSelectionKey+=StoreKeys.selectionKeys.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.selectionPoint.size();i++){
                        storeSelectionPoint+=StoreKeys.selectionPoint.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.sumArray.size();i++){
                        storeSum+=StoreKeys.sumArray.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.decGroup.size();i++){
                        decPoints+=StoreKeys.decGroup.get(i)+"%";
                    }
                    for(int i=0;i<StoreKeys.messageDigest.size();i++){
                        msgDigests+=StoreKeys.messageDigest.get(i)+"%";
                    }
                    String finalKeySet=storeRowTransKey+"<"+storeDepthTranskey+"<"+storeRowSeqStart+"<"+storeDepthSeqStart+"<"+storeLength+"<"+storeSelectionKey+"<"+storeSelectionPoint+"<"+storeSum+"<"+decPoints+"<"+msgDigests;
                    if(otherFlag==true){
                        finalKeySet=finalKeySet+"<"+StoreKeys.picEncryptKey;
                    }
                    System.out.println(finalKeySet);
                    HomePage.logDisplay.addLine("Keyset transmitted: "+finalKeySet,HomePage.homecontext);
                    Calendar calendar = Calendar.getInstance();
                    downloadURi = taskSnapshot.getDownloadUrl().toString();
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
                    String strdate = dateFormat1.format(calendar.getTime());
                    String auxTime=strdate+System.currentTimeMillis()+".txt";
                    Toast.makeText(HomePage.this, ""+downloadURi, Toast.LENGTH_SHORT);
                    //databaseReference.child("Transactions").child(String.valueOf(tranasactionId));
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("uri").setValue(downloadURi);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("keyset").setValue(finalKeySet);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("gateway").setValue(String.valueOf(InvokeEncryption.keyToSend));
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("auxFilename").setValue(auxTime);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("status").setValue("ST");
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverName").setValue(recvName);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("timestamp").setValue(time);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("index").setValue(String.valueOf(sentCount));
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("filename").setValue(filename);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("size").setValue(size);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderName").setValue(userName);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderID").setValue(userMobileNo);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverID").setValue("+91"+recvNum);

                    Sent.sentFileStat.add("ST");
                    Sent.sentFilename.add(filename);
                    Sent.sentFileUri.add(downloadURi);
                    Sent.sentFileRecv.add(recvName);
                    Sent.sentFileTime.add(time);
                    Sent.sentFileSize.add(size);
                    Sent.sentAuxFilename.add(auxTime);
                    Sent.senttransactionIds.add(tranasactionId);
                    Sent.sentFileIndex.add(String.valueOf(sentCount));

                    Sent sent=new Sent();
                    sent.displayListSent(HomePage.this.getBaseContext());
                    sentCount+=1;
                    editor.putString("sentCount",String.valueOf(sentCount));
                    editor.commit();
                    HomePage.logDisplay.logHeader.setText("Encryption complete");
                    HomePage.logDisplay.progressBar.setVisibility(View.GONE);
                    recvNum="";
                    recvName="";
                    /*Sent sent = new Sent();
                    Sent.sentFilename.add(filename);
                    Sent.sentFileRecv.add(recvName);
                    Sent.sentFileTime.add(time);
                    Sent.sentFileSize.add(size);
                    Sent.sentFileStat.add("Sent");
                    sent.populateSent(HomePage.this);*/

                    Toast.makeText(getBaseContext(), "File sent", Toast.LENGTH_SHORT).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Failed to send file", Toast.LENGTH_LONG).show();
                            //Unsent unsent = new Unsent();
                            //Unsent.unsentFilename.add(filename);
                            //Unsent.unsentFileSend.add(getReceiverName.getText().toString());
                            //Unsent.unsentFileTime.add(time);
                            //Unsent.unsentFileSize.add(size);
                            //Unsent.unsentFileStat.add("Sent");
                            //unsent.populateUnsent(HomePage.this);
                            e.printStackTrace();
                        }
                    });


        }else {
            Toast.makeText(this, "Some error occured while sending file.Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_DATA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(getBaseContext(),"Granted",Toast.LENGTH_LONG).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //Toast.makeText(getBaseContext(),"Granted",Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }


    }
    public void createNotification(){
        NotificationCompat.Builder notification=new NotificationCompat.Builder(HomePage.this)
                                                .setSmallIcon(R.drawable.receivedicon)
                                                .setContentTitle("Message")
                                                .setContentText("dfkjjjjjvkjdvnkdb");
        Intent notoficationIntent=new Intent(HomePage.this,HomePage.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,notoficationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,notification.build());

    }

   @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startService(new Intent(HomePage.this,Background.class));
        //Background.notif_flag=1;
        moveTaskToBack(true);
       Background.firstTimeRead=0;
        editor.putString("recordCount",String.valueOf(Received.recordCount));
        editor.commit();
        //Intent startMain = new Intent(HomePage.this,SplashScreen.class);
        //startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(startMain);
        //finish();


    }


    @Override
    protected void onUserLeaveHint() {
        //super.onUserLeaveHint();
        //Background.notif_flag=1;
        Background.firstTimeRead=0;
        startService(new Intent(HomePage.this,Background.class));
        editor.putString("recordCount",String.valueOf(Received.recordCount));
        editor.commit();
        //moveTaskToBack(true);
        //Intent startMain = new Intent(Intent.ACTION_MAIN);
        //startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(startMain);
        }
    @Override
    protected void onResume(){
        super.onResume();
        //Background.notif_flag=-1;
        Background.numberOfFiles=0;
        Background.receiver.clear();
        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
    public void makeDialog(){
        forwardContacts.clear();
        forwardName.clear();
        AlertDialog.Builder fileDialogBuilder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.forwardprompt, null);
        fileDialogBuilder.setView(dialogView);
        final AlertDialog filePrompt = fileDialogBuilder.create();
        filePrompt.show();
        Button forwardBtn=filePrompt.findViewById(R.id.forwardbtn);
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forwardContacts.size()!=0) {
                    if(pressedCaller==0) {
                        filePrompt.dismiss();
                        Received received = new Received();
                        received.forward();
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Received.longPress = false;
                        Received.selectedItems.clear();
                        received.deselect();
                    }else if(pressedCaller==1) {
                        Toast.makeText(HomePage.this, "bokachoadsa", Toast.LENGTH_SHORT).show();
                        filePrompt.dismiss();
                        Sent sent = new Sent();
                        sent.forward();
                        bottomPopUp.setVisibility(View.GONE);
                        viewFlipperSelector.setVisibility(View.VISIBLE);
                        Sent.sentLongpress = false;
                        Sent.sentSelectedItems.clear();
                        sent.deselect(HomePage.this.getBaseContext());
                    }
                }else{
                    Toast.makeText(HomePage.this, "Please select a sender", Toast.LENGTH_SHORT).show();
                }
                pressedCaller=-1;

            }
        });
        final LinearLayout forwardcontacts=(LinearLayout) filePrompt.findViewById(R.id.forwardContacts);
        SearchView searchReceiver = (SearchView) filePrompt.findViewById(R.id.forwardserach);
        printList(forwardcontacts);
        searchReceiver.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText,forwardcontacts);
                return false;
            }
        });
        printList(forwardcontacts);
    }
    public void printList(LinearLayout displayReceiever) {
        displayReceiever.removeAllViews();
        for (int i = 0; i < SearchSender.ContactsNames.size(); i++) {
            boolean avalFlag = false;
            final LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout iconDisplay = new LinearLayout(this);
            iconDisplay.setOrientation(LinearLayout.VERTICAL);
            ImageView fileicon = new ImageView(this);
            LinearLayout messageDetails = new LinearLayout(this);
            messageDetails.setOrientation(LinearLayout.VERTICAL);
            Space spaceTop = new Space(this);
            TextView contactname = new TextView(this);
            contactname.setText(SearchSender.ContactsNames.get(i));
            contactname.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            TextView contactImei = new TextView(this);
            contactImei.setText(SearchSender.ContactsNumbers.get(i));
            contactImei.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            Space spaceBelow = new Space(this);


            messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
            messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

            fileicon.setImageResource(R.drawable.contactsicon);
            Space spaceicon1 = new Space(this);
            Space spaceicon2 = new Space(this);
            final int index=i;
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   if(card.getAlpha()==1.0f){
                       card.setAlpha(0.99f);
                       card.setBackgroundColor(Color.parseColor("#1E1F60"));
                       forwardName.add(SearchSender.ContactsNames.get(index));
                       forwardContacts.add(SearchSender.ContactsNumbers.get(index));
                   }else{
                       card.setAlpha(1.0f);
                       card.setBackgroundColor(Color.parseColor("#0a0a0a"));
                       for(int i=0;i<forwardContacts.size();i++){
                           if(forwardContacts.get(i).equalsIgnoreCase(SearchSender.ContactsNumbers.get(index))){
                               forwardContacts.remove(i);
                               forwardName.remove(i);
                               break;
                           }
                       }
                   }
                }
            });
            iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
            card.setBackgroundResource(R.drawable.listborder);
            card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

            displayReceiever.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        }

    }
    public void filter(String text,LinearLayout displayReceiever ) {
        displayReceiever.removeAllViews();
        if (!text.equals("")) {
            for (int i = 0; i < SearchSender.ContactsNames.size(); i++) {
                if (SearchSender.ContactsNames.get(i).toLowerCase().contains(text.toLowerCase())) {
                    //Toast.makeText(getContext(),StoreContactNames.get(i),Toast.LENGTH_SHORT).show();
                    LinearLayout card = new LinearLayout(this);
                    card.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout iconDisplay = new LinearLayout(this);
                    iconDisplay.setOrientation(LinearLayout.VERTICAL);
                    ImageView fileicon = new ImageView(this);
                    LinearLayout messageDetails = new LinearLayout(this);
                    messageDetails.setOrientation(LinearLayout.VERTICAL);
                    Space spaceTop = new Space(this);
                    TextView contactname = new TextView(this);
                    contactname.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
                    TextView contactImei = new TextView(this);
                    SpannableString spannable;

                    int pos = SearchSender.ContactsNames.get(i).toLowerCase().indexOf(text.toLowerCase());
                    spannable = new SpannableString(SearchSender.ContactsNames.get(i));
                    spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    contactname.setText(spannable);
                    contactImei.setText(SearchSender.ContactsNumbers.get(i));
                    contactImei.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
                    Space spaceBelow = new Space(this);


                    messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                    messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                    messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                    fileicon.setImageResource(R.drawable.contactsicon);
                    Space spaceicon1 = new Space(this);
                    Space spaceicon2 = new Space(this);
                    iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                    iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                    card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                    card.setBackgroundResource(R.drawable.listborder);
                    card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    final int index=i;
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getBaseContext(),HomePage.class);
                            HomePage.recvNum=SearchSender.ContactsNumbers.get(index);
                            HomePage.recvName=SearchSender.ContactsNames.get(index);
                            startActivity(intent);
                            finish();
                        }
                    });
                    //ImageButton menu=new ImageButton(this.getContext());
                    //menu.setImageResource(R.drawable.addpeople);
                    //menu.setBackgroundColor(Color.parseColor("#00ffffff"));


                    displayReceiever.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            }
            for (int i = 0; i < SearchSender.ContactsNumbers.size(); i++) {
                if (SearchSender.ContactsNumbers.get(i).contains(text)) {
                    //Toast.makeText(getContext(),StoreContactNames.get(i),Toast.LENGTH_SHORT).show();

                    LinearLayout card = new LinearLayout(this);
                    card.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout iconDisplay = new LinearLayout(this);
                    iconDisplay.setOrientation(LinearLayout.VERTICAL);
                    ImageView fileicon = new ImageView(this);
                    LinearLayout messageDetails = new LinearLayout(this);
                    messageDetails.setOrientation(LinearLayout.VERTICAL);
                    Space spaceTop = new Space(this);
                    TextView contactname = new TextView(this);
                    contactname.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
                    TextView contactImei = new TextView(this);
                    SpannableString spannable;

                    int pos = SearchSender.ContactsNumbers.get(i).toLowerCase().indexOf(text.toLowerCase());
                    spannable = new SpannableString(SearchSender.ContactsNumbers.get(i));
                    spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    contactname.setText(SearchSender.ContactsNames.get(i));
                    contactImei.setText(spannable);
                    contactImei.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
                    Space spaceBelow = new Space(this);


                    messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                    messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                    messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                    messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                    fileicon.setImageResource(R.drawable.contactsicon);
                    Space spaceicon1 = new Space(this);
                    Space spaceicon2 = new Space(this);
                    iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                    iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                    card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                    card.setBackgroundResource(R.drawable.listborder);
                    card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    //ImageButton menu=new ImageButton(this.getContext());
                    //menu.setImageResource(R.drawable.addpeople);
                    //menu.setBackgroundColor(Color.parseColor("#00ffffff"));
                    final int index=i;
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getBaseContext(),HomePage.class);
                            HomePage.recvNum=SearchSender.ContactsNumbers.get(index);
                            HomePage.recvName=SearchSender.ContactsNames.get(index);
                            startActivity(intent);
                            finish();
                        }
                    });

                    displayReceiever.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            }
        }else {
            printList(displayReceiever);
        }
    }

    public void moreDetails() {

        Received.sortIndex();
        AlertDialog.Builder fileDialogBuilder = new AlertDialog.Builder(HomePage.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.moreinfodlg, null);
        fileDialogBuilder.setView(dialogView);
        final AlertDialog filePrompt = fileDialogBuilder.create();
        filePrompt.show();
        TextView fileName=(TextView) filePrompt.findViewById(R.id.filename);
        TextView fileSender=(TextView) filePrompt.findViewById(R.id.filesender);
        TextView fileTime=(TextView) filePrompt.findViewById(R.id.filetime);
        TextView fileSize=(TextView) filePrompt.findViewById(R.id.filesize);
        TextView fileType=(TextView) filePrompt.findViewById(R.id.filetype);
        TextView fileAux=(TextView) filePrompt.findViewById(R.id.fileaux);
        TextView fileTid=(TextView) filePrompt.findViewById(R.id.filetid);
        if(pressedCaller==0) {
            int index = Received.selectedItems.get(0);
            for (int i = 0; i < Received.indexarray.length; i++) {
                for (int j = 0; j < Received.recvFileIndex.size(); j++) {
                    if (Integer.parseInt(Received.recvFileIndex.get(j)) == Integer.parseInt(Received.indexarray[i])) {
                        if (j == index) {
                            //Toast.makeText(HomePage.this, "" + Received.recvFileUri.get(j), Toast.LENGTH_SHORT).show();
                            fileName.setText("File name: " + Received.recvFilename.get(j));
                            fileSender.setText("File Sender: " + Received.recvFileRecv.get(j));
                            fileTime.setText("Sent on: " + Received.recvFileTime.get(j));
                            fileSize.setText("File size: " + Received.recvFileSize.get(j));
                            fileType.setText("File type: .txt");
                            fileAux.setText("Auxiliary filename: " + Received.recvAuxFilename.get(j));
                            fileTid.setText("Transaction ID: " + Received.transactionIds.get(j));
                        }
                        break;
                    }
                }
            }
            Received received=new Received();
            Received.longPress = false;
            Received.selectedItems.clear();
            received.deselect();
        }else if(pressedCaller==1){
            int index = Sent.sentSelectedItems.get(0);
            for(int i=Sent.sentFilename.size()-1;i>=0;i--){
                //Toast.makeText(this, ""+i+" "+index, Toast.LENGTH_SHORT).show();
                if(index==i){
                    fileName.setText("File name: " + Sent.sentFilename.get(i));
                    fileSender.setText("File receiver: " + Sent.sentFileRecv.get(i));
                    fileTime.setText("Sent on: " + Sent.sentFileTime.get(i));
                    fileSize.setText("File size: " + Sent.sentFileSize.get(i));
                    fileType.setText("File type: .txt");
                    fileAux.setText("Auxiliary filename: " + Sent.sentAuxFilename.get(i));
                    fileTid.setText("Transaction ID: " + Sent.senttransactionIds.get(i));
                    break;
                }

            }
            Sent sent=new Sent();
            Sent.sentLongpress=false;
            Sent.sentSelectedItems.clear();
            sent.deselect(HomePage.this.getBaseContext());
        }
        bottomPopUp.setVisibility(View.GONE);
        viewFlipperSelector.setVisibility(View.VISIBLE);
        pressedCaller=-1;
    }

    @Override
    protected void onStart() {
        final Context context=getBaseContext();
        super.onStart();
        final TinyDB tinyDB=new TinyDB(getBaseContext());
        picURL=tinyDB.getString("picurl");
        System.out.println(picURL);
        profilePicture = (CircleImageView) findViewById(R.id.profilepic);
        profileImage = (CircleImageView) findViewById(R.id.profile_pic);
        if(picURL!=null&& !picURL.equalsIgnoreCase("")) {
            Picasso.with(this)
                    .load(picURL)
                    .placeholder(R.drawable.userdefault)
                    .into(profilePicture);
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
                    if (picURL != null && !picURL .equalsIgnoreCase("") ) {
                        Picasso.with(context)
                                .load(picURL)
                                .placeholder(R.drawable.userdefault)
                                .into(profilePicture);
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
