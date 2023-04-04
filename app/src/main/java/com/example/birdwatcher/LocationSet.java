package com.example.birdwatcher;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.birdwatcher.helpers.LocationTrack;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class LocationSet extends AppCompatActivity {

//    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitude_label,longitude_label,address,city,country;
    TextView bird_spices, datetime, recognition_type, prediction;
    Button getLocation, click_save;
    private final static int REQUEST_CODE = 100;
//
//    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference birdsRef = databaseRef.child("newBirdRef");
    DatabaseReference newBirdRef = birdsRef.push();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();





    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_set);


        bird_spices = findViewById(R.id.bird_name);
        datetime = findViewById(R.id.date_time);
        recognition_type = findViewById(R.id.recog_type);
        prediction = findViewById(R.id.confidence);

        latitude_label = findViewById(R.id.lattitude);
        longitude_label = findViewById(R.id.longitude);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        getLocation = findViewById(R.id.getLocation);
        click_save = findViewById(R.id.save_button);

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getLocation.setOnClickListener(v -> getLastLocation());



        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }


        Intent intent = getIntent();
        bird_spices.setText(intent.getStringExtra("clickedName"));
        recognition_type.setText(intent.getStringExtra("clickedRecognitionType"));
        datetime.setText(intent.getStringExtra("formattedDataTime"));
        prediction.setText(intent.getStringExtra("confidence_level"));


        getLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                locationTrack = new LocationTrack(LocationSet.this);


                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    DecimalFormat df = new DecimalFormat("#.#####");

                    Toast.makeText(getApplicationContext(), "Longitude:" + longitude + "\nLatitude:" + latitude, Toast.LENGTH_SHORT).show();
                    Geocoder geocoder = new Geocoder(LocationSet.this, Locale.getDefault());
                    try {
                        ArrayList<Address> addresses;
                        addresses = (ArrayList<Address>) geocoder.getFromLocation(latitude, longitude, 1);
                        latitude_label.setText(df.format(latitude));
                        longitude_label.setText(df.format(longitude));
                        address.setText(addresses.get(0).getAddressLine(0));
                        city.setText(addresses.get(0).getLocality());
                        country.setText(addresses.get(0).getCountryName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                    locationTrack.showSettingsAlert();
                }


            }
        });


        click_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String bird_species_value = bird_spices.getText().toString();
//                String datetime_value = datetime.getText().toString();
//                String recognition_type_value = recognition_type.getText().toString();
//                String prediction_value = prediction.getText().toString();
//                String latitude_value = latitude_label.getText().toString();
//                String longitude_value = longitude_label.getText().toString();
//                String address_value = address.getText().toString();
//                String city_value = city.getText().toString();
//                String country_value = country.getText().toString();
//
//                // Create a new HashMap to hold the data
//                Map<String, Object> birdInfo = new HashMap<>();
//                birdInfo.put("bird_species", bird_species_value);
//                birdInfo.put("datetime", datetime_value);
//                birdInfo.put("recognition_type", recognition_type_value);
//                birdInfo.put("prediction", prediction_value);
//                birdInfo.put("latitude", latitude_value);
//                birdInfo.put("longitude", longitude_value);
//                birdInfo.put("address", address_value);
//                birdInfo.put("city", city_value);
//                birdInfo.put("country", country_value);
//
//                // Save the data to Firebase
//                newBirdRef.setValue(birdInfo)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(LocationSet.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(LocationSet.this, "Error saving data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

                // Get the text values from the TextViews
                String birdSpecies = bird_spices.getText().toString();
                String dateTime = datetime.getText().toString();
                String recognitionType = recognition_type.getText().toString();
                String predictionValue = prediction.getText().toString();
                String latitude = latitude_label.getText().toString();
                String longitude = longitude_label.getText().toString();
                String addressValue = address.getText().toString();
                String cityValue = city.getText().toString();
                String countryValue = country.getText().toString();
                String user_email = user.getEmail();

                // Set the values to a new child node under the birdsRef
//                newBirdRef.child("bird_species").setValue(birdSpecies);
//                newBirdRef.child("date_time").setValue(dateTime);
//                newBirdRef.child("recognition_type").setValue(recognitionType);
//                newBirdRef.child("prediction").setValue(predictionValue);
//                newBirdRef.child("latitude").setValue(latitude);
//                newBirdRef.child("longitude").setValue(longitude);
//                newBirdRef.child("address").setValue(addressValue);
//                newBirdRef.child("city").setValue(cityValue);
//                newBirdRef.child("country").setValue(countryValue);

                if (TextUtils.isEmpty(birdSpecies) || TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(recognitionType)
                        || TextUtils.isEmpty(predictionValue) || TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)
                        || TextUtils.isEmpty(addressValue) || TextUtils.isEmpty(cityValue) || TextUtils.isEmpty(countryValue)) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields! or click on location button", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    newBirdRef.child("bird_species").setValue(birdSpecies);
                    newBirdRef.child("date_time").setValue(dateTime);
                    newBirdRef.child("recognition_type").setValue(recognitionType);
                    newBirdRef.child("prediction").setValue(predictionValue);
                    newBirdRef.child("latitude").setValue(latitude);
                    newBirdRef.child("longitude").setValue(longitude);
                    newBirdRef.child("address").setValue(addressValue);
                    newBirdRef.child("city").setValue(cityValue);
                    newBirdRef.child("country").setValue(countryValue);
                    newBirdRef.child("u_email").setValue(user_email);
                    Toast.makeText(getApplicationContext(), "Bird saved successfully!", Toast.LENGTH_SHORT).show();
//                    finish(); // close the activity
                } catch (DatabaseException e) {
                    Toast.makeText(getApplicationContext(), "Error saving bird: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(LocationSet.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
    }
}