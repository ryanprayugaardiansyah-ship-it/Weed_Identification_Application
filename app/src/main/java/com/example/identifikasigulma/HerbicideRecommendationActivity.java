package com.example.identifikasigulma;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.graphics.Typeface;

public class HerbicideRecommendationActivity extends AppCompatActivity {

    private String[] weedNames;
    private String cropType;
    private DatabaseGulma dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herbicide_recommendation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DatabaseGulma(this);

        // Mendapatkan data dari intent
        Intent intent = getIntent();
        weedNames = intent.getStringArrayExtra("WEED_NAMES");
        cropType = intent.getStringExtra("CROP_TYPE");

        if (weedNames != null && cropType != null) {
            List<HerbicideRecommendation> topThreeHerbicides = getTopThreeHerbicideRecommendations(weedNames, cropType);

            // Inisialisasi TextView
            TextView herbicide1TextView = findViewById(R.id.herbicide1);
            TextView description1TextView = findViewById(R.id.description1);
            TextView activeIngredient1TextView = findViewById(R.id.active_ingredient1);
            TextView similarityPercentage1TextView = findViewById(R.id.similarity_percentage1);

            TextView herbicide2TextView = findViewById(R.id.herbicide2);
            TextView description2TextView = findViewById(R.id.description2);
            TextView activeIngredient2TextView = findViewById(R.id.active_ingredient2);
            TextView similarityPercentage2TextView = findViewById(R.id.similarity_percentage2);

            TextView herbicide3TextView = findViewById(R.id.herbicide3);
            TextView description3TextView = findViewById(R.id.description3);
            TextView activeIngredient3TextView = findViewById(R.id.active_ingredient3);
            TextView similarityPercentage3TextView = findViewById(R.id.similarity_percentage3);

            // Menampilkan rekomendasi herbisida teratas beserta persentase kemiripannya
            if (topThreeHerbicides.size() > 0) {
                HerbicideRecommendation herbicide1 = topThreeHerbicides.get(0);
                herbicide1TextView.setText(herbicide1.name);
                description1TextView.setText(highlightWeeds(herbicide1.description, weedNames));
                activeIngredient1TextView.setText(herbicide1.activeIngredient);
                similarityPercentage1TextView.setText("Similarity: " + String.format("%.2f", herbicide1.similarity) + "%");
            }
            if (topThreeHerbicides.size() > 1) {
                HerbicideRecommendation herbicide2 = topThreeHerbicides.get(1);
                herbicide2TextView.setText(herbicide2.name);
                description2TextView.setText(highlightWeeds(herbicide2.description, weedNames));
                activeIngredient2TextView.setText(herbicide2.activeIngredient);
                similarityPercentage2TextView.setText("Similarity: " + String.format("%.2f", herbicide2.similarity) + "%");
            }
            if (topThreeHerbicides.size() > 2) {
                HerbicideRecommendation herbicide3 = topThreeHerbicides.get(2);
                herbicide3TextView.setText(herbicide3.name);
                description3TextView.setText(highlightWeeds(herbicide3.description, weedNames));
                activeIngredient3TextView.setText(herbicide3.activeIngredient);
                similarityPercentage3TextView.setText("Similarity: " + String.format("%.2f", herbicide3.similarity) + "%");
            }
        }
    }

    /**
     * Mendapatkan tiga rekomendasi herbisida teratas berdasarkan nama gulma dan jenis tanaman.
     *
     * @param weedNames Nama-nama gulma
     * @param cropType Jenis tanaman
     * @return Daftar tiga rekomendasi herbisida teratas
     */
    private List<HerbicideRecommendation> getTopThreeHerbicideRecommendations(String[] weedNames, String cropType) {
        List<HerbicideRecommendation> herbicideRecommendations = new ArrayList<>();
        Map<String, Double> herbicideSimilarityMap = new HashMap<>();

        // Mendapatkan herbisida dari tabel rekomendasi utama
        Cursor cursor = dbHelper.getHerbicideRecommendationsByCropType(cropType);
        while (cursor != null && cursor.moveToNext()) {
            String herbicideName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_HERBICIDE_NAME));
            String herbicideDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_DESCRIPTION_HERBICIDE));
            String activeIngredient = cursor.getString(cursor.getColumnIndexOrThrow("active_ingredient"));
            double averageSimilarity = calculateAverageSimilarity(herbicideDescription, weedNames);
            herbicideSimilarityMap.put(herbicideName, averageSimilarity);
            herbicideRecommendations.add(new HerbicideRecommendation(herbicideName, herbicideDescription, activeIngredient, averageSimilarity));
        }

        // Mendapatkan herbisida tambahan dari tabel rekomendasi tambahan
        Cursor additionalCursor = dbHelper.getAdditionalHerbicideRecommendationsByCropType(cropType);
        while (additionalCursor != null && additionalCursor.moveToNext()) {
            String herbicideName = additionalCursor.getString(additionalCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_ADDITIONAL_HERBICIDE_NAME));
            String herbicideDescription = additionalCursor.getString(additionalCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_ADDITIONAL_DESCRIPTION));
            String activeIngredient = additionalCursor.getString(additionalCursor.getColumnIndexOrThrow("active_ingredient"));
            double averageSimilarity = calculateAverageSimilarity(herbicideDescription, weedNames);
            herbicideSimilarityMap.put(herbicideName, averageSimilarity);
            herbicideRecommendations.add(new HerbicideRecommendation(herbicideName, herbicideDescription, activeIngredient, averageSimilarity));
        }

        // Mengurutkan herbisida berdasarkan kemiripan dalam urutan menurun
        List<Map.Entry<String, Double>> sortedHerbicides = new ArrayList<>(herbicideSimilarityMap.entrySet());
        Collections.sort(sortedHerbicides, (entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()));

        List<HerbicideRecommendation> topThreeHerbicides = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedHerbicides.subList(0, Math.min(3, sortedHerbicides.size()))) {
            for (HerbicideRecommendation recommendation : herbicideRecommendations) {
                if (recommendation.name.equals(entry.getKey())) {
                    topThreeHerbicides.add(recommendation);
                    break;
                }
            }
        }
        return topThreeHerbicides;
    }

    /**
     * Menghitung rata-rata kemiripan antara deskripsi herbisida dan nama ilmiah gulma.
     *
     * @param herbicideDescription Deskripsi herbisida
     * @param weedNames Nama-nama gulma
     * @return Rata-rata kemiripan
     */
    private double calculateAverageSimilarity(String herbicideDescription, String[] weedNames) {
        int matchingWeedsCount = 0;

        for (String weedName : weedNames) {
            Cursor weedCursor = dbHelper.getWeedDetails(weedName);
            if (weedCursor != null && weedCursor.moveToFirst()) {
                String weedScientificName = weedCursor.getString(weedCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_SCIENTIFIC_NAME));
                if (isWeedInHerbicideDescription(herbicideDescription, weedScientificName)) {
                    matchingWeedsCount++;
                }
            }
            if (weedCursor != null) {
                weedCursor.close();
            }
        }

        // Hitung persentase kesamaan berdasarkan jumlah gulma yang cocok
        double similarityPercentage = (matchingWeedsCount / (double) weedNames.length) * 100.0;
        return similarityPercentage;
    }

    private boolean isWeedInHerbicideDescription(String herbicideDescription, String weedScientificName) {
        return herbicideDescription.toLowerCase().contains(weedScientificName.toLowerCase());
    }

    // Metode untuk membuat teks bold pada nama ilmiah gulma yang terdeteksi
    private SpannableString highlightWeeds(String description, String[] weedNames) {
        SpannableString spannableString = new SpannableString(description);

        for (String weedName : weedNames) {
            Cursor weedCursor = dbHelper.getWeedDetails(weedName);
            if (weedCursor != null && weedCursor.moveToFirst()) {
                String weedScientificName = weedCursor.getString(weedCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_SCIENTIFIC_NAME));
                int start = description.toLowerCase().indexOf(weedScientificName.toLowerCase());
                while (start != -1) {
                    int end = start + weedScientificName.length();
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = description.toLowerCase().indexOf(weedScientificName.toLowerCase(), end);
                }
                weedCursor.close();
            }
        }

        return spannableString;
    }

    /**
     * Menghitung kemiripan kosinus antara dua teks.
     *
     * @param text1 Teks pertama
     * @param text2 Teks kedua
     * @return Nilai kemiripan kosinus
     */
    public double cosineSimilarity(String text1, String text2) {
        Map<String, Integer> freq1 = getTermFrequencies(text1);
        Map<String, Integer> freq2 = getTermFrequencies(text2);

        Set<String> terms = new HashSet<>(freq1.keySet());
        terms.addAll(freq2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (String term : terms) {
            int f1 = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                f1 = freq1.getOrDefault(term, 0);
            }
            int f2 = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                f2 = freq2.getOrDefault(term, 0);
            }
            dotProduct += f1 * f2;
            norm1 += f1 * f1;
            norm2 += f2 * f2;
        }

        if (norm1 == 0 || norm2 == 0) {
            return 0.0;
        } else {
            return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
        }
    }


    /**
     * Mendapatkan frekuensi kemunculan setiap term dalam teks.
     *
     * @param text Teks yang akan dianalisis
     * @return Peta frekuensi term
     */
    private Map<String, Integer> getTermFrequencies(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        String[] tokens = text.split("\\W+");
        for (String token : tokens) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                frequencies.put(token, frequencies.getOrDefault(token, 0) + 1);
            }
        }
        return frequencies;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani tombol kembali
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Kelas untuk merepresentasikan rekomendasi herbisida.
    private static class HerbicideRecommendation {
        String name;
        String description;
        String activeIngredient;
        double similarity;

        HerbicideRecommendation(String name, String description, String activeIngredient, double similarity) {
            this.name = name;
            this.description = description;
            this.activeIngredient = activeIngredient;
            this.similarity = similarity;
        }
    }

}
