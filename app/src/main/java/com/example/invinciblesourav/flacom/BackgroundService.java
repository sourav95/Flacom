package com.example.invinciblesourav.flacom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Invincible Sourav on 17-01-2018.
 */

public class BackgroundService extends Service {
    private NotificationManager mNM;
    Bundle b;
    Intent notificationIntent;
    private final IBinder mBinder = new LocalBinder();
    private String newtext;
    DatabaseReference databaseReference;

    public class LocalBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public void onCreate() {

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
        databaseReference.child("Transactions").orderByChild("receiver").equalTo("dtg").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //Toast.makeText(getBaseContext(),String.valueOf(dataSnapshot.getKey()),Toast.LENGTH_LONG).show();
                /*Received.recvFilename.add(dataSnapshot.child("filename").getValue(String.class));
                Received.recvFileRecv.add(dataSnapshot.child("sender").getValue(String.class));
                Received.recvFileSize.add(dataSnapshot.child("size").getValue(String.class));
                Received.recvFileUri.add(dataSnapshot.child("uri").getValue(String.class));
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                final String time = dateFormat.format(calendar.getTime());
                Received.recvFileTime.add(time);*/
                createNotification();
                //Received received = new Received();
                //received.populateRecv(HomePage.this);
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

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(BackgroundService.this,"bokachoda",Toast.LENGTH_LONG).show();
        return START_NOT_STICKY;
    }
    public void onDestroy() {
        stopSelf();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public void createNotification(){
        NotificationCompat.Builder notification=new NotificationCompat.Builder(BackgroundService.this)
                .setSmallIcon(R.drawable.receivedicon)
                .setContentTitle("Message")
                .setContentText("dfkjjjjjvkjdvnkdb");
        Intent notoficationIntent=new Intent(BackgroundService.this,HomePage.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,notoficationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(contentIntent);
        NotificationManager manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,notification.build());

    }
}
