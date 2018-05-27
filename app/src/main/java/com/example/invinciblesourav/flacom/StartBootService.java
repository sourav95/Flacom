package com.example.invinciblesourav.flacom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Invincible Sourav on 27-01-2018.
 */

public class StartBootService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            //Toast.makeText(context,"ghjk",Toast.LENGTH_LONG).show();

            Intent serviceIntent=new Intent(context,Background.class);
            context.startService(serviceIntent);
        }



    }
}
