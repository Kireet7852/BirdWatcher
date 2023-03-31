package com.example.birdwatcher.helpers;

public class Bird {
    private String address;
    private String bird_species;
    private String city;
    private String country;
    private String date_time;
    private String latitude;
    private String longitude;
    private String prediction;
    private String recognition_type;
    private String u_email;

    public Bird() {}

    public Bird(String address, String bird_species, String city, String country, String date_time, String latitude, String longitude, String prediction, String recognition_type, String u_email) {
        this.address = address;
        this.bird_species = bird_species;
        this.city = city;
        this.country = country;
        this.date_time = date_time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.prediction = prediction;
        this.recognition_type = recognition_type;
        this.u_email = u_email;
    }

    // getters and setters

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBird_species() {
        return bird_species;
    }

    public void setBird_species(String bird_species) {
        this.bird_species = bird_species;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getRecognition_type() {
        return recognition_type;
    }

    public void setRecognition_type(String recognition_type) {
        this.recognition_type = recognition_type;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }
}
