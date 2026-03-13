package com.example.getoutthere.organizer;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.getoutthere.R;
import com.example.getoutthere.event.Event;
import com.example.getoutthere.repositories.EventRepository;
import com.example.getoutthere.utils.QRCodeGenerator;
import com.google.zxing.WriterException;

public class EventQrCodeActivity extends AppCompatActivity {

    private ImageView qrCodeImage;
    private Button backButton;
    private Button exportButton;
    private EventRepository eventRepository;
    private QRCodeGenerator qrCodeGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr_code);

        qrCodeImage = findViewById(R.id.qrCodeImage);
        backButton = findViewById(R.id.backButton);
        exportButton = findViewById(R.id.exportButton);


        eventRepository = new EventRepository();
        qrCodeGenerator = new QRCodeGenerator();

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Missing event id", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // go back a page when clicked
        backButton.setOnClickListener(v -> finish());

        eventRepository.getEventById(eventId, new EventRepository.RepositoryCallback<Event>() {
            @Override
            public void onSuccess(Event event) {
                try {
                    String qrContent = event.getQrCodeContent();
                    if (qrContent == null || qrContent.isEmpty()) {
                        Toast.makeText(EventQrCodeActivity.this, "No QR data was found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Bitmap bitmap = qrCodeGenerator.generateQRCode(qrContent);
                    qrCodeImage.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    Toast.makeText(EventQrCodeActivity.this, "Failed to generate The QR code", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(EventQrCodeActivity.this, "Failed to load the event", Toast.LENGTH_SHORT).show();
            }
        });
    }
}