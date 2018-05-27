package com.example.invinciblesourav.flacom;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Background extends Service {
    DatabaseReference databaseReference,databaseStatus;
    //public static int notif_flag=0;
    //int notify=0;
    String flag="";
    //long notif_count=0;
    //long count,prevCount;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    long records=0;
    public static int firstTimeRead=0;
    //ArrayList<String> backRecvFilename=new ArrayList<>();
    static int numberOfFiles=0;
    public static ArrayList<String> receiver=new ArrayList<>();
    public Background() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        /*if (notif_flag==1){
            if(firstTimeRead==0){
                prevCount=Long.parseLong(pref.getString("recordCount","0"));
                firstTimeRead=1;
            }
            if(records>prevCount){
                //Toast.makeText(getBaseContext(),String.valueOf(records)+String.valueOf(prevCount),Toast.LENGTH_SHORT).show();
                numberOfFiles++;
                createNotification();
                prevCount=records;
                //editor.putString("recordCount",String.valueOf(records));
                //editor.commit();
            }

        }*/

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent=new Intent(getApplicationContext(),this.getClass());
        intent.setPackage(getPackageName());
        startService(intent);
        super.onTaskRemoved(rootIntent);
    }
    public void createNotification(){

            /*if(notif_flag==0){
                numberOfFiles=notif_count;
            }*/
            //String fileSender=Received.recvFileRecv.get(Received.recvFilename.size()-1);
        if(numberOfFiles==1) {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(Background.this)
                    .setSmallIcon(R.drawable.receivedicon)
                    .setContentTitle(String.valueOf(numberOfFiles) + " File received")
                    .setContentText(receiver.get(0)+" sent you a file")
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getPackageName() + "/raw/notify"))
                    .setAutoCancel(true);

            Intent notoficationIntent = new Intent(Background.this, HomePage.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notoficationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification.build());
        }else{

            NotificationCompat.Builder notification = new NotificationCompat.Builder(Background.this)
                    .setSmallIcon(R.drawable.receivedicon)
                    .setContentTitle(String.valueOf(numberOfFiles) + " Files received")
                    .setContentText(receiver.size()+" sent you a file")
                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                            + "://" + getPackageName() + "/raw/notify"))
                    .setAutoCancel(true);

            Intent notoficationIntent = new Intent(Background.this, HomePage.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notoficationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(contentIntent);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, notification.build());
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getBaseContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        numberOfFiles=0;
        //prevCount=Long.parseLong(pref.getString("recordCount","0"));
        /*if(Build.BRAND.equalsIgnoreCase("xiaomi") ){

            Intent intentService = new Intent();
            intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentService.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intentService);


        }else if(Build.BRAND.equalsIgnoreCase("Letv")){

            Intent intentService = new Intent();
            intentService.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            startActivity(intentService);

        }
        else if(Build.BRAND.equalsIgnoreCase("Honor")){

            Intent intentService = new Intent();
            intentService.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            startActivity(intentService);

        }*/
        databaseStatus=FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://flacom-44762.firebaseio.com/");
        databaseReference.child("Transactions").orderByChild("receiverID").equalTo(HomePage.userMobileNo).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                boolean flag=false;
                /*String status=dataSnapshot.child("status").getValue(String.class);
                if(status.equals("ST")){
                    numberOfFiles++;
                    for(int i=0;i<receiver.size();i++){
                        if(dataSnapshot.child("senderID").getValue(String.class).equals(receiver.get(i))){
                            flag=true;
                            break;
                        }
                    }
                    if(flag==false) {
                        receiver.add(dataSnapshot.child("senderID").getValue(String.class));
                    }
                    createNotification();
                    databaseStatus.child("Transactions").child(dataSnapshot.getKey()).child("status").setValue("DL");
                }*/


                /*if(notif_flag==0){
                    notif_count+=1;
                    createNotification();
                    //Toast.makeText(getBaseContext(),String.valueOf(records)+String.valueOf(prevCount),Toast.LENGTH_SHORT).show();
                }
                if(notif_flag==1){

                    records+=1;
                    //Toast.makeText(getBaseContext(),String.valueOf(records)+String.valueOf(prevCount),Toast.LENGTH_SHORT).show();
                }*/




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
        //Toast.makeText(getBaseContext(),backRecvFilename.toString(),Toast.LENGTH_LONG).show();

    }

}
