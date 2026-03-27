package com.example.getoutthere.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.getoutthere.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_event, parent, false);
        }

        Event event = events.get(position);

        TextView eventName = convertView.findViewById(R.id.eventName);
        ImageView eventImage = convertView.findViewById(R.id.eventImage);
        TextView noImageText = convertView.findViewById(R.id.noImageText);
        TextView eventDrawDate = convertView.findViewById(R.id.eventDrawDate);

        // ✅ Event name (from Firebase)
        eventName.setText(event.getName());

        // ✅ Draw date (from Firebase)
        if (event.getDrawDate() != null) {
            String formattedDate = new SimpleDateFormat("MM/dd/yyyy")
                    .format(event.getDrawDate().toDate());
            eventDrawDate.setText("Draw Date: " + formattedDate);
        } else {
            eventDrawDate.setText("Draw Date: N/A");
        }

        // ✅ Image handling (IMPORTANT FIX)
        if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {

            Glide.with(context)
                    .load(event.getPosterUrl())
                    .into(eventImage);

            noImageText.setVisibility(View.GONE);

        } else {
            // Keep image space, but don't load anything
            eventImage.setImageDrawable(null);
            noImageText.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}