package com.example.getoutthere;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getoutthere.entrant.SignUpActivity;
import com.example.getoutthere.event.EventListActivity;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The initial entry point of the Get Out There application.
 * <p>
 * This Activity acts as the main routing hub. It automatically checks whether
 * the current device already has a registered profile and routes the user
 * accordingly.
 * </p>
 * <p>
 * If a profile exists, the user is taken directly to the event list.
 * If no profile exists, the user is taken to the sign-up screen.
 * </p>
 *
 * Outstanding Issues:
 * None
 */
public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String deviceId;

    /**
     * Initializes the Activity, sets up Firebase and the current device ID,
     * and automatically checks whether the user already has a profile.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        checkEntrantProfileAndNavigate();
    }

    /**
     * Queries the Firestore "profiles" collection using the device's unique Android ID.
     * <p>
     * If a profile exists, the user is routed to the {@link EventListActivity}.
     * If no profile is found, the user is routed to the {@link SignUpActivity}.
     * </p>
     */
    private void checkEntrantProfileAndNavigate() {
        db.collection("profiles").document(deviceId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Intent intent;
                    if (documentSnapshot.exists()) {
                        intent = new Intent(MainActivity.this, EventListActivity.class);
                    } else {
                        intent = new Intent(MainActivity.this, SignUpActivity.class);
                    }

                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(MainActivity.this, "Error connecting to database", Toast.LENGTH_SHORT).show()
                );
    }
}