package com.example.identifikasigulma;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MultipleWeedDetailActivity extends AppCompatActivity {

    private String[] weedNames;
    private String[] scientificNames;
    private String[] descriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_weed_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        weedNames = intent.getStringArrayExtra("WEED_NAMES");
        scientificNames = intent.getStringArrayExtra("SCIENTIFIC_NAMES");
        descriptions = intent.getStringArrayExtra("DESCRIPTIONS");

        if (weedNames != null && scientificNames != null && descriptions != null) {
            displayWeedDetails(weedNames, scientificNames, descriptions);
        }

        setupCropButtons();
        hideHerbicideRecommendation();
    }

    @Override
    public void onBackPressed() {
        // Kembali ke CameraActivity
        super.onBackPressed();
        Intent intent = new Intent(MultipleWeedDetailActivity.this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Menampilkan detail gulma yang didapat dari intent.
     *
     * @param weedNames Nama-nama gulma
     * @param scientificNames Nama-nama ilmiah gulma
     * @param descriptions Deskripsi gulma
     */
    private void displayWeedDetails(String[] weedNames, String[] scientificNames, String[] descriptions) {
        TextView weedName1TextView = findViewById(R.id.weed_name_1);
        TextView scientificName1TextView = findViewById(R.id.scientific_name_1);
        TextView description1TextView = findViewById(R.id.description_1);

        TextView weedName2TextView = findViewById(R.id.weed_name_2);
        TextView scientificName2TextView = findViewById(R.id.scientific_name_2);
        TextView description2TextView = findViewById(R.id.description_2);

        TextView weedName3TextView = findViewById(R.id.weed_name_3);
        TextView scientificName3TextView = findViewById(R.id.scientific_name_3);
        TextView description3TextView = findViewById(R.id.description_3);

        weedName1TextView.setText(weedNames[0].replace("_", " "));
        scientificName1TextView.setText(scientificNames[0] != null ? scientificNames[0] : "Unknown");
        description1TextView.setText("Deskripsi: " + (descriptions[0] != null ? descriptions[0] : "Not available"));

        weedName2TextView.setText(weedNames[1].replace("_", " "));
        scientificName2TextView.setText(scientificNames[1] != null ? scientificNames[1] : "Unknown");
        description2TextView.setText("Deskripsi: " + (descriptions[1] != null ? descriptions[1] : "Not available"));

        weedName3TextView.setText(weedNames[2].replace("_", " "));
        scientificName3TextView.setText(scientificNames[2] != null ? scientificNames[2] : "Unknown");
        description3TextView.setText("Deskripsi: " + (descriptions[2] != null ? descriptions[2] : "Not available"));
    }


    //Mengatur tombol-tombol untuk memilih jenis tanaman dan menampilkan rekomendasi herbisida.
    private void setupCropButtons() {
        Button riceButton = findViewById(R.id.button_rice);
        Button cornButton = findViewById(R.id.button_corn);
        Button sugarcaneButton = findViewById(R.id.button_sugarcane);
        Button onionButton = findViewById(R.id.button_onion);

        int tidakAdaGulmaCount = countTidakAdaGulmaLabels();

        riceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHerbicideRecommendation("Tanaman Padi");
            }
        });

        cornButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHerbicideRecommendation("Tanaman Jagung");
            }
        });

        sugarcaneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHerbicideRecommendation("Tanaman Tebu");
            }
        });

        onionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHerbicideRecommendation("Tanaman Bawang Merah");
            }
        });

        // Menyembunyikan tombol tanaman jika terdapat 3 label "Tidak_Ada_Gulma"
        if (tidakAdaGulmaCount >= 3) {
            riceButton.setVisibility(View.GONE);
            cornButton.setVisibility(View.GONE);
            sugarcaneButton.setVisibility(View.GONE);
            onionButton.setVisibility(View.GONE);
        }
    }

    /**
     * Menghitung jumlah label "Tidak_Ada_Gulma".
     *
     * @return Jumlah label "Tidak_Ada_Gulma"
     */
    private int countTidakAdaGulmaLabels() {
        int count = 0;
        for (String weedName : weedNames) {
            if (weedName.equals("Tidak_Ada_Gulma")) {
                count++;
            }
        }
        return count;
    }

    //Menampilkan rekomendasi herbisida berdasarkan jenis tanaman yang dipilih.
    private void showHerbicideRecommendation(String cropType) {
        Intent intent = new Intent(MultipleWeedDetailActivity.this, HerbicideRecommendationActivity.class);
        intent.putExtra("WEED_NAMES", weedNames);
        intent.putExtra("CROP_TYPE", cropType);
        startActivity(intent);
    }

    //Menyembunyikan rekomendasi herbisida jika terdapat 3 label "Tidak_Ada_Gulma".
    private void hideHerbicideRecommendation() {
        TextView herbicideRecommendationTextView = findViewById(R.id.herbicide_recommendation);
        int tidakAdaGulmaCount = countTidakAdaGulmaLabels();
        if (tidakAdaGulmaCount >= 3) {
            herbicideRecommendationTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back arrow button in toolbar
        if (item.getItemId() == android.R.id.home) {
            // Create Intent to open CameraActivity
            Intent intent = new Intent(MultipleWeedDetailActivity.this, CameraActivity.class);
            // Clearing the stack so there are no activities above CameraActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Start CameraActivity
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
