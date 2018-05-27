package com.example.invinciblesourav.flacom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;





public class MainPager extends FragmentStatePagerAdapter {
        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public MainPager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    Received received = new Received();
                    return received;
                case 1:
                    Sent sent = new Sent();
                    return sent;
                case 2:
                    Unsent unsent = new Unsent();
                    return unsent;
                case 3:
                    AddressBook addressBook = new AddressBook();
                    return addressBook;
                //case 2:user_profile up=new user_profile();
                //    return up;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }


        @Override
        public CharSequence getPageTitle(int position) {
       /* switch(position){
            case 0:return "Search";
            case 1:return "History";
        }*/
            return null;
        }


    }

