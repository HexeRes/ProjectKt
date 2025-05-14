package com.example.projectkt.database;
public class HealthData {
    private int age;
    private int height;
    private double weight;
    private String bloodPressure;
    private int heartRate;
    private double bloodSugar;

    public HealthData() {
        // Пустой конструктор нужен для Firebase
    }

    public HealthData(int age, int height, double weight, String bloodPressure, int heartRate, double bloodSugar) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.bloodSugar = bloodSugar;
    }

    // Геттеры и сеттеры
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public int getHeartRate() { return heartRate; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }

    public double getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(double bloodSugar) { this.bloodSugar = bloodSugar; }
}