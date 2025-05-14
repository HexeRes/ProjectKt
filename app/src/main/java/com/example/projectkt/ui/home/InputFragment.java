package com.example.projectkt.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectkt.MainActivity;
import com.example.projectkt.database.HealthData;
import com.example.projectkt.databinding.FragmentInputBinding;

import com.example.projectkt.ui.dashboard.StatsFragment;
import com.google.firebase.database.DatabaseReference;

public class InputFragment extends Fragment {
    private EditText etAge, etHeight, etWeight, etBloodPressure, etHeartRate, etBloodSugar;
    private Button btnSave, btnViewStats;
    private DatabaseReference mDatabase;
    private FragmentInputBinding binding;
    private InputViewModel inputViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        inputViewModel =
                new ViewModelProvider(this).get(InputViewModel.class);


//        etAge = view.findViewById(R.id.etAge);
//        etHeight = view.findViewById(R.id.etHeight);
//        etWeight = view.findViewById(R.id.etWeight);
//        etBloodPressure = view.findViewById(R.id.etBloodPressure);
//        etHeartRate = view.findViewById(R.id.etHeartRate);
//        etBloodSugar = view.findViewById(R.id.etBloodSugar);


        binding = FragmentInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        etAge = binding.etAge;
        etHeight = binding.etHeight;
        etWeight = binding.etWeight;
        etBloodPressure = binding.etBloodPressure;
        etHeartRate = binding.etHeartRate;
        etBloodSugar = binding.etBloodSugar;
        btnSave = binding.btnSave;
        btnViewStats = binding.btnViewStats;

        btnSave.setOnClickListener(v -> saveHealthData());



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private HealthData collectDataFromFields() throws NumberFormatException {
        int age = Integer.parseInt(binding.etAge.getText().toString());
        int height = Integer.parseInt(binding.etHeight.getText().toString());
        double weight = Double.parseDouble(binding.etWeight.getText().toString());
        String bloodPressure = binding.etBloodPressure.getText().toString();
        int heartRate = Integer.parseInt(binding.etHeartRate.getText().toString());
        double bloodSugar = Double.parseDouble(binding.etBloodSugar.getText().toString());

        return new HealthData(age, height, weight, bloodPressure, heartRate, bloodSugar);
    }
    private void saveHealthData() {
        try {
            HealthData healthData = collectDataFromFields();
            inputViewModel.saveHealthData(healthData);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Пожалуйста, введите корректные данные", Toast.LENGTH_SHORT).show();
        }
    }

}