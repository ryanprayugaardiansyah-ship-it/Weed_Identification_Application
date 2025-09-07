package com.example.identifikasigulma;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class ScanMultipleActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_multiple);

        // Menjalankan animasi scan selama 1.5 detik
        new Handler().postDelayed(() -> {
            // Setelah animasi selesai, beralih ke WeedDetailActivity
            Intent intent = new Intent(ScanMultipleActivity.this, MultipleWeedDetailActivity.class);
            intent.putExtra("WEED_NAMES", getIntent().getStringArrayExtra("WEED_NAMES"));
            intent.putExtra("SCIENTIFIC_NAMES", getIntent().getStringArrayExtra("SCIENTIFIC_NAMES"));
            intent.putExtra("DESCRIPTIONS", getIntent().getStringArrayExtra("DESCRIPTIONS"));
            intent.putExtra("HERBICIDE", getIntent().getStringExtra("HERBICIDE"));
            startActivity(intent);
            finish();
        }, 1500); // 1.5 detik
    }
}
