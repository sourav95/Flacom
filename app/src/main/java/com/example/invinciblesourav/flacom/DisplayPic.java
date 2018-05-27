package com.example.invinciblesourav.flacom;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayPic extends AppCompatActivity {
    String fileName,fileRecvName,fileSender,fileTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pic);
        Intent intent=getIntent();
        fileName=intent.getStringExtra("filename");
        fileRecvName=intent.getStringExtra("filerecvname");
        fileSender=intent.getStringExtra("filesender");
        fileTime=intent.getStringExtra("filetime");
        TextView displayHeader=(TextView) findViewById(R.id.displaypic_head);
        displayHeader.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        TextView filename=(TextView) findViewById(R.id.displaypic_filename);
        filename.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        filename.setText("Filename: "+fileRecvName);
        TextView filesender=(TextView) findViewById(R.id.displaypic_filesender);
        filesender.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        filesender.setText("From: "+fileSender);
        TextView fileTimestamp=(TextView) findViewById(R.id.displaypic_filetime);
        fileTimestamp.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        fileTimestamp.setText(fileTime);
        ImageView picture=(ImageView) findViewById(R.id.displaypic_pic);


        ImageView backButton=(ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

    }

}
