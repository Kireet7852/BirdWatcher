package com.example.birdwatcher.helpers;

public class Bird {
    private String species;
    private String dateTime;
    private String recognitionType;
    private String prediction;
    private String latitude;
    private String longitude;
    private String address;
    private String city;
    private String country;

    public Bird() {} // Required for Firebase Realtime Database

    public Bird(String species, String dateTime, String recognitionType, String prediction, String latitude, String longitude, String address, String city, String country) {
        this.species = species;
        this.dateTime = dateTime;
        this.recognitionType = recognitionType;
        this.prediction = prediction;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    // Add getters and setters
}
