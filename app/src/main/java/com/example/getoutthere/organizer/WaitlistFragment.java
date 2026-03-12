package com.example.getoutthere.organizer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getoutthere.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WaitlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaitlistFragment extends Fragment {

    // Rename parameter argument to match eventId
    private static final String ARG_EVENT_ID = "eventId";

    // Event ID parameter
    private String eventId;

    public WaitlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventId The ID of the event to display the waitlist for.
     * @return A new instance of fragment WaitlistFragment.
     */
    public static WaitlistFragment newInstance(String eventId) {
        WaitlistFragment fragment = new WaitlistFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waitlist, container, false);
    }
}