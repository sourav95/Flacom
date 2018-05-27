package com.example.invinciblesourav.flacom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;

public class Unsent extends Fragment {

    static View fragmentUnsent;
    public static ArrayList<String> unsentFilename=new ArrayList<String>();
    public static ArrayList<String> unsentFileSend=new ArrayList<String>();
    public static ArrayList<String> unsentFileTime=new ArrayList<String>();
    public static ArrayList<String> unsentFileSize=new ArrayList<String>();
    public static ArrayList<String> unsentFileStat=new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentUnsent=inflater.inflate(R.layout.activity_unsent, container, false);
        
        return fragmentUnsent;
    }
    public void populateUnsent(Context context){
        LinearLayout listingItem=(LinearLayout) fragmentUnsent.findViewById(R.id.unsent_holder1);
        listingItem.removeAllViews();
        for (int i = unsentFilename.size()-1; i >= 0; i--) {
            LinearLayout card=new LinearLayout(context);
            card.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout iconDisplay=new LinearLayout(context);
            iconDisplay.setOrientation(LinearLayout.VERTICAL);
            ImageView fileicon=new ImageView(context);
            LinearLayout messageDetails=new LinearLayout(context);
            messageDetails.setOrientation(LinearLayout.VERTICAL);
            Space spaceTop=new Space(context);
            TextView filename=new TextView(context);
            filename.setText(unsentFilename.get(i));
            filename.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"JosefinSans-Regular.ttf"));
            TextView filesender=new TextView(context);
            filesender.setText("To : "+unsentFileSend.get(i));
            filesender.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"JosefinSans-Regular.ttf"));
            TextView timestamp=new TextView(context);
            timestamp.setText(unsentFileTime.get(i));
            timestamp.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"JosefinSans-Regular.ttf"));
            TextView size=new TextView(context);
            size.setText(unsentFileSize.get(i)+" KB");
            size.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"JosefinSans-Regular.ttf"));
            TextView acknowledged=new TextView(context);
            acknowledged.setText("Unsent");
            acknowledged.setTextColor(Color.parseColor("#f23030"));
            acknowledged.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"JosefinSans-Regular.ttf"));
            Space spaceBelow=new Space(context);


            messageDetails.addView(spaceTop,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));
            messageDetails.addView(filename,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(filesender,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(timestamp,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(size,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(acknowledged,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
            messageDetails.addView(spaceBelow,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 20));

            fileicon.setImageResource(R.drawable.alert);
            Space spaceicon1=new Space(context);
            Space spaceicon2=new Space(context);
            iconDisplay.addView(spaceicon1,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
            iconDisplay.addView(fileicon,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            iconDisplay.addView(spaceicon2,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            card.addView(iconDisplay,new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT , 0.3f));
            card.setBackgroundResource(R.drawable.listborder);
            card.addView(messageDetails,new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT , 1));
            ImageButton menu=new ImageButton(context);
            menu.setImageResource(R.drawable.options);
            menu.setBackgroundColor(Color.parseColor("#00ffffff"));
            card.addView(menu,new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT , 0.2f));


            listingItem.addView(card,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        }

    }
}
