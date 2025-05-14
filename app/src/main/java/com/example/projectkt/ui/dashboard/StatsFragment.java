package com.example.projectkt.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectkt.database.HealthData;
import com.example.projectkt.databinding.FragmentStatsBinding;
import com.example.projectkt.ui.home.InputViewModel;
import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    private InputViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(InputViewModel.class);
        binding = FragmentStatsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getAllHealthData().observe(getViewLifecycleOwner(), healthDataList -> {
            if (healthDataList != null && !healthDataList.isEmpty()) {
                calculateAndDisplayStats(healthDataList);
            } else {
                binding.tvAnalysis.setText("Нет данных для анализа");
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                binding.tvAnalysis.setText("Ошибка: " + error);
            }
        });
    }

    private void calculateAndDisplayStats(List<HealthData> healthDataList) {
        // Рассчитываем средние значения
        HealthStats stats = calculateHealthStats(healthDataList);

        // Формируем текст с анализом
        String analysisText = buildAnalysisText(stats, healthDataList);

        // Отображаем результат
        binding.tvAnalysis.setText(analysisText);
    }

    private HealthStats calculateHealthStats(List<HealthData> dataList) {
        HealthStats stats = new HealthStats();
        int size = dataList.size();

        // Рассчет средних значений
        stats.avgAge = dataList.stream().mapToInt(HealthData::getAge).average().orElse(0);
        stats.avgWeight = dataList.stream().mapToDouble(HealthData::getWeight).average().orElse(0);
        stats.avgHeartRate = dataList.stream().mapToInt(HealthData::getHeartRate).average().orElse(0);
        stats.avgBloodSugar = dataList.stream().mapToDouble(HealthData::getBloodSugar).average().orElse(0);

        // Рассчет изменений (последнее значение - первое)
        if (size > 1) {
            HealthData first = dataList.get(0);
            HealthData last = dataList.get(size - 1);

            stats.weightChange = last.getWeight() - first.getWeight();
            stats.heartRateChange = last.getHeartRate() - first.getHeartRate();
            stats.bloodSugarChange = last.getBloodSugar() - first.getBloodSugar();
        }

        return stats;
    }

    private String buildAnalysisText(HealthStats stats, List<HealthData> dataList) {
        StringBuilder sb = new StringBuilder();
        sb.append("Статистика по ").append(dataList.size()).append(" измерениям:\n\n");

        // Средние значения
        sb.append("Средние показатели:\n");
        sb.append(String.format("• Возраст: %.1f лет\n", stats.avgAge));
        sb.append(String.format("• Вес: %.1f кг\n", stats.avgWeight));
        sb.append(String.format("• Пульс: %.1f уд/мин\n", stats.avgHeartRate));
        sb.append(String.format("• Сахар: %.1f ммоль/л\n\n", stats.avgBloodSugar));

        // Динамика изменений
        if (dataList.size() > 1) {
            sb.append("Изменения с первого измерения:\n");
            sb.append(String.format("• Вес: %+.1f кг\n", stats.weightChange));
            sb.append(String.format("• Пульс: %+d уд/мин\n", stats.heartRateChange));
            sb.append(String.format("• Сахар: %+.1f ммоль/л\n", stats.bloodSugarChange));

            // Анализ динамики
            sb.append("\nАнализ динамики:\n");
            if (stats.weightChange > 2) sb.append("• Заметный набор веса\n");
            else if (stats.weightChange < -2) sb.append("• Заметное снижение веса\n");

            if (stats.heartRateChange > 10) sb.append("• Учащение пульса\n");
            else if (stats.heartRateChange < -10) sb.append("• Урежение пульса\n");
        }


        return sb.toString();
    }

    private double calculateBMI(int height, double weight) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(InputViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Вспомогательный класс для хранения статистик
    private static class HealthStats {
        double avgAge;
        double avgWeight;
        double avgHeartRate;
        double avgBloodSugar;
        double weightChange;
        int heartRateChange;
        double bloodSugarChange;
    }
}