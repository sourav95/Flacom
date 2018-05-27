package com.example.invinciblesourav.flacom;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.Space;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import android.Manifest;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.xml.transform.Templates;

import static android.app.Activity.RESULT_OK;

public class AddressBook extends Fragment {
    static View fragmentBook;
    ListView listView;
    ArrayList<String> StoreContactNames;
    ArrayList<String> StoreContactNumber;
    ArrayList<String> RawContactNames;
    ArrayList<String> RawContactNumber;
    ArrayList<String> SearchNumbers;
    ArrayList<String> SearchNames;
    ArrayAdapter<String> arrayAdapter;
    Cursor cursor;
    String name, phonenumber;
    String[] nonDup;
    String[] nonDup2;
    public static final int RequestPermissionCode = 1;
    android.support.v7.widget.SearchView editsearch;
    static ArrayList<String> fetchedMobile;
    int children;
    LinearLayout listingItem;
    LayoutInflater inflater;
    public static AlertDialog filePrompt;
    static Uri filePath;
    static String size, time, filename;
    EditText getReceiverName;
    String nameToSend = "", numberToSend = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBook = inflater.inflate(R.layout.activity_address_book, container, false);


        fetchedMobile = new ArrayList<String>();

        fetchContacts();

        StoreContactNames = new ArrayList<String>();

        StoreContactNumber = new ArrayList<String>();

        RawContactNames = new ArrayList<>();

        RawContactNumber = new ArrayList<>();

        SearchNames = new ArrayList<>();

        SearchNumbers = new ArrayList<>();

        StoreContactNames.clear();

        StoreContactNumber.clear();

        RawContactNumber.clear();

        RawContactNames.clear();

        SearchSender.ContactsNames.clear();

        SearchSender.ContactsNumbers.clear();

        //EnableRuntimePermission();


        GetContactsIntoArrayList();
        //System.out.println("cccc"+StoreContactNames.toString());

        removeDuplicates();

        reformArrayList();

        children = 0;

        rearrangeContacts();

        listingItem = (LinearLayout) fragmentBook.findViewById(R.id.addbook_holder);
        printContacts();
        editsearch = (android.support.v7.widget.SearchView) fragmentBook.findViewById(R.id.address_serach);
        TextView searchText = (TextView) editsearch.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchText.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf"));
        searchText.setTextSize(16);
        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                filter(text);
                return false;
            }
        });

        Button refreshButton=(Button) fragmentBook.findViewById(R.id.refreshbutton);
        refreshButton.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                RawContactNames.clear();
                RawContactNumber.clear();
                StoreContactNumber.clear();
                StoreContactNames.clear();
                SearchNames.clear();
                SearchNumbers.clear();
                SearchSender.ContactsNames.clear();
                SearchSender.ContactsNumbers.clear();
                GetContactsIntoArrayList();
                removeDuplicates();

                reformArrayList();

                children = 0;

                rearrangeContacts();

                printContacts();

                Toast.makeText(getContext(),"Contacts refreshed",Toast.LENGTH_LONG).show();
            }
        });
        return fragmentBook;
    }
    private String removeSpaces(String number){
        String phone="";
        if(number.startsWith("0")){
            number=number.substring(1,number.length());
        }
        for(int i=0;i<number.length();i++){
            if(number.charAt(i)!=' '){
                phone+=number.charAt(i);
            }
        }
        return phone;
    }
    public void GetContactsIntoArrayList() {


        cursor = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {

            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phonenumber=removeSpaces(phonenumber);
            Log.d("Phone Number",phonenumber);

            if(phonenumber.matches("^[0-9]*$")||(phonenumber.startsWith("+91")&&phonenumber.substring(3,phonenumber.length()).matches("^[0-9]*$"))) {
                StoreContactNames.add(name);

                RawContactNames.add(name);

                StoreContactNumber.add(phonenumber);

                RawContactNumber.add(phonenumber);

                Log.d("Phone Number","true");
            }

        }

        cursor.close();


    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(),
                Manifest.permission.READ_CONTACTS)) {

            Toast.makeText(getActivity(), "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    public void removeDuplicates() {
        nonDup = new String[StoreContactNumber.size()];
        nonDup2 = new String[StoreContactNames.size()];
        for (int i = 0; i < StoreContactNumber.size(); i++) {
            String number = StoreContactNumber.get(i);
            String trimmedString = "";
            if (number.startsWith("+91")) {
                for (int k = 3; k < number.length(); k++) {
                    if (number.charAt(k) != ' ') {
                        trimmedString = trimmedString + String.valueOf(number.charAt(k));
                    }
                }
                nonDup[i] = trimmedString;
            } else {
                for (int k = 0; k < number.length(); k++) {
                    if (number.charAt(k) != ' ') {
                        trimmedString = trimmedString + String.valueOf(number.charAt(k));
                    }
                }
                nonDup[i] = trimmedString;
            }

        }
        for (int i = 0; i < StoreContactNames.size(); i++) {
            nonDup2[i] = StoreContactNames.get(i);
        }
        for (int i = 0; i < nonDup.length; i++) {
            for (int j = i + 1; j < nonDup.length; j++) {
                if (Long.parseLong(nonDup[i]) == Long.parseLong(nonDup[j])) {
                    nonDup[j] = "-999";
                    nonDup2[j] = "-999";
                }
            }
        }

    }

    public void reformArrayList() {
        StoreContactNames.clear();
        StoreContactNumber.clear();
        for (int i = 0; i < nonDup.length; i++) {
            if (!nonDup[i].equalsIgnoreCase("-999")) {
                for (int k = 0; k < RawContactNumber.size(); k++) {
                    String number = RawContactNumber.get(k);
                    String trimmedString = "";
                    for (int l = 0; l < number.length(); l++) {
                        if (number.charAt(l) != ' ') {
                            trimmedString = trimmedString + String.valueOf(number.charAt(l));
                        }
                    }
                    if (trimmedString.startsWith("+91")) {
                        if (Long.parseLong(trimmedString.substring(3)) == Long.parseLong(nonDup[i])) {
                            StoreContactNumber.add(nonDup[i]);
                            StoreContactNames.add(RawContactNames.get(k));
                            break;
                        }
                    } else {
                        if (Long.parseLong(trimmedString) == Long.parseLong(nonDup[i])) {
                            StoreContactNumber.add(nonDup[i]);
                            StoreContactNames.add(RawContactNames.get(k));
                            break;

                        }
                    }
                }
            }
        }
    }

    public boolean checkAvailable(String mobile) {
        for (int j = 0; j < fetchedMobile.size(); j++) {
            String compMob = fetchedMobile.get(j).substring(3);

            if (Long.parseLong(mobile) == Long.parseLong(compMob)) {

                return true;
            }

        }
        return false;
    }

    public void rearrangeContacts() {
        String names[] = new String[StoreContactNames.size()];
        String numbers[] = new String[StoreContactNumber.size()];
        for (int i = 0; i < StoreContactNames.size(); i++) {
            names[i] = StoreContactNames.get(i);
        }
        for (int i = 0; i < StoreContactNumber.size(); i++) {
            numbers[i] = StoreContactNumber.get(i);
        }
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < names.length - i - 1; j++) {
                if (names[j].compareTo(names[j + 1]) > 0) {
                    String temp = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = temp;

                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;

                }
            }
        }
        StoreContactNames.clear();
        StoreContactNumber.clear();
        for (int i = 0; i < names.length; i++) {
            StoreContactNames.add(names[i]);
        }
        for (int i = 0; i < numbers.length; i++) {
            StoreContactNumber.add(numbers[i]);
        }
    }

    public void fetchContacts() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                children++;
                fetchedMobile.add(dataSnapshot.child("mobile").getValue(String.class));
                if (children == dataSnapshot.getChildrenCount()) {
                    printContacts();
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

    public void printContacts() {
        listingItem.removeAllViews();
        for (int i = 0; i < StoreContactNames.size(); i++) {
            boolean avalFlag = false;
            LinearLayout card = new LinearLayout(this.getContext());
            card.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout iconDisplay = new LinearLayout(this.getContext());
            iconDisplay.setOrientation(LinearLayout.VERTICAL);
            ImageView fileicon = new ImageView(this.getContext());
            LinearLayout messageDetails = new LinearLayout(this.getContext());
            messageDetails.setOrientation(LinearLayout.VERTICAL);
            Space spaceTop = new Space(this.getContext());
            TextView contactname = new TextView(this.getContext());
            contactname.setText(StoreContactNames.get(i));
            contactname.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
            TextView contactImei = new TextView(this.getContext());
            if (checkAvailable(StoreContactNumber.get(i))) {
                contactImei.setText(StoreContactNumber.get(i));
                //contactImei.setTextColor(Color.RED);
                avalFlag = true;
                SearchSender.ContactsNames.add(StoreContactNames.get(i));
                SearchSender.ContactsNumbers.add(StoreContactNumber.get(i));
            } else {
                contactImei.setText(StoreContactNumber.get(i));
            }
            contactImei.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
            Space spaceBelow = new Space(this.getContext());


            messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
            messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

            fileicon.setImageResource(R.drawable.contactsicon);
            Space spaceicon1 = new Space(this.getContext());
            Space spaceicon2 = new Space(this.getContext());
            iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
            iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
            card.setBackgroundResource(R.drawable.listborder);
            card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            //ImageButton menu=new ImageButton(this.getContext());
            //menu.setImageResource(R.drawable.addpeople);
            //menu.setBackgroundColor(Color.parseColor("#00ffffff"));
            final Button sendInvite = new Button(this.getContext());
            sendInvite.setText("Invite");
            sendInvite.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
            if (avalFlag == true) {
                sendInvite.setText("Send");
            }
            final int temp = i;
            final boolean finalAvalFlag = avalFlag;

            final int finalI = i;
            sendInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalAvalFlag == false) {
                        PackageManager packageManager = getContext().getPackageManager();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + "91" + StoreContactNumber.get(temp) + "&text=" + URLEncoder.encode("This is a test message for our app.Please ignore.", "UTF-8");
                            intent.setPackage("com.whatsapp");
                            intent.setData(Uri.parse(url));
                            getContext().startActivity(intent);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        nameToSend = StoreContactNames.get(finalI);
                        numberToSend = StoreContactNumber.get(finalI);
                        HomePage.recvNum=numberToSend;
                        HomePage.recvName=nameToSend;
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setType("*/*");
                        getActivity().startActivityForResult(intent, 9);

                        //HomePage.filePrompt.show();


                    }
                }
            });
            card.addView(sendInvite, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.4f));
            listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        }

    }

    public void filter(String text) {
        listingItem.removeAllViews();
        for (int i = 0; i < StoreContactNames.size(); i++) {
            if (StoreContactNames.get(i).toLowerCase().contains(text.toLowerCase())) {
                //Toast.makeText(getContext(),StoreContactNames.get(i),Toast.LENGTH_SHORT).show();
                boolean avalFlag = false;
                LinearLayout card = new LinearLayout(this.getContext());
                card.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout iconDisplay = new LinearLayout(this.getContext());
                iconDisplay.setOrientation(LinearLayout.VERTICAL);
                ImageView fileicon = new ImageView(this.getContext());
                LinearLayout messageDetails = new LinearLayout(this.getContext());
                messageDetails.setOrientation(LinearLayout.VERTICAL);
                Space spaceTop = new Space(this.getContext());
                TextView contactname = new TextView(this.getContext());
                contactname.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
                TextView contactImei = new TextView(this.getContext());
                SpannableString spannable;
                if (checkAvailable(StoreContactNumber.get(i))) {

                    avalFlag = true;
                }
                int pos = StoreContactNames.get(i).toLowerCase().indexOf(text.toLowerCase());
                spannable = new SpannableString(StoreContactNames.get(i));
                spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                contactname.setText(spannable);
                contactImei.setText(StoreContactNumber.get(i));
                contactImei.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
                Space spaceBelow = new Space(this.getContext());


                messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                fileicon.setImageResource(R.drawable.contactsicon);
                Space spaceicon1 = new Space(this.getContext());
                Space spaceicon2 = new Space(this.getContext());
                iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                card.setBackgroundResource(R.drawable.listborder);
                card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                //ImageButton menu=new ImageButton(this.getContext());
                //menu.setImageResource(R.drawable.addpeople);
                //menu.setBackgroundColor(Color.parseColor("#00ffffff"));
                Button sendInvite = new Button(this.getContext());
                sendInvite.setText("Invite");
                if (avalFlag == true) {
                    sendInvite.setText("Send");
                }
                final int temp = i;
                final boolean finalAvalFlag = avalFlag;

                final int finalI = i;
                sendInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (finalAvalFlag == false) {
                            PackageManager packageManager = getContext().getPackageManager();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            try {
                                String url = "https://api.whatsapp.com/send?phone=" + "91" + StoreContactNumber.get(temp) + "&text=" + URLEncoder.encode("This is a test message for our app.Please ignore.", "UTF-8");
                                intent.setPackage("com.whatsapp");
                                intent.setData(Uri.parse(url));
                                getContext().startActivity(intent);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        } else {
                            nameToSend = StoreContactNames.get(finalI);
                            numberToSend = StoreContactNumber.get(finalI);
                            HomePage.recvNum=numberToSend;
                            HomePage.recvName=nameToSend;
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("*/*");
                            getActivity().startActivityForResult(intent, 9);

                            //HomePage.filePrompt.show();


                        }

                    }
                });
                card.addView(sendInvite, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.4f));
                listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        }
        for (int i = 0; i < StoreContactNumber.size(); i++) {
            if (StoreContactNumber.get(i).toLowerCase().contains(text)) {
                //Toast.makeText(getContext(),StoreContactNames.get(i),Toast.LENGTH_SHORT).show();
                boolean avalFlag = false;
                LinearLayout card = new LinearLayout(this.getContext());
                card.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout iconDisplay = new LinearLayout(this.getContext());
                iconDisplay.setOrientation(LinearLayout.VERTICAL);
                ImageView fileicon = new ImageView(this.getContext());
                LinearLayout messageDetails = new LinearLayout(this.getContext());
                messageDetails.setOrientation(LinearLayout.VERTICAL);
                Space spaceTop = new Space(this.getContext());
                TextView contactname = new TextView(this.getContext());
                contactname.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
                TextView contactImei = new TextView(this.getContext());
                SpannableString spannable;
                if (checkAvailable(StoreContactNumber.get(i))) {

                    avalFlag = true;
                }
                int pos = StoreContactNumber.get(i).toLowerCase().indexOf(text.toLowerCase());
                spannable = new SpannableString(StoreContactNumber.get(i));
                spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                contactname.setText(StoreContactNames.get(i));
                contactImei.setText(spannable);
                contactImei.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "JosefinSans-Regular.ttf"));
                Space spaceBelow = new Space(this.getContext());


                messageDetails.addView(spaceTop, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
                messageDetails.addView(contactname, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(contactImei, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
                messageDetails.addView(spaceBelow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

                fileicon.setImageResource(R.drawable.contactsicon);
                Space spaceicon1 = new Space(this.getContext());
                Space spaceicon2 = new Space(this.getContext());
                iconDisplay.addView(spaceicon1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30));
                iconDisplay.addView(fileicon, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                iconDisplay.addView(spaceicon2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                card.addView(iconDisplay, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.3f));
                card.setBackgroundResource(R.drawable.listborder);
                card.addView(messageDetails, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
                //ImageButton menu=new ImageButton(this.getContext());
                //menu.setImageResource(R.drawable.addpeople);
                //menu.setBackgroundColor(Color.parseColor("#00ffffff"));
                Button sendInvite = new Button(this.getContext());
                sendInvite.setText("Invite");
                if (avalFlag == true) {
                    sendInvite.setText("Send");
                }
                final int temp = i;
                sendInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PackageManager packageManager = getContext().getPackageManager();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        try {
                            String url = "https://api.whatsapp.com/send?phone=" + "91" + StoreContactNumber.get(temp) + "&text=" + URLEncoder.encode("This is a test message for our app.Please ignore.", "UTF-8");
                            intent.setPackage("com.whatsapp");
                            intent.setData(Uri.parse(url));
                            getContext().startActivity(intent);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                });
                card.addView(sendInvite, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.4f));
                listingItem.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("fff","dafsdfsd");
        switch (requestCode) {
            case 9:
                if (resultCode == RESULT_OK) {
                    Log.d("fff","dafsdfsd");
                }
        }
    }
}
