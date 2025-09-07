package com.example.identifikasigulma;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Menjalankan animasi scan selama 3 detik
        new Handler().postDelayed(() -> {
            // Setelah animasi selesai, beralih ke WeedDetailActivity
            Intent intent = new Intent(ScanActivity.this, WeedDetailActivity.class);
            intent.putExtra("NAMA_GULMA", getIntent().getStringExtra("NAMA_GULMA"));
            intent.putExtra("NAMA_ILMIAH", getIntent().getStringExtra("NAMA_ILMIAH"));
            intent.putExtra("DESKRIPSI", getIntent().getStringExtra("DESKRIPSI"));
            intent.putExtra("HERBISIDA", getIntent().getStringExtra("HERBISIDA"));
            intent.putExtra("GAMBAR_GULMA", getIntent().getStringExtra("GAMBAR_GULMA"));
            startActivity(intent);
            finish();
        }, 1500); // 3 detik
    }
}
