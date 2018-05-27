package com.example.invinciblesourav.flacom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class DisplayFile extends AppCompatActivity {
    String fileName,fileRecvName,fileSender,fileTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_file);
        Intent intent=getIntent();
        fileName=intent.getStringExtra("filename");
        fileRecvName=intent.getStringExtra("filerecvname");
        fileSender=intent.getStringExtra("filesender");
        fileTime=intent.getStringExtra("filetime");
        TextView displayHeader=(TextView) findViewById(R.id.display_head);
        displayHeader.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        TextView filename=(TextView) findViewById(R.id.display_filename);
        filename.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        filename.setText("Filename: "+fileRecvName);
        TextView filesender=(TextView) findViewById(R.id.display_filesender);
        filesender.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        filesender.setText("From: "+fileSender);
        TextView fileTimestamp=(TextView) findViewById(R.id.display_filetime);
        fileTimestamp.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        fileTimestamp.setText(fileTime);
        TextView filecontents=(TextView) findViewById(R.id.display_filecontents);
        filecontents.setTypeface(Typeface.createFromAsset(getAssets(),"JosefinSans-Regular.ttf"));
        filecontents.setText(ReadFile(fileName));
        ImageView backButton=(ImageView) findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

    }
    public static  String ReadFile(String fileName){
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
        //Toast.makeText(context, ""+line, Toast.LENGTH_SHORT).show();

        return line;
    }


}
