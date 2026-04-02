// The following class is from Anthropic, Claude, "Add a filter button fragment to EventListActivity", 2026-04-01
package com.example.getoutthere.event;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.getoutthere.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class EventFilterFragment extends BottomSheetDialogFragment {

    public interface FilterListener {
        void onFiltersApplied(String category, String startDate, String endDate, String location);
    }

    private FilterListener listener;
    private String selectedStartDate = "";
    private String selectedEndDate = "";

    public void setFilterListener(FilterListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        Button btnStartDate = view.findViewById(R.id.btnStartDate);
        Button btnEndDate = view.findViewById(R.id.btnEndDate);
        EditText filterLocation = view.findViewById(R.id.filterLocation);
        Button btnApplyFilter = view.findViewById(R.id.btnApplyFilter);

        // Populate category spinner — replace with your actual categories
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Sports", "Music", "Arts", "Food", "Outdoors"});
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        btnStartDate.setOnClickListener(v -> showDatePicker(true, btnStartDate));
        btnEndDate.setOnClickListener(v -> showDatePicker(false, btnEndDate));

        btnApplyFilter.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFiltersApplied(
                        spinnerCategory.getSelectedItem().toString(),
                        selectedStartDate,
                        selectedEndDate,
                        filterLocation.getText().toString().trim()
                );
            }
            dismiss();
        });
    }

    private void showDatePicker(boolean isStart, Button button) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
            String date = year + "-" + (month + 1) + "-" + day;
            if (isStart) selectedStartDate = date;
            else selectedEndDate = date;
            button.setText(date);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }
}