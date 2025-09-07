package com.example.identifikasigulma;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;

public class TentangActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        // Mengatur Toolbar sebagai ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton instagramButton = findViewById(R.id.imageButton);
        instagramButton.setOnClickListener(v -> {
            String url = "https://www.instagram.com/ryprasyah/?igsh=ZWkzamd5Z3BkdmNs";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        // Menambahkan tombol panah kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Mengatur aksi ketika tombol panah kembali ditekan
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Panggil finish() untuk menutup aktivitas dan kembali ke halaman sebelumnya
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
