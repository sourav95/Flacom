package com.example.invinciblesourav.flacom;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Invincible Sourav on 29-12-2017.
 */

public class Flacom extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            if(FirebaseApp.getApps(this).isEmpty()){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }
        }

}
