package com.example.getoutthere.organizer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.getoutthere.R;

public class OrganizerEventDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button editButton = new Button(this);
        editButton.setText("Edit Event (Placeholder)");
        setContentView(editButton);
    }
}