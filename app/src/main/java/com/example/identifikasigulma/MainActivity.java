package com.example.identifikasigulma;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mengaktifkan EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //tombol ambil gambar
        Button btnAmbilGambar = findViewById(R.id.ambil);
        btnAmbilGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                } else {
                    startCameraActivity();
                }
            }
        });

        // Tombol pencarian
        Button pencarianButton = findViewById(R.id.pencarian);
        pencarianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk beralih ke halaman deskripsi aplikasi
                Intent intent = new Intent(MainActivity.this, PencarianActivity.class);
                startActivity(intent);
            }
        });

        // Tombol tentang
        Button tentangButton = findViewById(R.id.tentang);
        tentangButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk beralih ke halaman deskripsi aplikasi
                Intent intent = new Intent(MainActivity.this, TentangActivity.class);
                startActivity(intent);
            }
        });

        // Tombol riwayat
        Button riwayatButton = findViewById(R.id.riwayat);
        riwayatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk beralih ke halaman riwayat identifikasi
                Intent intent = new Intent(MainActivity.this, RiwayatActivity.class);
                startActivity(intent);
            }
        });

        // Tombol keluar
        Button exitButton = findViewById(R.id.keluar);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Munculkan dialog konfirmasi sebelum menutup aplikasi
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Konfirmasi Keluar");
                builder.setMessage("Apakah Anda ingin keluar dari aplikasi?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Menutup aplikasi
                        finish();
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Biarkan aplikasi tetap berjalan
                        dialog.dismiss(); // Tutup dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        // Mengatur padding berdasarkan sistem bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Izin kamera diberikan");
                Toast.makeText(this, "Izin kamera diberikan. Anda bisa memulai kamera sekarang.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Izin kamera tidak diberikan");
                Toast.makeText(this, "Izin kamera dibutuhkan untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraActivity() {
        Log.d(TAG, "Memulai CameraActivity");
        Intent intent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(intent);
    }
}
