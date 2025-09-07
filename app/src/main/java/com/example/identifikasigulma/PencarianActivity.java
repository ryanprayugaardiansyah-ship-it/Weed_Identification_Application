package com.example.identifikasigulma;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PencarianActivity extends AppCompatActivity {
    private GulmaAdapter adapter;
    private List<Gulma> gulmaList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pencarian);

        // Buat daftar gulma dan tambahkan data gulma
        gulmaList = new ArrayList<>();
        gulmaList.add(new Gulma(
                "Kangkung Sawah",
                "Kangkung Sawah memiliki nama latin Ipomoea Aquatica adalah tanaman herba yang tumbuh di Asia yang seringkali digunakan untuk tumisan dan hidangan lainnya. Tanaman kangkung memiliki akar jenis tunggang dan memiliki banyak percabangan, batang kangkung berbentuk bulat, berlubang dan mengandung banyak air. Tumbuhan ini dapat menjadi ancaman ekoloogis karena merambat dan menaungi tanaman budidaya lainnya, yang menyebabkan tanaman tersebut kehilangan sinar matahari dan oksigen.",
                "Ipomoea Aquatica", R.drawable.kangkung_sawah));
        gulmaList.add(new Gulma(
                "Cacabean",
                "Cacabean atau dalam bahasa latin nya disebut Ludwigia octovalvis, merupakan salah satu gulma dengan tipe aquatic weed, gulma ini hampir bisa ditemui disemua wilayah seperti di Amerika, Australia, Afrika dan di Asia, termasuk di Indonesia, di Indonesia sendiri cacabean atau Ludwigia octovalvis merupakan gulma pada tanaman padi. Cacabean atau Ludwigia octovalvis mempunyai bunga yang berwarna kuning dengan mempunyai 4 kelopak bungan dengan masing-masing panjangnya 10 mm, kebutuhan cahaya untuk gulma ini bisa menyesuaikan diri, cacabean mampu tumbuh baik dengan cahaya yang banyak dan juga pada cahaya yang ternaungi.",
                "Ludwigia", R.drawable.cacabean));
        gulmaList.add(new Gulma(
                "Kucing-kucingan",
                "Acalypha indica merupakan salah satu gulma yang memiliki banyak nama daerah diantaranya kucing-kucingan, kucing galak, akar kucing, anting-anting, bayam kucing, dan masih banyak lagi. Hal ini dikarenakan, kucing sangat tertarik dengan akar pohon ini karena gulma ini berfungsi untuk melancarkan pencernaan kucing. Tanaman ini tersebar luas di wilayah tropis mulai dari Afrika Barat menuju India, Indo-China menuju Filipina dan Jawa. Acalypha Indica atau kucing-kucingan merupakan gulma yang umumnya tumbuh secara liar di pinggir jalan, lapangan rumput, lahan sawah, maupun di lereng bukit",
                "Acalypha indica", R.drawable.kucing_kucingan));
        gulmaList.add(new Gulma(
                "Maman Lanang",
                "Sieruela Rutidosperma atau yang sering disebut Maman Lanang Merupakan salah satu tumbuhan gulma yang termasuk dalam salah satu anggota famili cleomaceae. Maman Lanang biasanya ditemukan di pinggir jalan, sawah, ladang dan perkebunan warga. Karena dapat berkembang biak dengan sangat cepat, Maman Lanang dianggap sebagai spesies invasif di sebagian besar negara",
                "Sieruela Rutidosperma", R.drawable.maman_lanang));
        gulmaList.add(new Gulma(
                "Alternanthera Philoxeroides",
                "Alternanthera Philoxeroides atau yang sering disebut sebagai gulma Aligator, adalah spesies asli daerah beriklim sedang di Amerika Selatan, yang meliputi Argentina, Brazil, Paraguay, dan Uruguay. Gulma ini dapat tumbuh subur di lingkungan kering dan perairan dan ditandai dengan bunga tipis berwarna keputihan di sepanjang tangkai, batang berongga tidak beraturan dan pola daun sederhana.",
                "Alternanthera Philoxeroides", R.drawable.alternanthera_philoxeroides));
        gulmaList.add(new Gulma(
                "Rumput Belulang",
                "Eleusine Indica atau yang biasa di sebut Rumput Belulang merupakan salah satu gulma tanaman budidaya yang mempunyai daya saing yang tinggi dari keluarga Poaceae atau termasuk dalam keluarga rumput-rumputan. rumput belulang berkembang biak dengan menggunakan biji (Seeds). Tanaman ini diangggap sebagai gulma karena memang sulit untuk ditangani. salah satu pengendalian rumput belulang yang ampuh yaitu dengan menggunakan herbisida.",
                "Eleusine Indica", R.drawable.rumput_belulang));
        gulmaList.add(new Gulma(
                "Kate Mas",
                "Kate Mas memiliki nama latin Euphorbia Heterophylla. Kate Mas merupakan tanaman gulma yang tumbuh pada daerah lembab. Tanaman ini berasal dari Amerika Tengah dan Amerika Selatan. Penyebarannya meluas ke daerah tropis dan subtropis termasuk ke Indonesia. Diperkenalkan ke Asia Selatan dan Tenggara sebagai tanaman hias. Patik mas juga seringkali dianggap sebagai gulma, namun berkhasiat sebagai obat dan banyak digunakan dalam pengobatan tradisional Afrika. Daun mengandung senyawa dengan aktivitas diantaranya antimikrobia dan antiinflamasi.",
                "Euphorbia Heterophylla", R.drawable.kate_mas));
        gulmaList.add(new Gulma(
                "Putri Malu",
                "Putri Malu atau Mimosa Pudica adalah salah satu tumbuhan yang dikategorikan kedalam jenis tumbuhan gulma dengan digolongkan kedalam anggota suku polong-polongan. Tumbuhan yang mempunyai nama latin Mimosa pudica ini tergolong kedalam keluarga Fabaceae yang berati suku polong polongan. Putri malu mudah sekali dikenali karena tumbuhan ini mempunyai sifat khas yakni, jika kita sentuh daun dari tumbuhan ini akan menciut atau layu dengan cepat, namun jika dibiarkan dalam beberapa menit daunnya akan kembali sedia kala.",
                "Mimosa Pudica", R.drawable.putri_malu));
        gulmaList.add(new Gulma(
                "Commelina Diffusa",
                "Commelina diffusa adalah tanaman tahunan atau semak yang tumbuh terutama di daerah tropis yang mengalami musim kering. Commelina diffusa memiliki daun hijau gelap yang mengkilap dan tumbuh merambat rendah. Tanaman ini lebih suka tumbuh di tempat teduh.",
                "Commelina Diffusa", R.drawable.commelina_diffusa));
        gulmaList.add(new Gulma(
                "Kiambang",
                "Pistia Stratiotes atau yang sering disebut Kiambang merupakan adalah tumbuhan air yang biasa dijumpai mengapung di perairan tenang seperti di lahan sawah, kolam, dan rawa-rawa. Apu-apu atau kiambang juga dikategorikan sebagai gulma tanaman padi. Apu-apu atau kiambang sering kali ditemui di sawah sebagai gulma, dengan menghisap unsur hara pada tanah sehingga tanaman padi akan bersaing untuk mendapatkan unsur hara yang kita berikan, masalah lain adalah kayambang berkembangbiak dengan cepat sehingga petani seringkali melakukan pembersihan secara berkala. Cara pengendalian lain yaitu dengan menggunakan herbisida",
                "Pistia Stratiotes", R.drawable.kiambang));

        // Inisialisasi RecyclerView dan Adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GulmaAdapter(gulmaList);
        recyclerView.setAdapter(adapter);

        // Tambahkan CustomDividerItemDecoration
        CustomDividerItemDecoration dividerItemDecoration = new CustomDividerItemDecoration(
                this, R.drawable.divider, 50, 50); // Sesuaikan margin kiri dan kanan
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Setup SearchView
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        // Set onItemClick listener
        adapter.setOnItemClickListener(new GulmaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Gulma gulma) {
                Intent intent = new Intent(PencarianActivity.this, DetailGulmaActivity.class);
                intent.putExtra("nama", gulma.getNama());
                intent.putExtra("deskripsi", gulma.getDeskripsi());
                intent.putExtra("namaIlmiah", gulma.getNamaIlmiah());
                intent.putExtra("namaGambar", gulma.getNama()); // Menggunakan nama gulma untuk menentukan gambar
                startActivity(intent);
            }
        });
    }
}
