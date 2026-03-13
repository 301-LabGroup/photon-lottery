package com.example.getoutthere.organizer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getoutthere.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class EntrantListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final List<String> entrantNames = new ArrayList<>();
    private final List<String> entrantDeviceIds = new ArrayList<>();

    private String eventId;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrant_list);

        listView = findViewById(R.id.entrantListView);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                entrantNames
        );
        listView.setAdapter(adapter);

        eventId = getIntent().getStringExtra("eventId");

        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "No event ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadWaitlistEntrants();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

    }

    private void loadWaitlistEntrants() {
        db.collection("events")
                .document(eventId)
                .collection("waitingList")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    entrantNames.clear();
                    entrantDeviceIds.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        entrantNames.add("No one is on the waiting list.");
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String deviceId = doc.getId();

                        entrantDeviceIds.add(deviceId);
                        entrantNames.add("Loading...");
                        int index = entrantNames.size() - 1;

                        loadProfileName(deviceId, index);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load waiting list", Toast.LENGTH_SHORT).show()
                );
    }

    private void loadProfileName(String deviceId, int index) {
        db.collection("profiles")
                .document(deviceId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String name = "Unknown user";

                    if (documentSnapshot.exists()) {
                        String fetchedName = documentSnapshot.getString("name");
                        if (fetchedName != null && !fetchedName.isEmpty()) {
                            name = fetchedName;
                        }
                    }

                    entrantNames.set(index, name);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    entrantNames.set(index, "Unknown user");
                    adapter.notifyDataSetChanged();
                });
    }
}