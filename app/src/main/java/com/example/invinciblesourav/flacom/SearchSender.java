package com.example.invinciblesourav.flacom;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchSender extends AppCompatActivity {
    SearchView searchReceiver;
    static LinearLayout displayReceiever;
    public static ArrayList<String> ContactsNames = new ArrayList<>();
    public static ArrayList<String> ContactsNumbers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sender);
        displayReceiever = (LinearLayout) findViewById(R.id.avallist);

        printList();
        //Toast.makeText(this, String.valueOf(ContactsNames.size()), Toast.LENGTH_SHORT).show();
        searchReceiver = (SearchView) findViewById(R.id.sendersearch);
        searchReceiver.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    public void printList() {
        displayReceiever.removeAllViews();
        for (int i = 0; i < ContactsNames.size(); i++) {
            boolean avalFlag = false;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout iconDisplay = new LinearLayout(this);
            iconDisplay.setOrientation(LinearLayout.VERTICAL);
            ImageView fileicon = new ImageView(this);
            LinearLayout messageDetails = new LinearLayout(this);
            messageDetails.setOrientation(LinearLayout.VERTICAL);
            Space spaceTop = new Space(this);
            TextView contactname = new TextView(this);
            contactname.setText(ContactsNames.get(i));
            contactname.setTypeface(Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf"));
            TextView contactImei = new TextView(this);
            contactImei.setText(ContactsNumbers.get(i));
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
                    Intent intent=new Intent(getBaseContext(),HomePage.class);
                    HomePage.recvNum=ContactsNumbers.get(index);
                    HomePage.recvName=ContactsNames.get(index);
                    ContactsNumbers.clear();
                    ContactsNumbers.clear();
                    startActivity(intent);
                    finish();
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


    public void filter(String text) {
        displayReceiever.removeAllViews();
        if (!text.equals("")) {
            for (int i = 0; i < ContactsNames.size(); i++) {
                if (ContactsNames.get(i).toLowerCase().contains(text.toLowerCase())) {
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

                    int pos = ContactsNames.get(i).toLowerCase().indexOf(text.toLowerCase());
                    spannable = new SpannableString(ContactsNames.get(i));
                    spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    contactname.setText(spannable);
                    contactImei.setText(ContactsNumbers.get(i));
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
                            HomePage.recvNum=ContactsNumbers.get(index);
                            HomePage.recvName=ContactsNames.get(index);
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
            for (int i = 0; i < ContactsNumbers.size(); i++) {
                if (ContactsNumbers.get(i).contains(text)) {
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

                    int pos = ContactsNumbers.get(i).toLowerCase().indexOf(text.toLowerCase());
                    spannable = new SpannableString(ContactsNumbers.get(i));
                    spannable.setSpan(new BackgroundColorSpan(Color.BLUE), pos, pos + text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    contactname.setText(ContactsNames.get(i));
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
                            HomePage.recvNum=ContactsNumbers.get(index);
                            HomePage.recvName=ContactsNames.get(index);
                            startActivity(intent);
                            finish();
                        }
                    });

                    displayReceiever.addView(card, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            }
        }else {
            printList();
        }
    }


}
