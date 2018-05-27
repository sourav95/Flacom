package com.example.invinciblesourav.flacom;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Invincible Sourav on 22-05-2018.
 */

public class LogDisplay extends AppCompatActivity{
    public LayoutInflater inflater;
    AlertDialog logDialog;
    View dialogView;
    LinearLayout logHolder;
    ScrollView scrollView;
    public TextView logHeader;
    public ProgressBar progressBar;
    public  void createDialog(Context context){
        AlertDialog.Builder logDialogBuilder = new AlertDialog.Builder(context);
        //inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.log, null);
        logDialogBuilder.setView(dialogView);
        logDialog = logDialogBuilder.create();
        logDialog.show();
        //logDialog.setCancelable(false);
        //logDialog.setCanceledOnTouchOutside(false);
        logHolder=(LinearLayout) logDialog.findViewById(R.id.logHolder);
        scrollView=(ScrollView) logDialog.findViewById(R.id.scrollLog);
        logHeader=(TextView) logDialog.findViewById(R.id.logHeader);
        progressBar=(ProgressBar) logDialog.findViewById(R.id.progressLog);
    }
    public void dismissDialog(){
        logDialog.dismiss();
    }
    public void addLine(String line,Context context){
        TextView textView=new TextView(context);
        textView.setText(line);
        logHolder.addView(textView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }
}
