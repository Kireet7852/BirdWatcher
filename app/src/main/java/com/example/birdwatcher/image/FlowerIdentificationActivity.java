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
import com.example.birdwatcher.helpers.MLImageHelperActivity;
import com.example.birdwatcher.web_search;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FlowerIdentificationActivity extends MLImageHelperActivity {

    private ImageLabeler imageLabeler;
    private ListView outputView;
    String clickedName;
    String clickedRecognitionType = "Image";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalModel localModel = new LocalModel.Builder().setAssetFilePath("birdy.tflite").build();
        CustomImageLabelerOptions options = new CustomImageLabelerOptions.Builder(localModel)
                                                .setConfidenceThreshold(0.7f)
                                                .setMaxResultCount(5)
                                                .build();
        imageLabeler = ImageLabeling.getClient(options);
    }

    @Override
    protected void runDetection(Bitmap bitmap) {
        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
        outputView = findViewById(R.id.textView);
        imageLabeler.process(inputImage).addOnSuccessListener(imageLabels -> {
//            StringBuilder sb = new StringBuilder();
//            for (ImageLabel label : imageLabels) {
//                sb.append(label.getText()).append(": ").append(label.getConfidence()).append("\n");
//            }
//            if (imageLabels.isEmpty()) {
//                getOutputTextView().setText("Could not identify!!");
//            } else {
//                getOutputTextView().setText(sb.toString());
//            }
//        }).addOnFailureListener(e -> {
//            e.printStackTrace();
            if (imageLabels.isEmpty()) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, new String[]{"Could not identify!!"});
                outputView.setAdapter(adapter);
                return;
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
                builder.setTitle("Select follow option");
                View dialogView = getLayoutInflater().inflate(R.layout.activity_custom_dialog_layout, null);
                builder.setView(dialogView);

                Button cancelButton = dialogView.findViewById(R.id.cancel_button);

                cancelButton.setOnClickListener((v) -> {
//                    dialog.dismiss();

//                    WebView webView = findViewById(R.id.web_View);
                    String word = clickedItem.split(":")[0].trim();
//                    String web = "https://www.google.com/search?q=" + word;
//                    webView.setWebViewClient(new WebViewClient());
//                    webView.getSettings().setJavaScriptEnabled(true);
//                    webView.loadUrl(web);

                    Intent intent = new Intent(this, web_search.class);
                    intent.putExtra("query", word);
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
//                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}
