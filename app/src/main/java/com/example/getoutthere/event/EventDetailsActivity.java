package com.example.getoutthere.event;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.getoutthere.R;
import com.example.getoutthere.models.EntrantProfile;
import com.example.getoutthere.utils.FirebaseHelper;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView eventName, eventAddress, eventDate, eventCapacity, eventFee;
    private Button btnJoin, btnLeave;

    private String eventId;
    private Event event;
    private EntrantProfile entrant;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // UI elements
        eventName = findViewById(R.id.EventName);
        eventAddress = findViewById(R.id.EventAddress);
        eventDate = findViewById(R.id.EventDate);
        eventCapacity = findViewById(R.id.EventCapacity);
        eventFee = findViewById(R.id.EventSignupFee);

        btnJoin = findViewById(R.id.btnJoinWaitingList);
        btnLeave = findViewById(R.id.btnLeaveWaitingList);

        // Get event ID from intent
        eventId = getIntent().getStringExtra("eventId");

        // Get device ID for entrant
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        entrant = new EntrantProfile();
        entrant.setDeviceId(deviceId);

        // Fetch event from Firestore
        db.collection("events").document(eventId).get().addOnSuccessListener(documentSnapshot -> {

            if (documentSnapshot.exists()) {

                event = documentSnapshot.toObject(Event.class);
                event.setId(documentSnapshot.getId());

                eventName.setText(event.getName());
                eventAddress.setText("Address: " + event.getDescription());
                eventDate.setText("Lottery Draw Date: " + event.getDrawDate());
                eventCapacity.setText("Spots Available: " + event.getCapacity());
                eventFee.setText("Signup Fee: $" + event.getSignupFee());

                ImageView eventPoster = findViewById(R.id.EventPoster);

                // Check if the event has a poster URL saved
                if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
                    Glide.with(EventDetailsActivity.this)
                            .load(event.getPosterUrl())
                            .into(eventPoster);
                }
            }
        });

        // Join waiting list
        btnJoin.setOnClickListener(v -> {
            btnJoin.setEnabled(false);
            btnJoin.setText("Joining...");

            FirebaseHelper.joinWaitingList(event, entrant, new FirebaseHelper.WaitlistCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(EventDetailsActivity.this, "Joined waiting list!", Toast.LENGTH_SHORT).show();
                    btnJoin.setText("Joined");
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(EventDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    btnJoin.setEnabled(true);
                    btnJoin.setText("Join Waiting List");
                }
            });
        });
    }
}