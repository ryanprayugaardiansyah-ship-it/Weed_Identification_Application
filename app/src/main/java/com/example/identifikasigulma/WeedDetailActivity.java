package com.example.identifikasigulma;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class WeedDetailActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private String weedDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weed_detail);

        // Inisialisasi TextToSpeech
        textToSpeech = new TextToSpeech(this, this);

        // Mengatur Toolbar sebagai ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menambahkan tombol panah kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Mengambil data dari Intent
        String weedName = getIntent().getStringExtra("NAMA_GULMA");
        String scientificName = getIntent().getStringExtra("NAMA_ILMIAH");
        String description = getIntent().getStringExtra("DESKRIPSI");
        String herbicideRecommendation = getIntent().getStringExtra("HERBISIDA");
        String weedImageName = getIntent().getStringExtra("GAMBAR_GULMA");

        Log.d("WeedDetailActivity", "Weed Name: " + weedName);
        Log.d("WeedDetailActivity", "Scientific Name: " + scientificName);
        Log.d("WeedDetailActivity", "Description: " + description);
        Log.d("WeedDetailActivity", "Herbicide: " + herbicideRecommendation);
        Log.d("WeedDetailActivity", "Image URL: " + weedImageName);

        // Mengatur tampilan dengan data yang diterima
        TextView weedNameTextView = findViewById(R.id.nama_gulma);
        TextView scientificNameTextView = findViewById(R.id.nama_ilmiah);
        TextView descriptionTextView = findViewById(R.id.deskripsi);
        TextView herbicideRecommendationTextView = findViewById(R.id.herbisida);
        ImageView weedImageView = findViewById(R.id.gambar_gulma);

        weedNameTextView.setText(weedName.replace("_", " "));
        scientificNameTextView.setText(scientificName);
        descriptionTextView.setText(description);
        herbicideRecommendationTextView.setText(herbicideRecommendation);

        // Menyimpan deskripsi gulma untuk digunakan saat TextToSpeech siap
        weedDescription = description;

        // Mendapatkan resource ID dari nama gambar
        int imageResId = getResources().getIdentifier(weedImageName, "drawable", getPackageName());

        // Mengatur gambar pada ImageView
        weedImageView.setImageResource(imageResId);

        // Menyembunyikan TextView rekomendasi herbisida jika label "Tidak_Ada_Gulma" terdeteksi
        if (weedName.equals("Tidak_Ada_Gulma")) {
            herbicideRecommendationTextView.setVisibility(View.GONE);
        } else {
            herbicideRecommendationTextView.setText(herbicideRecommendation);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Bahasa tidak didukung");
            } else {
                // Panggil fungsi untuk membaca deskripsi gulma secara suara
                speakDescription(weedDescription);
            }
        } else {
            Log.e("TextToSpeech", "Inisialisasi gagal");
        }
    }

    @Override
    protected void onDestroy() {
        // Stop TextToSpeech saat aktivitas dihancurkan
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    // Fungsi untuk membaca deskripsi secara suara
    private void speakDescription(String description) {
        textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menangani tombol panah kembali
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

