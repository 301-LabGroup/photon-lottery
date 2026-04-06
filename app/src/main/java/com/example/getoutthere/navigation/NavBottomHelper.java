package com.example.getoutthere.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.getoutthere.R;
import com.example.getoutthere.admin.AdminDashboardActivity;
import com.example.getoutthere.entrant.EntrantNotificationActivity;
import com.example.getoutthere.entrant.EventHistory;
import com.example.getoutthere.entrant.ProfileActivity;
import com.example.getoutthere.event.EventListActivity;
import com.example.getoutthere.organizer.OrganizerEventListActivity;
import com.example.getoutthere.utils.LocalUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.getoutthere.admin.NotificationLogsActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class NavBottomHelper {



    public static void setupBottomNav(Activity activity, BottomNavigationView bottomNav, int selectedItemId) {
        bottomNav.setSelectedItemId(selectedItemId);

        //open up secret admin door conditionally
        procSecretAdminDoor(bottomNav);


        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == selectedItemId) {
                return true;
            }

            Intent intent = null;

            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, EventListActivity.class);
            } else if (itemId == R.id.nav_manage_events) {
                intent = new Intent(activity, OrganizerEventListActivity.class);
            } else if (itemId == R.id.nav_history) {
                intent = new Intent(activity, EventHistory.class);
            } else if (itemId == R.id.nav_notifications) {
                intent = new Intent(activity, EntrantNotificationActivity.class);
            } else if (itemId == R.id.nav_profile) {
                intent = new Intent(activity, ProfileActivity.class);
            }else if (itemId == R.id.nav_admin) {
                intent = new Intent(activity, AdminDashboardActivity.class);

            }

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
                return true;
            }

            return false;
        });
    }

    public static void procSecretAdminDoor(BottomNavigationView bottomNav){
        String deviceId = LocalUtils.getLocalDeviceId(bottomNav.getContext());

        FirebaseFirestore.getInstance().collection("admins").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Menu menu = bottomNav.getMenu();
                        MenuItem adminItem = menu.findItem(R.id.nav_admin);
                        if (adminItem != null) {
                            adminItem.setVisible(true); // reveal the secret door
                        }
                    }
                });
    }

}