package com.example.projectkt.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class HealthDatabaseHelper {
    private DatabaseReference databaseReference;
    private static final String HEALTH_DATA_NODE = "healthData";

    public HealthDatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://projectkt-edda8-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = database.getReference(HEALTH_DATA_NODE);
    }

    // Сохранение данных здоровья
    public void saveHealthData(HealthData healthData, final DatabaseCallback callback) {
        String key = databaseReference.push().getKey();
        if (key != null) {
            databaseReference.child(key).setValue(healthData)
                    .addOnSuccessListener(aVoid -> callback.onSuccess("Данные успешно сохранены"))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        } else {
            callback.onFailure("Не удалось создать ключ для данных");
        }
    }

    // Получение последних данных здоровья
    public void getLatestHealthData(final DataCallback<HealthData> callback) {
        databaseReference.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HealthData healthData = snapshot.getValue(HealthData.class);
                        if (healthData != null) {
                            callback.onDataLoaded(healthData);
                            return;
                        }
                    }
                }
                callback.onDataNotAvailable("Данные не найдены");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataNotAvailable(databaseError.getMessage());
            }
        });
    }

    // Получение всех данных здоровья (если нужно)
    public void getAllHealthData(final DataListCallback<HealthData> callback) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<HealthData> healthDataList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    HealthData healthData = snapshot.getValue(HealthData.class);
                    if (healthData != null) {
                        healthDataList.add(healthData);
                    }
                }
                callback.onDataListLoaded(healthDataList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onDataNotAvailable(databaseError.getMessage());
            }
        });
    }

    public interface DataListCallback<T> {
        void onDataListLoaded(List<T> dataList);

        void onDataNotAvailable(String errorMessage);
    }

    // Интерфейсы для колбэков
    public interface DatabaseCallback {
        void onSuccess(String message);

        void onFailure(String errorMessage);
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);

        void onDataNotAvailable(String errorMessage);
    }
}