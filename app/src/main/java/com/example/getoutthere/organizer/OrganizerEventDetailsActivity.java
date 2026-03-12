package com.example.getoutthere.organizer;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.widget.Button;
import com.example.getoutthere.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrganizerEventDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button btnBack;
    private Button btnDrawLottery;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_organizer_event_details);

        // Get eventId passed from previous screen
        eventId = getIntent().getStringExtra("eventId");

        // UI elements
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);
        btnDrawLottery = findViewById(R.id.btnDrawLottery);

        // Set up ViewPager with fragments
        OrganizerTabAdapter adapter = new OrganizerTabAdapter(this, eventId);
        viewPager.setAdapter(adapter);

        // Connect TabLayout to ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("Invited"); break;
                case 1: tab.setText("Enrolled"); break;
                case 2: tab.setText("Cancelled"); break;
                case 3: tab.setText("Waitlist"); break;
            }
        }).attach();

        // Back button
        btnBack.setOnClickListener(v -> finish());

    }
}