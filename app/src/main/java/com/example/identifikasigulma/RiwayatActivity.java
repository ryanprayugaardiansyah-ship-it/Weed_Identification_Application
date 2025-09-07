package com.example.identifikasigulma;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Collections;

public class RiwayatActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        listView = findViewById(R.id.listViewRiwayat);

        displayIdentificationHistory();

        // Mengatur Toolbar sebagai ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menambahkan tombol panah kembali
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    //Menampilkan riwayat identifikasi gulma dari database.
    private void displayIdentificationHistory() {
        DatabaseGulma dbHelper = new DatabaseGulma(this);
        Cursor cursor = dbHelper.getWeedIdentificationHistory();

        // Filter data untuk menghilangkan "Tidak_Ada_Gulma"
        MatrixCursor filteredCursor = new MatrixCursor(new String[] {
                DatabaseGulma.COLUMN_ID,
                DatabaseGulma.COLUMN_IDENTIFIED_WEED,
                DatabaseGulma.COLUMN_SCIENTIFIC_NAME,
                DatabaseGulma.COLUMN_IMAGE_URL
        });

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String identifiedWeed = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_IDENTIFIED_WEED));
                if (!identifiedWeed.equals("Tidak_Ada_Gulma")) {
                    // Tambahkan data ke filteredCursor jika bukan "Tidak_Ada_Gulma"
                    @SuppressLint("Range") int weedId = cursor.getInt(cursor.getColumnIndex(DatabaseGulma.COLUMN_ID));
                    @SuppressLint("Range") String scientificName = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_SCIENTIFIC_NAME));
                    @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_IMAGE_URL));

                    filteredCursor.addRow(new Object[]{
                            weedId,
                            identifiedWeed,
                            scientificName,
                            imageUrl
                    });
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        String[] fromColumns = {
                DatabaseGulma.COLUMN_IDENTIFIED_WEED,
                DatabaseGulma.COLUMN_SCIENTIFIC_NAME,
                DatabaseGulma.COLUMN_IMAGE_URL
        };
        int[] toViews = {
                R.id.textViewWeedName,
                R.id.textViewScientificName,
                R.id.imageViewWeed
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_riwayat,
                filteredCursor,
                fromColumns,
                toViews,
                0
        );

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int viewId = view.getId();
                if (viewId == R.id.textViewWeedName || viewId == R.id.textViewScientificName) {
                    String text = cursor.getString(columnIndex).replace("_", " ");
                    ((TextView) view).setText(text);
                    return true;
                } else if (viewId == R.id.imageViewWeed) {
                    String imageName = cursor.getString(columnIndex);
                    int imageResId = getResources().getIdentifier(imageName.toLowerCase(), "drawable", getPackageName());
                    Log.d("RiwayatActivity", "Image Name: " + imageName + ", Resource ID: " + imageResId);

                    ShapeableImageView imageView = (ShapeableImageView) view;
                    Glide.with(RiwayatActivity.this)
                            .load(imageResId)
                            .apply(new RequestOptions().transform(new RoundedCorners(16)))
                            .into(imageView);

                    return true;
                }
                return false;
            }
        });

        listView.setAdapter(adapter);

        // Menambahkan listener klik item ke ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    @SuppressLint("Range") int weedId = cursor.getInt(cursor.getColumnIndex(DatabaseGulma.COLUMN_ID));
                    @SuppressLint("Range") String identifiedWeed = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_IDENTIFIED_WEED));
                    @SuppressLint("Range") String scientificName = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_SCIENTIFIC_NAME));
                    @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_IMAGE_URL));

                    // Memanggil metode showWeedDetails untuk menampilkan detail gulma
                    showWeedDetails(identifiedWeed, scientificName, imageUrl);
                }
            }
        });

        // Menambahkan listener klik lama item ke ListView
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    @SuppressLint("Range") int weedId = cursor.getInt(cursor.getColumnIndex(DatabaseGulma.COLUMN_ID));
                    showDeleteConfirmationDialog(weedId);
                }
                return true;
            }
        });
    }

    private void showDeleteConfirmationDialog(int weedId) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Gulma")
                .setMessage("Apakah Anda yakin ingin menghapus gulma ini dari riwayat?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteWeedHistory(weedId);
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteWeedHistory(int id) {
        DatabaseGulma dbHelper = new DatabaseGulma(this);
        dbHelper.deleteWeedIdentification(id);
        displayIdentificationHistory(); // Refresh ListView
    }


    /**
     * Menampilkan detail gulma berdasarkan data yang dipilih dari ListView.
     *
     * @param identifiedWeed Nama gulma yang teridentifikasi
     * @param scientificName Nama ilmiah gulma
     * @param imageUrl URL gambar gulma
     */
    private void showWeedDetails(String identifiedWeed, String scientificName, String imageUrl) {
        DatabaseGulma dbHelper = new DatabaseGulma(this);
        Cursor cursor = dbHelper.getWeedDetails(identifiedWeed);

        if (cursor != null && cursor.moveToFirst()) {
            int weedId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_ID));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_DESCRIPTION));
            @SuppressLint("Range") String herbicideRecommendation = cursor.getString(cursor.getColumnIndex(DatabaseGulma.COLUMN_HERBICIDE_NAME));

            Cursor herbicideCursor = dbHelper.getHerbicideRecommendations(weedId);
            ArrayList<String> herbicideRecommendations = new ArrayList<>();
            if (herbicideCursor != null && herbicideCursor.moveToFirst()) {
                do {
                    String cropType = herbicideCursor.getString(herbicideCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_CROP_TYPE));
                    String herbicideName = herbicideCursor.getString(herbicideCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_HERBICIDE_NAME));
                    herbicideRecommendations.add(cropType + " : " + herbicideName);
                } while (herbicideCursor.moveToNext());
                herbicideCursor.close();
            }

            Intent intent = new Intent(RiwayatActivity.this, WeedDetailActivity.class);
            intent.putExtra("NAMA_GULMA", identifiedWeed);
            intent.putExtra("NAMA_ILMIAH", scientificName);
            intent.putExtra("DESKRIPSI", description);
            intent.putExtra("HERBISIDA", TextUtils.join( "\n", herbicideRecommendations));
            intent.putExtra("GAMBAR_GULMA", imageUrl);
            startActivity(intent);
        } else {
            Log.d("RiwayatActivity", "No details found for: " + identifiedWeed);
        }

        if (cursor != null) {
            cursor.close();
        }
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