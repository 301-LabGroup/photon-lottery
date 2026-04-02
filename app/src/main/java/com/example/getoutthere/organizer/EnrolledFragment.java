package com.example.getoutthere.organizer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.getoutthere.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.FileWriter;

/**
 * Fragment displaying the enrolled entrants for an event.
 */
public class EnrolledFragment extends Fragment {

    private static final String ARG_EVENT_ID = "eventId";
    private String eventId;

    // Firestore instance
    private FirebaseFirestore db;

    // RecyclerView adapter and data
    private EntrantAdapter adapter;
    private List<Map<String, String>> enrolledEntrants = new ArrayList<>();

    // UI element
    private RecyclerView rvEnrolled;

    public EnrolledFragment() {
        // Required empty public constructor
    }

    public static EnrolledFragment newInstance(String eventId) {
        EnrolledFragment fragment = new EnrolledFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getString(ARG_EVENT_ID);
        }
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enrolled, container, false);

        // Initialize RecyclerView
        rvEnrolled = view.findViewById(R.id.rvEnrolled);
        rvEnrolled.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter
        adapter = new EntrantAdapter(enrolledEntrants);
        rvEnrolled.setAdapter(adapter);

        // Fetch the enrolled entrants from Firebase
        fetchEnrolledEntrants();

        Button btnExportCsv = view.findViewById(R.id.btnExportCsv);
        btnExportCsv.setOnClickListener(v -> previewAndExportCSV());

        return view;
    }

    private void fetchEnrolledEntrants() {
        if (eventId == null) return;

        // Add a snapshot listener so it updates in real-time
        db.collection("events")
                .document(eventId)
                .collection("waitingList")
                .whereEqualTo("status", "Enrolled")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("EnrolledFragment", "Listen failed.", error);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error fetching enrolled list", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                    enrolledEntrants.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            Map<String, String> entrant = new HashMap<>();
                            entrant.put("deviceId", doc.getId());
                            entrant.put("name", doc.getString("name"));
                            entrant.put("email", doc.getString("email"));
                            entrant.put("phone", doc.getString("phone"));
                            entrant.put("status", doc.getString("status"));
                            enrolledEntrants.add(entrant);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    // Generate the CSV and show the preview
    private void previewAndExportCSV() {
        if (enrolledEntrants == null || enrolledEntrants.isEmpty()) {
            Toast.makeText(getContext(), "No enrolled entrants to export", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the CSV string
        StringBuilder csvData = new StringBuilder();
        csvData.append("Name,Email\n");

        for (Map<String, String> entrant : enrolledEntrants) {
            String name = entrant.get("name") != null ? entrant.get("name").replace(",", "") : "Unknown";
            String email = entrant.get("email") != null ? entrant.get("email").replace(",", "") : "No Email";

            csvData.append(name).append(",").append(email).append("\n");
        }

        // Create the Preview Dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("CSV Preview");

        // Use a ScrollView so large lists don't run off the screen
        android.widget.ScrollView scrollView = new android.widget.ScrollView(getContext());
        android.widget.TextView tvPreview = new android.widget.TextView(getContext());

        tvPreview.setText(csvData.toString());
        tvPreview.setPadding(48, 32, 48, 32);
        tvPreview.setTextSize(14f);
        // Use a monospace font so the CSV columns line up neatly
        tvPreview.setTypeface(android.graphics.Typeface.MONOSPACE);

        scrollView.addView(tvPreview);
        builder.setView(scrollView);

        // Add the Share/Export button
        builder.setPositiveButton("Share File", (dialog, which) -> {
            shareCSVFile(csvData.toString()); // Call the share method if they approve
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void shareCSVFile(String csvContent) {
        try {
            // Save to a temporary file in the app's cache
            File file = new File(requireContext().getCacheDir(), "enrolled_entrants.csv");
            FileWriter writer = new FileWriter(file);
            writer.write(csvContent);
            writer.close();

            // Generate a secure URI using the FileProvider
            Uri path = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

            // Trigger the Android Share Menu
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Enrolled Entrants List");
            intent.putExtra(Intent.EXTRA_STREAM, path);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(intent, "Share CSV File"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error sharing CSV: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}