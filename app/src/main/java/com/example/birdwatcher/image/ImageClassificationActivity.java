package com.example.birdwatcher.image;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birdwatcher.LocationSet;
import com.example.birdwatcher.R;
import com.example.birdwatcher.Web_search;
import com.example.birdwatcher.helpers.MLImageHelperActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ImageClassificationActivity extends MLImageHelperActivity {
    private ImageLabeler imageLabeler;
    private ListView outputView;
    String clickedName;
    String clickedRecognitionType = "Image";

//    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLabeler = ImageLabeling.getClient(new ImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7f)
                .build());

    }



    @Override
    protected void runDetection(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        outputView = findViewById(R.id.textView);
//        webView = findViewById(R.id.webView);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
//           StringBuilder sb = new StringBuilder();
//           for (ImageLabel label : imageLabels) {
//               sb.append(label.getText()).append(": ").append(label.getConfidence()).append("\n");
//           }
//           if (imageLabels.isEmpty()) {
//               getOutputTextView().setText("Could not classify!!");
//           } else {
//               getOutputTextView().setText(sb.toString());
//           }
//        }).addOnFailureListener(e -> {
//            e.printStackTrace();
            if (imageLabels.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, new String[]{"Could not identify!!"});
                outputView.setAdapter(adapter);
            } else {
                int labelSize = imageLabels.size();
                if (labelSize > 0 && imageLabels.get(labelSize - 1).getText().equals("Could not identify!!")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_list_item_1, new String[]{"Could not identify!!"});
                    outputView.setAdapter(adapter);
                    Toast.makeText(this, "Capture Again Not able to recognize ",  Toast.LENGTH_SHORT).show();
                    return; // Stop function if last label is "Could not identify!!"
                }

                ArrayList<String> labelList = new ArrayList<>();
                for (ImageLabel label : imageLabels) {
                    labelList.add(label.getText() + ": " + String.format("%.3f", label.getConfidence()*100));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, labelList);
                outputView.setAdapter(adapter);
            }

            outputView.setOnItemClickListener((parent, view, position, id) -> {
                // Get the text of the clicked item
                String clickedItem = ((TextView) view).getText().toString();
                // Display a toast message with the clicked item text
                Toast.makeText(this, "Clicked: " + clickedItem, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("Choose an option");
                View dialogView = getLayoutInflater().inflate(R.layout.activity_custom_dialog_layout, null);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                cancelButton.setOnClickListener((v) -> {

                    String word = clickedItem.split(":")[0].trim();


                    Intent intent = new Intent(this, Web_search.class);
                    intent.putExtra("querys", word);
                    startActivity(intent);
                    alertDialog.dismiss();
                });

                Button okButton = dialogView.findViewById(R.id.ok_button);
                okButton.setOnClickListener((v) -> {
                    // Add your own logic here for what to do when the "Ok" button is clicked
                    String word = clickedItem.split(":")[0].trim();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
                    Date date = new Date();

                    String formattedDateTime = dateFormat.format(date);


                    Intent intent = new Intent(this, LocationSet.class);
                    intent.putExtra("clickedName", word);
                    intent.putExtra("clickedRecognitionType", clickedRecognitionType);
                    intent.putExtra("formattedDataTime", formattedDateTime);
                    intent.putExtra("confidence_level",clickedItem.split(":")[1].trim());
                    startActivity(intent);
                    alertDialog.dismiss();
                });

            });
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }




}

//String str = "sky: 80.787";
//String word = str.split(":")[0].trim();
//System.out.println(word);

