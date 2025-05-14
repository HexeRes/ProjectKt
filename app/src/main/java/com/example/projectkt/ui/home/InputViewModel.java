package com.example.projectkt.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectkt.database.HealthData;
import com.example.projectkt.database.HealthDatabaseHelper;

import java.util.List;

public class InputViewModel extends ViewModel {
    private final HealthDatabaseHelper dbHelper;
    private final MutableLiveData<HealthData> healthData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<List<HealthData>> allHealthData = new MutableLiveData<>();
    public InputViewModel() {
        dbHelper = new HealthDatabaseHelper();
    }

    public LiveData<List<HealthData>> getAllHealthData() {
        loadAllHealthData(); // Автоматически загружаем данные при запросе
        return allHealthData;
    }
    public void loadAllHealthData() {
        isLoading.setValue(true);
        dbHelper.getAllHealthData(new HealthDatabaseHelper.DataListCallback<HealthData>() {
            @Override
            public void onDataListLoaded(List<HealthData> dataList) {
                isLoading.setValue(false);
                allHealthData.setValue(dataList);
            }

            @Override
            public void onDataNotAvailable(String error) {
                isLoading.setValue(false);
                errorMessage.setValue(error);
            }
        });
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void saveHealthData(HealthData data) {
        dbHelper.saveHealthData(data, new HealthDatabaseHelper.DatabaseCallback() {
            @Override
            public void onSuccess(String message) {
                successMessage.postValue(message);
            }

            @Override
            public void onFailure(String errorMessage) {
                InputViewModel.this.errorMessage.postValue(errorMessage);
            }
        });
    }

    public void loadLatestHealthData() {
        dbHelper.getLatestHealthData(new HealthDatabaseHelper.DataCallback<HealthData>() {
            @Override
            public void onDataLoaded(HealthData data) {
                healthData.postValue(data);
            }

            @Override
            public void onDataNotAvailable(String error) {
                errorMessage.postValue(error);
            }
        });
    }
}