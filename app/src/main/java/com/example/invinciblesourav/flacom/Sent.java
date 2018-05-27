package com.example.invinciblesourav.flacom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Sent extends Fragment {
    LinearLayout listingItem;
    static View fragmentViewSent;
    static TinyDB tinyDBsent;
    public static ArrayList<String> sentFileStat=new ArrayList<String>();
    public static ArrayList<String> sentFilename = new ArrayList<String>();
    public static ArrayList<String> sentFileRecv = new ArrayList<String>();
    public static ArrayList<String> sentFileTime = new ArrayList<String>();
    public static ArrayList<String> sentFileSize = new ArrayList<String>();
    public static ArrayList<String> sentFileUri = new ArrayList<>();
    public static ArrayList<String> sentAuxFilename=new ArrayList<>();
    public static ArrayList<Integer> sentSavedMarkList=new ArrayList<>();
    public static ArrayList<Integer> sentMarkedMarkList=new ArrayList<>();
    public static ArrayList<Integer> senttransactionIds=new ArrayList<>();
    public static ArrayList<String> sentFileIndex=new ArrayList<>();
    public static Context scontext;
    public static boolean sentLongpress=false;
    public static boolean sentDeSelectAll=false;
    ArrayList<String> ContactNames=new ArrayList<>();
    ArrayList<String> ContactNumbers=new ArrayList<>();
    //Context context;
    public static ArrayList<Integer> sentSelectedItems=new ArrayList<>();
    DatabaseReference databaseReference;
    int recordCount;
    static View sheetview;
    static BottomSheetDialog bottomSheetDialog;
    static int screenHeight;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentViewSent = inflater.inflate(R.layout.activity_sent, container, false);

       scontext=getContext();
        sentFilename.clear();
        sentFileRecv.clear();
        sentFileTime.clear();
        sentFileSize.clear();
        sentFileUri.clear();
        sentFileStat.clear();
        sentFileIndex.clear();
        sentAuxFilename.clear();
        senttransactionIds.clear();

        tinyDBsent = new TinyDB(getContext());
        //tinyDBsent.clear();
        sentFilename = tinyDBsent.getListString("sentfilename");
        sentFileRecv = tinyDBsent.getListString("sentfilerecv");
        sentFileTime = tinyDBsent.getListString("sentfiletime");
        sentFileSize = tinyDBsent.getListString("sentfilesize");
        sentFileUri = tinyDBsent.getListString("sentfileuri");
        sentFileStat = tinyDBsent.getListString("sentfilestat");
        sentFileIndex = tinyDBsent.getListString("sentfileindex");
        sentAuxFilename = tinyDBsent.getListString("sentfileaux");
        senttransactionIds=tinyDBsent.getListInt("senttransID");


        displayListSent(Sent.this.getContext());



        


        return fragmentViewSent;

        }
    protected void displayListSent(final Context context){
        //Toast.makeText(context, ""+sentFileUri, Toast.LENGTH_SHORT).show();
        int index = 0;
        LinearLayout listingItem = (LinearLayout) fragmentViewSent.findViewById(R.id.sent_holder);
        sentSavedMarkList=tinyDBsent.getListInt("markedSent");
        //Toast.makeText(context, "Sent "+sentSavedMarkList, Toast.LENGTH_SHORT).show();
        if(sentFilename.size()==0){
            Toast.makeText(context, "zdiu"+sentFilename.size(), Toast.LENGTH_SHORT).show();
            sentSavedMarkList.clear();
            tinyDBsent.putListInt("markedSent",sentSavedMarkList);
        }
        listingItem.removeAllViews();
        for (int i = sentFilename.size()-1; i >= 0; i--) {
            index=i;
            boolean flag = false;
            for (int k = 0; k < sentSelectedItems.size(); k++) {
                if (index == sentSelectedItems.get(k)) {
                    flag = true;
                    break;
                }
            }
            //Toast.makeText(context, "Derp "+index, Toast.LENGTH_SHORT).show();
            if (flag == false) {


                final LinearLayout card = new LinearLayout(context);
                //card.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout iconDisplay = new LinearLayout(context);
                iconDisplay.setOrientation(LinearLayout.VERTICAL);
                ImageView fileicon = new ImageView(context);
                LinearLayout messageDetails = new LinearLayout(context);
                messageDetails.setOrientation(LinearLayout.VERTICAL);
                Space spaceTop = new Space(context);
                TextView filename = new TextView(context);
                filename.setText(sentFilename.get(index));
                filename.setTypeface(Typeface.createFromAsset(context.getAssets(), "JosefinSans-Regular.ttf"));
                final TextView filesender = new TextView(context);
                filesender.setText("To : " + sentFileRecv.get(index));
                //Toast.makeText(context,sentFilename.get(i).toString(),Toast.LENGTH_LONG).show();
                filesender.setTypeface(Typeface.createFromAsset(context.getAssets(), "JosefinSans-Regular.ttf"));
                TextView timestamp = new TextView(context);
                timestamp.setText(sentFileTime.get(index));
                timestamp.setTypeface(Typeface.createFromAsset(context.getAssets(), "JosefinSans-Regular.ttf"));
                TextView size = new TextView(context);
                size.setText(sentFileSize.get(index) + " KB");
                size.setTypeface(Typeface.createFromAsset(context.getAssets(), "JosefinSans-Regular.ttf"));
                Space spaceBelow = new Space(context);


                messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                messageDetails.addView(filename, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(filesender, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(timestamp, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(size, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                fileicon.setImageResource(R.drawable.fileicon1);
                fileicon.setPadding(0,0,0,20);
                Space spaceicon1 = new Space(context);
                Space spaceicon2 = new Space(context);
                iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                card.setBackgroundResource(R.drawable.listborder);
                if(sentDeSelectAll==true){
                    card.setBackgroundResource(R.drawable.listborder);
                }
                card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                final ImageView markedIndicator=new ImageView(context);
                markedIndicator.setImageResource(R.drawable.marker);
                sentSavedMarkList.clear();
                sentSavedMarkList=tinyDBsent.getListInt("markedSent");
                boolean markedFlag=false;
                boolean savedFlag=false;
                for(int m=0;m<sentSavedMarkList.size();m++){
                    if(index==sentSavedMarkList.get(m)){
                        savedFlag=true;
                        break;
                    }
                }
                for(int l=0;l<sentMarkedMarkList.size();l++) {
                    if (index == sentMarkedMarkList.get(l)) {
                        markedFlag=true;
                    }
                }
                //Toast.makeText(context, "Marked: "+sentMarkedMarkList, Toast.LENGTH_SHORT).show();
                if(savedFlag==true && markedFlag==true){
                    for(int k=0;k<sentSavedMarkList.size();k++){
                        if(sentSavedMarkList.get(k)==index){
                            sentSavedMarkList.remove(k);
                            break;
                        }
                    }
                }
                if(savedFlag==false && markedFlag==true){
                    sentSavedMarkList.add(index);
                }
                boolean displayFlag=false;
                for(int k=0;k<sentSavedMarkList.size();k++){
                    if(sentSavedMarkList.get(k)==index){
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

                tinyDBsent.putListInt("markedSent",sentSavedMarkList);
                //Toast.makeText(context, ""+sentAuxFilename.size(), Toast.LENGTH_SHORT).show();
                final int c = index;
                final LinearLayout tCard=card;

                filesender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (sentLongpress == false) {

                                Intent intent = new Intent(context, DisplayFile.class);
                                intent.putExtra("filename", sentAuxFilename.get(c));
                                intent.putExtra("filerecvname", sentFilename.get(c));
                                intent.putExtra("filesender", sentFileRecv.get(c));
                                intent.putExtra("filetime", sentFileTime.get(c));
                                //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                                //ReadFile(context,sentAuxFilename.get(c));

                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {

                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    //tCard.setBackgroundColor(Color.parseColor("#0a0a0a"));
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                                //tCard.setBackgroundColor(Color.parseColor("#1E1F60"));

                            }
                        }
                        monitorSelected();

                    }
                });
                filename.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                                //tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            }
                        }
                        monitorSelected();

                    }
                });
                spaceBelow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {

                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                spaceTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {

                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                timestamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {

                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                size.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {

                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                iconDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (sentLongpress == false) {

                            Intent intent = new Intent(context, DisplayFile.class);
                            intent.putExtra("filename", sentAuxFilename.get(c));
                            intent.putExtra("filerecvname", sentFilename.get(c));
                            intent.putExtra("filesender", sentFileRecv.get(c));
                            intent.putExtra("filetime", sentFileTime.get(c));
                            //Toast.makeText(context, "jjj", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                            //ReadFile(context,sentAuxFilename.get(c));
                        } else {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        }
                        monitorSelected();
                    }
                });
                iconDisplay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                size.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                spaceBelow.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                spaceTop.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                filesender.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                filename.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });
                timestamp.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        HomePage.pressedCaller=1;
                        //Toast.makeText(context,"Clicked for long",Toast.LENGTH_SHORT).show();
                                /*bottomSheetDialog = new BottomSheetDialog(context);
                                sheetview = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottomdialog, null);
                                bottomSheetDialog.setContentView(sheetview);
                                BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) sheetview.getParent());
                                bottomSheetBehavior.setPeekHeight(1000);
                                bottomSheetDialog.show();*/
                        if (sentLongpress == true) {
                            boolean flagSelected = false;
                            for (int i = 0; i < sentSelectedItems.size(); i++) {
                                if (sentSelectedItems.get(i) == c) {
                                    sentSelectedItems.remove(i);
                                    tCard.setBackgroundResource(R.drawable.listborder);
                                    flagSelected = true;
                                    break;
                                }
                            }
                            if (flagSelected == false) {
                                tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                                sentSelectedItems.add(c);
                            }
                        } else {
                            tCard.setBackgroundColor(Color.parseColor("#1E1F60"));
                            HomePage.bottomPopUp.setVisibility(View.VISIBLE);
                            HomePage.viewFlipperSelector.setVisibility(View.GONE);
                            sentSelectedItems.add(c);
                            sentLongpress = true;
                        }
                        monitorSelected();
                        return true;
                    }
                });



                    /*card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //new DownloadFileFromURL(sentFilename.get(c)).execute(sentFileUri.get(c));
                            //String line=ReadFile(context,sentFilename.get(c));
                            //Toast.makeText(context,line,Toast.LENGTH_SHORT).show();
                        }
                    });*/

                //intent=new Intent(context,DisplayFile.class);
                //startActivity(intent);

                listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //Toast.makeText(context, ""+transactionIds, Toast.LENGTH_SHORT).show();
                tinyDBsent.putListString("sentfilename",sentFilename);
                tinyDBsent.putListString("sentfilerecv",sentFileRecv );
                tinyDBsent.putListString("sentfiletime",sentFileTime);
                tinyDBsent.putListString("sentfilesize",sentFileSize);
                tinyDBsent.putListString("sentfileuri",sentFileUri);
                tinyDBsent.putListString("sentfilestat",sentFileStat);
                tinyDBsent.putListString("sentfileindex",sentFileIndex);
                tinyDBsent.putListString("sentfileaux",sentAuxFilename);
                tinyDBsent.putListInt("senttransID",senttransactionIds);
                tinyDBsent.putListInt("markedSent",sentSavedMarkList);


            }else {
                //Toast.makeText(context, ""+transactionIds, Toast.LENGTH_SHORT).show();

                sentFilename.remove(index);
                sentFileRecv.remove(index);
                sentFileTime.remove(index);
                sentFileSize.remove(index);
                sentFileUri.remove(index);
                sentFileStat.remove(index);
                sentFileIndex.remove(index);
                sentAuxFilename.remove(index);
                senttransactionIds.remove(index);

                if(sentSavedMarkList.size()!=0) {
                    for(int x=0;x<sentSavedMarkList.size();x++){
                        if(index==sentSavedMarkList.get(x)){
                            sentSavedMarkList.remove(x);
                            break;
                        }
                    }

                }
                tinyDBsent.putListString("sentfilename",sentFilename);
                tinyDBsent.putListString("sentfilerecv",sentFileRecv );
                tinyDBsent.putListString("sentfiletime",sentFileTime);
                tinyDBsent.putListString("sentfilesize",sentFileSize);
                tinyDBsent.putListString("sentfileuri",sentFileUri);
                tinyDBsent.putListString("sentfilestat",sentFileStat);
                tinyDBsent.putListString("sentfileindex",sentFileIndex);
                tinyDBsent.putListString("sentfileaux",sentAuxFilename);
                tinyDBsent.putListInt("senttransID",senttransactionIds);
                tinyDBsent.putListInt("markedSent",sentSavedMarkList);



            }


        }
    }

    public void monitorSelected(){
        if(sentSelectedItems.size()>1){
            HomePage.moreInfo.setAlpha(0.3f);
            HomePage.moreInfo.setEnabled(false);
        }else{
            HomePage.moreInfo.setAlpha(1.0f);
            HomePage.moreInfo.setEnabled(true);
        }
    }
    public void deselect(Context context){

        sentDeSelectAll=true;
        displayListSent(context);
        sentDeSelectAll=false;

    }

    public void marker(Context context){

        for(int i=0;i<sentSelectedItems.size();i++){
            sentMarkedMarkList.add(sentSelectedItems.get(i));
        }

        sentSelectedItems.clear();
        displayListSent(context);




    }
    public void deleteRecord(Context context){

        //Toast.makeText(Received.thiscontext, ""+selectedItems.toString(), Toast.LENGTH_SHORT).show();

        displayListSent(context);

        sentSelectedItems.clear();
    }
    public void forward() {


        int index=0;
        for (int i = sentFilename.size()-1; i >=0; i--) {

                    index = i;


            for (int k = 0; k < sentSelectedItems.size(); k++) {
                if (index == sentSelectedItems.get(k)) {
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

        pref = scontext.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        Random r = new Random();
        int tranasactionId = r.nextInt(1000000000);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String time = dateFormat.format(calendar.getTime());
        String size=sentFileSize.get(index);

        String filename=sentFilename.get(index);

        long sentCount= Long.parseLong(pref.getString("sentCount", "0"));
        //Calendar calendar = Calendar.getInstance();
        String downloadURi = sentFileUri.get(index);
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
    
}


