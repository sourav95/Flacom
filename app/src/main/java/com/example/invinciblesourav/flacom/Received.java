package com.example.invinciblesourav.flacom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static android.R.attr.keySet;
import static android.R.attr.path;

public class Received extends Fragment {
    private static final int MY_PERMISSIONS_DATA = 1;
    LinearLayout listingItem;
    DatabaseReference databaseReference;
    static View fragmentRecv;
    static String[] indexarray;
    public static ArrayList<Integer> markedList=new ArrayList<>();
    public static ArrayList<Integer> savedMarkedList=new ArrayList<>();
    public static ArrayList<String> recvFilename = new ArrayList<String>();
    public static ArrayList<String> recvFileRecv = new ArrayList<String>();
    public static ArrayList<String> recvFileTime = new ArrayList<String>();
    public static ArrayList<String> recvFileSize = new ArrayList<String>();
    public static ArrayList<String> recvFileUri = new ArrayList<>();
    public static ArrayList<String> recvFileIndex=new ArrayList<>();
    public static ArrayList<String> recvAuxFilename=new ArrayList<>();
    public static ArrayList<String> transactionIds=new ArrayList<>();
    public static ArrayList<Integer> selectedItems=new ArrayList<>();
    public static ArrayList<String> keySets=new ArrayList<>();
    public static ArrayList<String> keyRecv=new ArrayList<>();
    public static ArrayList<String> senderMob=new ArrayList<>();
    public static ArrayList<String> downloadedFileUri=new ArrayList<>(10);
    ArrayList<String> ContactNames=new ArrayList<>();
    ArrayList<String> ContactNumbers=new ArrayList<>();
    public static Context thiscontext;
    static Intent intent;
    String file_url;
    private LayoutInflater inflater;
    public static AlertDialog progressDialog;
    public static long recordCount=0;
    static View sheetview;
    static BottomSheetDialog bottomSheetDialog;
    static int screenHeight;
    long count=0;
    public static ProgressBar progressBar;
    public static TextView progressValue;
    Cursor cursor;
    public static TinyDB tinyDB;
    public static boolean longPress=false;
    public static boolean deSelectAll=false;
    int lastUriIndex;
    public static LogDisplay logDisplay;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRecv = inflater.inflate(R.layout.activity_received, container, false);
        logDisplay=new LogDisplay();
        logDisplay.inflater=this.getLayoutInflater();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        inflater=this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.downloadprogress, null);
        dialogBuilder.setView(dialogView);
        progressDialog = dialogBuilder.create();
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressBar=(ProgressBar) progressDialog.findViewById(R.id.downloadprgbar);
        progressValue=(TextView) progressDialog.findViewById(R.id.progressVal);
        progressBar.setMax(100);
        progressDialog.dismiss();
        //Background.notif_flag=0;
        recordCount=0;
        getContacts();
        Background.numberOfFiles=0;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        listingItem = (LinearLayout) fragmentRecv.findViewById(R.id.received_holder);
        thiscontext = this.getContext();
        tinyDB=new TinyDB(thiscontext);

        /*if (ContextCompat.checkSelfPermission(thiscontext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_DATA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/
        //Toast.makeText(thiscontext, HomePage.userMobileNo, Toast.LENGTH_SHORT).show();
        recvFileTime.clear();
        recvFileIndex.clear();
        recvAuxFilename.clear();
        recvFileUri.clear();
        recvFileSize.clear();
        recvFileIndex.clear();
        recvFileRecv.clear();
        transactionIds.clear();
        keySets.clear();
        keyRecv.clear();
        senderMob.clear();
        downloadedFileUri.clear();
//tinyDB.clear();
        recvFilename=tinyDB.getListString("filenames");
        recvFileRecv=tinyDB.getListString("filesenders");
        recvFileTime=tinyDB.getListString("filetimes");
        recvFileSize=tinyDB.getListString("filesizes");
        recvFileIndex=tinyDB.getListString("fileindices");
        recvAuxFilename=tinyDB.getListString("fileauxnames");
        recvFileUri=tinyDB.getListString("fileuris");
        transactionIds=tinyDB.getListString("transId");
        keySets=tinyDB.getListString("keyStore");
        keyRecv=tinyDB.getListString("keyToDecrypt");
        senderMob=tinyDB.getListString("senderNumber");
        downloadedFileUri=tinyDB.getListString("fileStorageUri");
        selectedItems.clear();
        displayList();

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
        databaseReference.child("Transactions").orderByChild("receiverID").equalTo(HomePage.userMobileNo).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {



               boolean prefetchedFlag=false;
                String auxFile=dataSnapshot.child("auxFilename").getValue(String.class);
                for(int i=0;i<recvAuxFilename.size();i++){

                    if(recvAuxFilename.get(i).equals(auxFile)){
                        prefetchedFlag=true;
                    }
                }
                //Toast.makeText(thiscontext, ""+prefetchedFlag, Toast.LENGTH_SHORT).show();
                if(prefetchedFlag==false) {
                    recordCount+=1;
                    //Toast.makeText(getBaseContext(),String.valueOf(dataSnapshot.getKey()),Toast.LENGTH_LONG).show();
                    if(HomePage.viewPager.getCurrentItem()!=0){
                        HomePage.notifier.setVisibility(View.VISIBLE);
                        HomePage.notifier.setText(String.valueOf(recordCount));
                    }
                    //Toast.makeText(thiscontext,dataSnapshot.child("index").getValue(String.class),Toast.LENGTH_LONG).show();
                    recvFilename.add(dataSnapshot.child("filename").getValue(String.class));

                    //recvFileRecv.add(dataSnapshot.child("senderID").getValue(String.class));
                    String senderId = dataSnapshot.child("senderID").getValue(String.class);
                    senderMob.add(senderId);
                    boolean flag = false;
                    String senderName = "";
                    for (int p = 0; p < ContactNumbers.size(); p++) {
                        //Toast.makeText(thiscontext, ""+ContactNumbers.get(p), Toast.LENGTH_SHORT).show();
                        if (("+91" + ContactNumbers.get(p)).equals((senderId))) {
                            flag = true;
                            senderName = ContactNames.get(p);
                            break;
                        }
                    }
                    if (flag == false) {
                        recvFileRecv.add(senderId);
                    } else {
                        recvFileRecv.add(senderName);
                    }

                    recvAuxFilename.add(dataSnapshot.child("auxFilename").getValue(String.class));
                    recvFileSize.add(dataSnapshot.child("size").getValue(String.class));
                    recvFileUri.add(dataSnapshot.child("uri").getValue(String.class));
                    recvFileTime.add(dataSnapshot.child("timestamp").getValue(String.class));
                    recvFileIndex.add(dataSnapshot.child("index").getValue(String.class));
                    transactionIds.add(dataSnapshot.getKey());
                    keySets.add(dataSnapshot.child("keyset").getValue(String.class));
                    keyRecv.add(dataSnapshot.child("gateway").getValue(String.class));
                    //Toast.makeText(thiscontext,dataSnapshot.child("index").getValue(String.class),Toast.LENGTH_LONG).show();

                    displayList();

                    tinyDB.putListString("filenames", recvFilename);
                    tinyDB.putListString("filesenders", recvFileRecv);
                    tinyDB.putListString("filetimes", recvFileTime);
                    tinyDB.putListString("filesizes", recvFileSize);
                    tinyDB.putListString("fileindices", recvFileIndex);
                    tinyDB.putListString("fileauxnames", recvAuxFilename);
                    tinyDB.putListString("fileuris", recvFileUri);
                    tinyDB.putListString("transId",transactionIds);
                    tinyDB.putListString("keyStore",keySets);
                    tinyDB.putListString("keyToDecrypt",keyRecv);
                    tinyDB.putListString("senderNumber",senderMob);
                    tinyDB.putListString("fileStorageUri",downloadedFileUri);
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



        return fragmentRecv;
    }

    /*public static void populateRecv(final Context context) {
        int index=0;
        LinearLayout listingItem = (LinearLayout) fragmentRecv.findViewById(R.id.received_holder);
        sortIndex();
        listingItem.removeAllViews();
        for (int i = 0; i <indexarray.length; i++) {
            for(int j=0;j<recvFileIndex.size();j++){
                if(Integer.parseInt(recvFileIndex.get(j))==Integer.parseInt(indexarray[i])){
                    index=j;
                    break;
                }
            }

            LinearLayout card = new LinearLayout(thiscontext);
            //card.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout iconDisplay = new LinearLayout(thiscontext);
            iconDisplay.setOrientation(LinearLayout.VERTICAL);
            ImageView fileicon = new ImageView(thiscontext);
            LinearLayout messageDetails = new LinearLayout(thiscontext);
            messageDetails.setOrientation(LinearLayout.VERTICAL);
            Space spaceTop = new Space(thiscontext);
            TextView filename = new TextView(thiscontext);
            filename.setText(recvFilename.get(index));
            filename.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
            final TextView filesender = new TextView(thiscontext);
            filesender.setText("From : " + recvFileRecv.get(index));
            //Toast.makeText(thiscontext,recvFilename.get(i).toString(),Toast.LENGTH_LONG).show();
            filesender.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
            TextView timestamp = new TextView(thiscontext);
            timestamp.setText(recvFileTime.get(index));
            timestamp.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
            TextView size = new TextView(thiscontext);
            size.setText(recvFileSize.get(index) + " KB");
            size.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
            Space spaceBelow = new Space(thiscontext);


            messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
            messageDetails.addView(filename, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(filesender, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(timestamp, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(size, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

            fileicon.setImageResource(R.drawable.fileicon);
            Space spaceicon1 = new Space(thiscontext);
            Space spaceicon2 = new Space(thiscontext);
            iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
            card.setBackgroundResource(R.drawable.listborder);
            card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Received.this.getContext(),DisplayFile.class);
                    Toast.makeText(thiscontext, "jjj", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            });

            card.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                    bottomSheetDialog = new BottomSheetDialog(thiscontext);
                    sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                    bottomSheetDialog.setContentView(sheetview);
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                    bottomSheetBehavior.setPeekHeight(1000);
                    bottomSheetDialog.show();
                    return false;
                }
            });





            if (i == 0) {
                card.setBackgroundColor(Color.parseColor("#1E1F60"));
            }
            final int c = i;
            card.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            //new DownloadFileFromURL(recvFilename.get(c)).execute(recvFileUri.get(c));
                                            //String line=ReadFile(thiscontext,recvFilename.get(c));
                                            //Toast.makeText(thiscontext,line,Toast.LENGTH_SHORT).show();
                                        }
                                    });

                    //intent=new Intent(context,DisplayFile.class);
                    //startActivity(intent);

                    listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                }

        }*/
    private void getContacts(){
        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            if(phonenumber.startsWith("+91")){
                phonenumber=phonenumber.substring(3,phonenumber.length());
            }
            String number="";
            for(int i=0;i<phonenumber.length();i++){
                if(phonenumber.charAt(i)!=' '){
                    number=number+phonenumber.charAt(i);
                }
            }
            phonenumber=number;
            ContactNames.add(name);

            ContactNumbers.add(phonenumber);



        }

        cursor.close();

    }
    public static  String ReadFile( Context context,String fileName){
        String line = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File("/sdcard/SendSec/Downloads/"+fileName));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();

            while ( (line = bufferedReader.readLine()) != null )
            {
                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
            line = stringBuilder.toString();

            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            //Log.d(TAG, ex.getMessage());
        }
        catch(IOException ex) {
            //Log.d(TAG, ex.getMessage());
        }
        Toast.makeText(context, ""+line, Toast.LENGTH_SHORT).show();
        return line;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_DATA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }

    }
    public static void sortIndex(){



        indexarray=recvFileIndex.toArray(new String[recvFileIndex.size()]);
        for(int i=0;i<indexarray.length;i++){
            for(int j=0;j<indexarray.length-i-1;j++){
                if(Integer.parseInt(indexarray[j])<Integer.parseInt(indexarray[j+1])){
                    String temp=indexarray[j];
                    indexarray[j]=indexarray[j+1];
                    indexarray[j+1]=temp;
                }
            }
        }
    }
    public  boolean checkFilename(String filename){
        try {
            FileInputStream fileInputStream = new FileInputStream (new File("/sdcard/SendSec/Downloads/"+filename));
        } catch (FileNotFoundException e) {
            //Toast.makeText(thiscontext, "File not found", Toast.LENGTH_SHORT).show();
            return false;
        }
        //Toast.makeText(thiscontext, "File found", Toast.LENGTH_SHORT).show();
        return true;
    }
    public void deleteRecord(){

     Toast.makeText(Received.thiscontext, ""+selectedItems.toString(), Toast.LENGTH_SHORT).show();

        displayList();

        selectedItems.clear();

        displayList();
    }
    public void deselect(){

        deSelectAll=true;
        displayList();
        deSelectAll=false;

    }
    public void monitorSelected(){
        if(selectedItems.size()>1){
            HomePage.moreInfo.setAlpha(0.3f);
            HomePage.moreInfo.setEnabled(false);
        }else{
            HomePage.moreInfo.setAlpha(1.0f);
            HomePage.moreInfo.setEnabled(true);
        }
    }
    public void forward() {


        int index=0;
        for (int i = 0; i < indexarray.length; i++) {
            for (int j = 0; j < recvFileIndex.size(); j++) {
                if (Integer.parseInt(recvFileIndex.get(j)) == Integer.parseInt(indexarray[i])) {
                    index = j;
                    break;
                }
            }

            for (int k = 0; k < selectedItems.size(); k++) {
                if (index == selectedItems.get(k)) {
                for(int m=0;m<HomePage.forwardName.size();m++){
                    uploadImage(index,HomePage.forwardName.get(m),HomePage.forwardContacts.get(m));
                }

                }
            }
        }
    }
    public void uploadImage(int index, String receiverName, String receiverContact) {
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        pref = thiscontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        Random r = new Random();
        int tranasactionId = r.nextInt(1000000000);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String time = dateFormat.format(calendar.getTime());
        String size=recvFileSize.get(index);

        String filename=recvFilename.get(index);

        long sentCount= Long.parseLong(pref.getString("sentCount", "0"));
                    //Calendar calendar = Calendar.getInstance();
                    String downloadURi = recvFileUri.get(index);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
                    String strdate = dateFormat1.format(calendar.getTime());
                    Toast.makeText(Received.thiscontext, ""+downloadURi, Toast.LENGTH_SHORT).show();
                    String auxtime=strdate+System.currentTimeMillis()+".txt";
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId));
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("uri").setValue(downloadURi);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("auxFilename").setValue(auxtime);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("status").setValue("ST");
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverName").setValue(receiverName);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("timestamp").setValue(time);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("index").setValue(String.valueOf(sentCount));
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("filename").setValue(filename);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("size").setValue(size);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderName").setValue(HomePage.userName);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("senderID").setValue(HomePage.userMobileNo);
                    databaseReference.child("Transactions").child(String.valueOf(tranasactionId)).child("receiverID").setValue("+91"+receiverContact);


        Sent.sentFileStat.add("ST");
        Sent.sentFilename.add(filename);
        Sent.sentFileUri.add(downloadURi);
        Sent.sentFileRecv.add(receiverName);
        Sent.sentFileTime.add(time);
        Sent.sentFileSize.add(size);
        Sent.sentAuxFilename.add(auxtime);
        Sent.senttransactionIds.add(tranasactionId);
        Sent.sentFileIndex.add(String.valueOf(sentCount));

        Sent sent=new Sent();
        sent.displayListSent(Received.thiscontext);
                    sentCount+=1;
                    editor.putString("sentCount",String.valueOf(sentCount));
                    editor.commit();
                    //recvNum="";
                    /*Sent sent = new Sent();
                    Sent.sentFilename.add(filename);
                    Sent.sentFileRecv.add(recvName);
                    Sent.sentFileTime.add(time);
                    Sent.sentFileSize.add(size);
                    Sent.sentFileStat.add("Sent");
                    sent.populateSent(HomePage.this);*/

                    Toast.makeText(Received.thiscontext, "File sent", Toast.LENGTH_SHORT).show();


    }
   public void marker(){

        for(int i=0;i<selectedItems.size();i++){
            markedList.add(selectedItems.get(i));
        }

       selectedItems.clear();
        displayList();




   }
    protected void displayList(){

        int index = 0;
        LinearLayout listingItem = (LinearLayout) fragmentRecv.findViewById(R.id.received_holder);
        sortIndex();

        listingItem.removeAllViews();
        savedMarkedList=tinyDB.getListInt("marked");
        //Toast.makeText(thiscontext, ""+savedMarkedList, Toast.LENGTH_SHORT).show();



        for (int i = 0; i < indexarray.length; i++) {
            for (int j = 0; j < recvFileIndex.size(); j++) {
                if (Integer.parseInt(recvFileIndex.get(j)) == Integer.parseInt(indexarray[i])) {
                    index = j;
                    break;
                }
            }


            boolean flag = false;
            for (int k = 0; k < selectedItems.size(); k++) {
                if (index == selectedItems.get(k)) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {


                final LinearLayout card = new LinearLayout(thiscontext);
                //card.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout iconDisplay = new LinearLayout(thiscontext);
                iconDisplay.setOrientation(LinearLayout.VERTICAL);
                ImageView fileicon = new ImageView(thiscontext);
                LinearLayout messageDetails = new LinearLayout(thiscontext);
                messageDetails.setOrientation(LinearLayout.VERTICAL);
                Space spaceTop = new Space(thiscontext);
                final TextView filename = new TextView(thiscontext);
                filename.setText(recvFilename.get(index));
                filename.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
                final TextView filesender = new TextView(thiscontext);
                filesender.setText("From : " + recvFileRecv.get(index));
                //Toast.makeText(thiscontext,recvFilename.get(i).toString(),Toast.LENGTH_LONG).show();
                filesender.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
                TextView timestamp = new TextView(thiscontext);
                timestamp.setText(recvFileTime.get(index));
                timestamp.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
                TextView size = new TextView(thiscontext);
                size.setText(recvFileSize.get(index) + " KB");
                size.setTypeface(Typeface.createFromAsset(thiscontext.getAssets(), "JosefinSans-Regular.ttf"));
                Space spaceBelow = new Space(thiscontext);


                messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                messageDetails.addView(filename, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(filesender, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(timestamp, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(size, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                fileicon.setImageResource(R.drawable.fileicon1);
                fileicon.setPadding(0,0,0,20);
                Space spaceicon1 = new Space(thiscontext);
                Space spaceicon2 = new Space(thiscontext);
                iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                card.setBackgroundResource(R.drawable.listborder);
                if(deSelectAll==true){
                    card.setBackgroundResource(R.drawable.listborder);
                }
                card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                final ImageView markedIndicator=new ImageView(thiscontext);
                markedIndicator.setImageResource(R.drawable.marker);


                savedMarkedList=tinyDB.getListInt("marked");
                boolean markedFlag=false;
                boolean savedFlag=false;
                for(int m=0;m<savedMarkedList.size();m++){
                    if(index==savedMarkedList.get(m)){
                        savedFlag=true;
                        break;
                    }
                }
                for(int l=0;l<markedList.size();l++) {
                    if (index == markedList.get(l)) {
                        markedFlag=true;
                        break;
                    }
                }
                if(savedFlag==true && markedFlag==true){
                    for(int k=0;k<savedMarkedList.size();k++){
                        if(savedMarkedList.get(k)==index){
                            savedMarkedList.remove(k);
                            break;
                        }
                    }
                }
                if(savedFlag==false && markedFlag==true){
                    savedMarkedList.add(index);
                }
                boolean displayFlag=false;
                for(int k=0;k<savedMarkedList.size();k++){
                    if(savedMarkedList.get(k)==index){
                        displayFlag=true;
                        break;
                    }
                }
                if(displayFlag==false) {
                    markedIndicator.setVisibility(View.GONE);
                }else {
                    markedIndicator.setVisibility(View.VISIBLE);
                    card.addView(markedIndicator, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.4f));
                }

                tinyDB.putListInt("marked",savedMarkedList);
                //Toast.makeText(thiscontext, ""+recvAuxFilename.size(), Toast.LENGTH_SHORT).show();
                final int c = index;
                final LinearLayout tCard=card;

                filesender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }
                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {

                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    //tCard.setBackgroundColor(Color.parseColor("#0a0a0a"));
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                                //tCard.setBackgroundColor(Color.parseColor("#1E1F60"));

                            }
                        }
                        monitorSelected();

                    }
                });
                filename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }
                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                                //tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            }
                        }
                        monitorSelected();

                    }
                });
                spaceBelow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }
                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {

                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                spaceTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }
                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {

                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                timestamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }
                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {

                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        }
                            monitorSelected();
                    }
                });
                size.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equals(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));


                            }else {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath="";
                                for(int i=0;i<downloadedFileUri.size();i++){
                                    String filenames[]=downloadedFileUri.get(i).split("%");
                                    if(filenames[0].equals(recvAuxFilename.get(c))){
                                        downloadedPath=filenames[1];
                                        break;
                                    }
                                }
                                File file = new File(downloadedPath);
                                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                intent.setDataAndType(Uri.fromFile(file), type);
                                PackageManager manager=thiscontext.getPackageManager();
                                List<ResolveInfo> info=manager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
                                if(info.isEmpty()){
                                    Toast.makeText(thiscontext,"No app found to open this file",Toast.LENGTH_LONG).show();
                                }else {
                                    thiscontext.startActivity(intent);
                                }
                            }

                            //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {

                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                iconDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (longPress == false) {
                            boolean foundfFlag = checkFilename(recvAuxFilename.get(c));
                            if (foundfFlag == false) {

                                int flag=0;
                                if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpg")){
                                    flag=1;
                                }else if(recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xml")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pdf")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".ppt")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".png")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".xls")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".bmp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".css")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".tif")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".html")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".docm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".pptx")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp4")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".3gp")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".flv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".log")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".jpe")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mp3")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".webm")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".avi")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".mkv")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".dll")
                                        ||recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length()).equalsIgnoreCase(".exe")){
                                    flag=2;
                                }
                                String getExtension=recvFilename.get(c).substring(recvFilename.get(c).indexOf("."),recvFilename.get(c).length());
                                new DownloadFileFromURL(getExtension,c,flag,recvAuxFilename.get(c),keySets.get(c),senderMob.get(c),HomePage.userMobileNo,keyRecv.get(c)).execute(recvFileUri.get(c));



                            }else {

                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String downloadedPath = "";
                                String secureFlag = "";
                                for (int i = 0; i < downloadedFileUri.size(); i++) {
                                    String filenames[] = downloadedFileUri.get(i).split("%");
                                    if (filenames[0].equals(recvAuxFilename.get(c))) {
                                        downloadedPath = filenames[1];
                                        secureFlag = filenames[2];
                                        break;
                                    }
                                }
                                if (secureFlag.equals("true")) {
                                    File file = new File(downloadedPath);
                                    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                                    String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                                    String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                                    intent.setDataAndType(Uri.fromFile(file), type);
                                    PackageManager manager = thiscontext.getPackageManager();
                                    List<ResolveInfo> info = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                                    if (info.isEmpty()) {
                                        Toast.makeText(thiscontext, "No app found to open this file", Toast.LENGTH_LONG).show();
                                    } else {
                                        thiscontext.startActivity(intent);
                                    }
                                }else{
                                    Toast.makeText(Received.thiscontext,"The message has been tampered",Toast.LENGTH_LONG).show();
                                }
                            }
                                //ReadFile(thiscontext,recvAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                iconDisplay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                size.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                spaceBelow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                spaceTop.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                filesender.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                filename.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                timestamp.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=0;
                        //Toast.makeText(thiscontext,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(thiscontext);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (longPress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < selectedItems.size(); i++) {
                                if (selectedItems.get(i) == c) {
                                    selectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                selectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            selectedItems.add(c);
                            longPress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });



                    /*card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //new DownloadFileFromURL(recvFilename.get(c)).execute(recvFileUri.get(c));
                            //String line=ReadFile(thiscontext,recvFilename.get(c));
                            //Toast.makeText(thiscontext,line,Toast.LENGTH_SHORT).show();
                        }
                    });*/

                //intent=new Intent(context,DisplayFile.class);
                //startActivity(intent);

                listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //Toast.makeText(thiscontext, ""+transactionIds, Toast.LENGTH_SHORT).show();

            }else {
                //Toast.makeText(thiscontext, ""+transactionIds, Toast.LENGTH_SHORT).show();
                String key = transactionIds.get(index);
                Toast.makeText(thiscontext, "" + key, Toast.LENGTH_SHORT).show();
                DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/Transactions/" + key);
                deleteRef.removeValue();
                deleteRef.removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        //Toast.makeText(thiscontext, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                for(int k=0;k<downloadedFileUri.size();k++){
                    String filenames[]=downloadedFileUri.get(k).split("%");
                    if(filenames[0].equals(recvAuxFilename.get(index))){
                        downloadedFileUri.remove(k);
                    }
                }
                recvFileTime.remove(index);
                recvFileIndex.remove(index);
                recvAuxFilename.remove(index);
                recvFileUri.remove(index);
                recvFileSize.remove(index);
                recvFileRecv.remove(index);
                recvFilename.remove(index);
                transactionIds.remove(index);
                keySets.remove(index);
                keyRecv.remove(index);
                senderMob.remove(index);


                if (savedMarkedList.size() != 0) {
                    for (int x = 0; x < savedMarkedList.size(); x++) {
                        if (savedMarkedList.get(x) == index)
                            savedMarkedList.remove(x);
                        break;
                    }
                }

            }


        }
        tinyDB.putListString("filenames", recvFilename);
        tinyDB.putListString("filesenders", recvFileRecv);
        tinyDB.putListString("filetimes", recvFileTime);
        tinyDB.putListString("filesizes", recvFileSize);
        tinyDB.putListString("fileindices", recvFileIndex);
        tinyDB.putListString("fileauxnames", recvAuxFilename);
        tinyDB.putListString("fileuris", recvFileUri);
        tinyDB.putListString("transId",transactionIds);
        tinyDB.putListInt("marked",savedMarkedList);
        tinyDB.putListString("keyStore",keySets);
        tinyDB.putListString("keyToDecrypt",keyRecv);
        tinyDB.putListString("senderNumber",senderMob);
        tinyDB.putListString("fileStorageUri",downloadedFileUri);
        Log.d("recvFilename: ",downloadedFileUri.toString());
    }
    protected void decrypt(String extension,int flag,String cipher, String keyset,String sender,String receiver,String keyDec,String filename){
        StoreKeys.rowTrKey.clear();
        StoreKeys.depthTrKey.clear();
        StoreKeys.seqStartDepth.clear();
        StoreKeys.seqStartRow.clear();
        StoreKeys.selectionPoint.clear();
        StoreKeys.selectionKeys.clear();
        StoreKeys.length.clear();
        StoreKeys.sumArray.clear();
        StoreKeys.decGroup.clear();
        StoreKeys.messageDigest.clear();
        StoreKeys.picEncryptKey="";
        long keyToDecrypt=0;
        Received.logDisplay.addLine("Getting keyset.........",Received.thiscontext);
        Log.d("keyset",keyset);
        String keyList[]=keyset.split("<");
        String rowKeys[]=keyList[0].split("%");
        String depthKeys[]=keyList[1].split("%");
        String rowSeq[]=keyList[2].split("%");
        String depthSeq[]=keyList[3].split("%");
        String lengthKey[]=keyList[4].split("%");
        String selectionKey[]=keyList[5].split("%");
        String selectionPoint[]=keyList[6].split("%");
        String sum[]=keyList[7].split("%");
        String decBreak[]=keyList[8].split("%");
        String msgDigests[]=keyList[9].split("%");
        String picKey="";
        if(flag==1 || flag==2){
            picKey=keyList[10];
            StoreKeys.picEncryptKey=picKey;
            System.out.println("PICKEY"+picKey);
        }
        Log.d("svsdv",keyList[1]);


        for (int i=0;i<rowKeys.length;i++){
            //rowKeys[i]=String.valueOf(Long.parseLong(rowKeys[i])^keyToDecrypt);
            StoreKeys.rowTrKey.add(rowKeys[i]);

        }
        for (int i=0;i<depthKeys.length;i++){
            //depthKeys[i]=String.valueOf(Long.parseLong(depthKeys[i])^keyToDecrypt);
            StoreKeys.depthTrKey.add(depthKeys[i]);

        }
        for (int i=0;i<rowSeq.length;i++){
            //rowSeq[i]=String.valueOf(Long.parseLong(rowSeq[i])^keyToDecrypt);
            StoreKeys.seqStartRow.add(Integer.parseInt(rowSeq[i]));

        }
        for (int i=0;i<depthSeq.length;i++){
            //depthSeq[i]=String.valueOf(Long.parseLong(depthSeq[i])^keyToDecrypt);
            StoreKeys.seqStartDepth.add(Integer.parseInt(depthSeq[i]));
        }
        for (int i=0;i<lengthKey.length;i++){
            //lengthKey[i]=String.valueOf(Long.parseLong(lengthKey[i])^keyToDecrypt);
            StoreKeys.length.add(Integer.parseInt(lengthKey[i]));
        }
        for (int i=0;i<selectionKey.length;i++){
            //selectionKey[i]=String.valueOf(Long.parseLong(selectionKey[i])^keyToDecrypt);
            StoreKeys.selectionKeys.add(Integer.parseInt(selectionKey[i]));
        }
        for (int i=0;i<selectionPoint.length;i++){
            //selectionPoint[i]=String.valueOf(Long.parseLong(selectionPoint[i])^keyToDecrypt);
            StoreKeys.selectionPoint.add(Integer.parseInt(selectionPoint[i]));
        }
        for(int i=0;i<sum.length;i++){
            StoreKeys.sumArray.add(sum[i]);
        }
        for(int i=0;i<decBreak.length;i++){
            StoreKeys.decGroup.add(Integer.parseInt(decBreak[i]));
        }
        for(int i=0;i<msgDigests.length;i++){
            StoreKeys.messageDigest.add(Integer.parseInt(msgDigests[i]));
        }
        if(flag==0) {
            InvokeDecryption invokeDecryption = new InvokeDecryption();
            try {
                String plainText = invokeDecryption.decryptFeeder(cipher, sender, receiver, keyDec);
                PrintWriter printWriter = new PrintWriter("/sdcard/SendSec/Downloads/" + filename);
                printWriter.print("");
                printWriter.close();
                FileOperations.writeFile("/sdcard/SendSec/Downloads/" + filename, plainText);
                downloadedFileUri.add(filename+"%"+"/sdcard/SendSec/Downloads/" + filename+"%"+InvokeDecryption.securelyReceived);
                tinyDB.putListString("fileStorageUri",downloadedFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(flag==1){
            Log.d("IMAGE","decrytpyiom");
            InvokeDecryption invokeDecryption = new InvokeDecryption();
            try {
                String plainText = invokeDecryption.decryptFeeder(picKey, sender, receiver, keyDec);
                System.out.println("DecKEy "+plainText);
                String words[]=cipher.split(" ");
                String lines[]=cipher.split("\n");
                int rows=lines.length;
                int columns=(words.length-1)/rows;
                Log.d("SIZE",rows+"  "+columns);
                String bitmap[][]=DecryptPic.generateArray(cipher,rows,columns);
                String n[][]=EncryptPic.decodeWithRandom(bitmap,plainText,rows,columns);
                for(int i=0;i<rows;i++){
                    for(int j=0;j<columns;j++){
                        System.out.print(n[i][j]+" ");
                    }
                    System.out.println();
                }
                DecryptPic.getDecPic(n,rows,columns,filename,extension);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else if(flag==2){
            String timestamp=String.valueOf(System.currentTimeMillis());
            InvokeDecryption invokeDecryption = new InvokeDecryption();
            String plainText = null;
            try {
                plainText = invokeDecryption.decryptFeeder(picKey, sender, receiver, keyDec);
                System.out.println("DecKEy "+plainText);
                System.out.print(cipher);
                String encryptedBits[]=cipher.split(" ");
                String originalBitStream="";
                System.out.println("LENGTH"+encryptedBits.length);
                originalBitStream=ParallelXOR.getData(encryptedBits,plainText);
                /*for(int i=0;i<encryptedBits.length-1;i++){
                    System.out.println(i);
                    originalBitStream+=OtherFileHandler.decBinaryXor(encryptedBits[i],plainText)+" ";
                }*/
                //System.out.println(originalBitStream);

                OtherFileHandler.getFileByte("/sdcard/SendSec/Downloads/"+timestamp+extension,originalBitStream );
                downloadedFileUri.add(filename+"%"+"/sdcard/SendSec/Downloads/" + timestamp+extension.toLowerCase()+"%"+InvokeDecryption.securelyReceived);
                tinyDB.putListString("fileStorageUri",downloadedFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}




class DownloadFileFromURL extends AsyncTask<String, String, String> {
String filename;
String keyDecrypt,senderNo,receiverNo,keyRecv,extension;
int index;
int flag;
    public DownloadFileFromURL(String extension,int c,int flag,String filename,String keyset,String sender,String receiver,String keyDec) {
        this.filename=filename;
        keyDecrypt=keyset;
        senderNo=sender;
        receiverNo=receiver;
        keyRecv=keyDec;
        this.flag=flag;
        index=c;
        this.extension=extension;
    }

    /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Received.progressDialog.show();
            Received.progressBar.setMax(100);
            Received.progressBar.setProgress(0);
            Received.progressValue.setText("Downloading file.....\n0% completed");
            //showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File direct = new File(Environment.getExternalStorageDirectory() + "/SendSec/Downloads");

                if (!direct.exists()) {
                    File wallpaperDirectory = new File("/sdcard/SendSec/Downloads");
                    wallpaperDirectory.mkdirs();
                }

                // Output stream
                OutputStream output = new FileOutputStream("/sdcard/SendSec/Downloads/"+filename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    //Received.setProgress(total,lenghtOfFile);
                    publishProgress(String.valueOf((int)((total*100)/lenghtOfFile)));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
            Received.progressBar.setProgress(Integer.parseInt(progress[0]));
            Received.progressValue.setText("Downloading file.....\n"+Integer.parseInt(progress[0])+"% completed");
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            Received.logDisplay.createDialog(Received.thiscontext);
            Received.logDisplay.logHeader.setText("Decrypting file....");
            Received.logDisplay.addLine("Decryption starting.......",Received.thiscontext);
            Received.progressDialog.dismiss();
            String contents=DisplayFile.ReadFile(filename);


            if(flag==1 || flag==2){
                System.out.println(contents);
                Received.logDisplay.addLine("File contents: ",Received.thiscontext);
                Received.logDisplay.addLine(contents,Received.thiscontext);
                Received received=new Received();
                received.decrypt(extension,flag,contents,keyDecrypt,senderNo,receiverNo,keyRecv,filename);

                /*String bitmap[][]=DecryptPic.generateArray(contents,rows,columns);
                try {
                    DecryptPic.getDecPic(bitmap,rows,columns);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/
            }else {
                Log.d("Encrypted received", contents);
                Received received = new Received();
                received.decrypt(extension,0,contents, keyDecrypt, senderNo, receiverNo, keyRecv,filename);
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String downloadedPath="";
            String secureFlag="";
            for(int i=0;i<Received.downloadedFileUri.size();i++){
                String filenames[]=Received.downloadedFileUri.get(i).split("%");
                if(filenames[0].equals(Received.recvAuxFilename.get(index))){
                    downloadedPath=filenames[1];
                    secureFlag=filenames[2];
                    break;
                }
            }
            Log.d("SECURE",secureFlag);
            if(secureFlag.equals("true")) {
                File file = new File(downloadedPath);
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String ext = file.getName().substring(file.getName().indexOf(".") + 1);
                String type = mimeTypeMap.getMimeTypeFromExtension(ext);
                intent.setDataAndType(Uri.fromFile(file), type);
                PackageManager manager = Received.thiscontext.getPackageManager();
                List<ResolveInfo> info = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

                if (info.isEmpty()) {
                    Toast.makeText(Received.thiscontext, "No app found to open this file", Toast.LENGTH_LONG).show();
                } else {
                    Received.thiscontext.startActivity(intent);
                }
            }else{
                Toast.makeText(Received.thiscontext,"The message has been tampered",Toast.LENGTH_LONG).show();
            }

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            //String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.txt";
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }


