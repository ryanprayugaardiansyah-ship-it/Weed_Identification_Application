package com.example.identifikasigulma;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class DetailGulmaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gulma);

        // Mengatur Toolbar sebagai ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menambahkan tombol panah kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Terima data dari Intent
        Intent intent = getIntent();
        String nama = intent.getStringExtra("nama");
        String deskripsi = intent.getStringExtra("deskripsi");
        String namaIlmiah = intent.getStringExtra("namaIlmiah");
        String namaGambar = intent.getStringExtra("namaGambar");

        // Set data ke view
        TextView textViewNama = findViewById(R.id.text_view_nama);
        TextView textViewNamaIlmiah = findViewById(R.id.text_view_nama_ilmiah);
        TextView textViewDeskripsi = findViewById(R.id.text_view_deskripsi);
        ViewPager2 viewPagerGulma = findViewById(R.id.view_pager_gulma);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        textViewNama.setText(nama);
        textViewNamaIlmiah.setText(namaIlmiah);
        textViewDeskripsi.setText(deskripsi);

        // Siapkan daftar resource ID gambar yang ingin ditampilkan
        List<Integer> imageResourceIds = new ArrayList<>();
        if ("Maman Lanang".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.maman_lanang_1);
            imageResourceIds.add(R.drawable.maman_lanang_2);
            imageResourceIds.add(R.drawable.maman_lanang_3);
            imageResourceIds.add(R.drawable.maman_lanang_4);
        } else if ("Alternanthera Philoxeroides".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.alternanthera_philoxeroides_1);
            imageResourceIds.add(R.drawable.alternanthera_philoxeroides_2);
            imageResourceIds.add(R.drawable.alternanthera_philoxeroides_3);
        } else if ("Kangkung Sawah".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.kangkung_sawah_1);
            imageResourceIds.add(R.drawable.kangkung_sawah_2);
            imageResourceIds.add(R.drawable.kangkung_sawah_3);
        } else if ("Cacabean".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.cacabean_1);
            imageResourceIds.add(R.drawable.cacabean_2);
        } else if ("Kucing-kucingan".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.kucing_kucingan_1);
            imageResourceIds.add(R.drawable.kucing_kucingan_2);
        } else if ("Rumput Belulang".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.rumput_belulang_1);
        } else if ("Kate Mas".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.kate_mas_1);
            imageResourceIds.add(R.drawable.kate_mas_2);
            imageResourceIds.add(R.drawable.kate_mas_3);
        } else if ("Putri Malu".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.putri_malu_1);
            imageResourceIds.add(R.drawable.putri_malu_2);
            imageResourceIds.add(R.drawable.putri_malu_3);
        } else if ("Commelina Diffusa".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.commelina_diffusa_1);
            imageResourceIds.add(R.drawable.commelina_diffusa_2);
        } else if ("Kiambang".equals(namaGambar)) {
            imageResourceIds.add(R.drawable.kiambang_2);
            imageResourceIds.add(R.drawable.kiambang_1);
            imageResourceIds.add(R.drawable.kiambang_3);
        }

        // Atur adapter untuk ViewPager2
        ImagePagerAdapter adapter = new ImagePagerAdapter(imageResourceIds);
        viewPagerGulma.setAdapter(adapter);

        // Hubungkan TabLayout dengan ViewPager2
        new TabLayoutMediator(tabLayout, viewPagerGulma, (tab, position) -> {
            // Kosong, tidak perlu mengatur teks pada tab
        }).attach();
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
