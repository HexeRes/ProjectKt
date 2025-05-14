package com.example.projectkt.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectkt.LoginActivity;

import com.example.projectkt.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.Nullable;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = requireActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);

        setupThemeSwitch();
        setupLogoutButton();
        setupAboutSection();
        setupAppInfoSection();
    }

    private void setupThemeSwitch() {
        boolean isDarkTheme = sharedPreferences.getBoolean("DARK_THEME", false);
        binding.switchTheme.setChecked(isDarkTheme);

        binding.switchTheme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("DARK_THEME", isChecked);
            editor.apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO
            );

            requireActivity().recreate();
        });
    }

    private void setupLogoutButton() {
        binding.buttonLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Выход")
                    .setMessage("Вы уверены, что хотите выйти?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        mAuth.signOut();
                        navigateToLogin();
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });
    }

    private void navigateToLogin() {
        startActivity(new Intent(requireActivity(), LoginActivity.class));
        requireActivity().finish();
    }

    private void setupAboutSection() {
        binding.aboutDeveloper.setOnClickListener(v -> showDeveloperInfo());
    }

    private void showDeveloperInfo() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Об авторе")
                .setMessage("Приложение разработано [Ваше имя]\n\n" +
                        "Контакты:\n" +
                        "Email: your@email.com\n" +
                        "GitHub: github.com/yourprofile")
                .setPositiveButton("OK", null)
                .show();
    }

    private void setupAppInfoSection() {
        binding.appInfo.setOnClickListener(v -> showAppInfo());

        try {
            PackageInfo pInfo = requireContext().getPackageManager()
                    .getPackageInfo(requireContext().getPackageName(), 0);
            String version = pInfo.versionName;
            binding.appVersion.setText("Версия " + version);
        } catch (PackageManager.NameNotFoundException e) {
            binding.appVersion.setText("Версия неизвестна");
        }
    }

    private void showAppInfo() {
        new AlertDialog.Builder(requireContext())
                .setTitle("О программе")
                .setMessage("Название приложения\n\n" +
                        "Описание функционала приложения\n\n" +
                        "Используемые технологии:\n" +
                        "- Firebase Authentication\n" +
                        "- Material Design Components\n" +
                        "- Android Jetpack")
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}