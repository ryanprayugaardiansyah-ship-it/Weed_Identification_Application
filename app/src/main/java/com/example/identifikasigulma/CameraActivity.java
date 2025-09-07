package com.example.identifikasigulma;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.camera2.CameraMetadata;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.provider.MediaStore;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final int REQUEST_IMAGE_GALLERY = 100;
    private static final int MODEL_INPUT_WIDTH = 224; // Sesuaikan dengan model Anda
    private static final int MODEL_INPUT_HEIGHT = 224; // Sesuaikan dengan model Anda
    private SurfaceView mSurfaceView;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCaptureSession;
    private String cameraId;
    private DatabaseGulma dbHelper;
    private boolean isSurfaceCreated = false;
    private boolean isMultipleMode = false;
    private Button selectedButton;
    private MediaPlayer mediaPlayer;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        dbHelper = new DatabaseGulma(this);

        // Inisialisasi TextToSpeech
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(new Locale("id", "ID")); // Bahasa Indonesia
                } else {
                    Toast.makeText(CameraActivity.this, "TextToSpeech tidak tersedia", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton galleryButton = findViewById(R.id.Gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button captureButton = findViewById(R.id.Shutter);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
                playShutterSound();
            }
        });

        Button singleButton = findViewById(R.id.Single);
        singleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearTemporaryImages(); // Menghapus gambar sementara
                isMultipleMode = false;
                setButtonPressed(singleButton);
                String message = "Mode Single sekarang terpilih, silahkan potret atau pilih 1 gulma";
                Toast.makeText(CameraActivity.this, message, Toast.LENGTH_SHORT).show();
                speakOut(message);
            }
        });

        Button multipleButton = findViewById(R.id.Multiple);
        multipleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMultipleMode = true;
                setButtonPressed(multipleButton);
                String message = "Mode Multipel atau beberapa sekarang terpilih, silahkan potret atau pilih 3 gulma";
                Toast.makeText(CameraActivity.this, message, Toast.LENGTH_SHORT).show();
                speakOut(message);
            }
        });

        ImageButton helpButton = findViewById(R.id.Help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelpDialog();
            }
        });

        mSurfaceView = findViewById(R.id.camera_preview);
        mSurfaceView.getHolder().addCallback(this);
        openCamera();
    }

    private void speakOut(String message) {
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
    }


    private void playShutterSound() {
        if (mediaPlayer == null) {
            // Inisialisasi MediaPlayer dengan suara jepretan dari res/raw
            mediaPlayer = MediaPlayer.create(this, R.raw.shutter_2);
        }

        if (mediaPlayer != null) {
            // Putar suara jepretan
            mediaPlayer.start();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        // Menutup TextToSpeech untuk menghindari kebocoran memori
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        // Pastikan untuk melepaskan sumber daya MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        isSurfaceCreated = true;
        // Jika surface sudah dibuat dan kamera juga sudah dibuka, maka buat session pratinjau
        if (isSurfaceCreated && mCameraDevice != null) {
            createCameraPreviewSession();
        }
    }

    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // Tidak perlu diimplementasikan untuk saat ini
    }

    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        isSurfaceCreated = false;
        // Tidak perlu diimplementasikan untuk saat ini
    }

    private void openCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0]; // Mendapatkan ID kamera pertama
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin kamera dibutuhkan untuk menggunakan fitur ini.", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
                return;
            }
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice cameraDevice) {
                    mCameraDevice = cameraDevice;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                    cameraDevice.close();
                    mCameraDevice = null;
                }

                @Override
                public void onError(@NonNull CameraDevice cameraDevice, int error) {
                    cameraDevice.close();
                    mCameraDevice = null;
                    Toast.makeText(CameraActivity.this, "Kamera error.", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Izin kamera tidak diberikan.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceHolder holder = mSurfaceView.getHolder();
            Surface surface = holder.getSurface();
            final CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mCaptureSession = session;
                    try {
                        mCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(CameraActivity.this, "Gagal membuat pratinjau kamera.", Toast.LENGTH_SHORT).show();
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void captureImage() {
        if (mCameraDevice == null) return;
        try {
            final ImageReader imageReader = ImageReader.newInstance(MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(imageReader.getSurface());
            SurfaceHolder holder = mSurfaceView.getHolder();
            Surface surface = holder.getSurface();
            outputSurfaces.add(surface);

            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    try (Image image = reader.acquireLatestImage()) {
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        if (isMultipleMode) {
                            // Simpan gambar ke penyimpanan sementara
                            saveTemporaryImage(bitmap);
                        } else {
                            // Identifikasi gambar secara langsung
                            identifyWeed(bitmap);
                        }
                        // Membuat sesi tangkapan baru setelah selesai mengambil gambar
                        createCameraPreviewSession();
                    }
                }
            };
            imageReader.setOnImageAvailableListener(readerListener, null);

            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), null, null);
                        // Tidak perlu menutup sesi tangkapan di sini
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void saveTemporaryImage(Bitmap bitmap) {
        if (bitmap == null) return;

        LinearLayout imageContainer = findViewById(R.id.temporary_image_container);

        if (!isMultipleMode) {
            // Jika mode Single, hapus gambar sementara sebelumnya
            imageContainer.removeAllViews();
        }

        // Membuat ImageView baru untuk gambar yang disimpan
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.temp_image_width),
                getResources().getDimensionPixelSize(R.dimen.temp_image_height));
        layoutParams.setMargins(8, 0, 8, 0);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bitmap);

        // Menambahkan ImageView ke container
        imageContainer.addView(imageView);

        // Memeriksa apakah sudah ada 3 gambar disimpan
        if (imageContainer.getChildCount() >= 3) {
            // Identifikasi ketika sudah ada 3 gambar disimpan
            identifyMultipleWeeds(imageContainer);
        }
    }

    private void clearTemporaryImages() {
        LinearLayout imageContainer = findViewById(R.id.temporary_image_container);
        imageContainer.removeAllViews();
    }

    private void identifyMultipleWeeds(LinearLayout imageContainer) {
        int childCount = imageContainer.getChildCount();

        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            View view = imageContainer.getChildAt(i);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                bitmaps.add(bitmap);
            }
        }

        if (bitmaps.size() == 3) {
            // Lakukan proses identifikasi pada setiap gambar
            String[] weedNames = new String[3];
            String[] scientificNames = new String[3];
            String[] descriptions = new String[3];
            String herbicideRecommendation = ""; // Temporary placeholder

            for (int i = 0; i < bitmaps.size(); i++) {
                Bitmap bitmap = bitmaps.get(i);
                weedNames[i] = identifyWeed(bitmap); // Anda mungkin perlu mengubah metode ini untuk mengembalikan hasil identifikasi
            }

            // Mendapatkan detail untuk setiap gulma yang teridentifikasi
            for (int i = 0; i < weedNames.length; i++) {
                if (weedNames[i] != null) {
                    Cursor cursor = dbHelper.getWeedDetails(weedNames[i]);
                    if (cursor != null && cursor.moveToFirst()) {
                        scientificNames[i] = cursor.getString(cursor.getColumnIndexOrThrow("nama_ilmiah"));
                        descriptions[i] = cursor.getString(cursor.getColumnIndexOrThrow("deskripsi"));
                        cursor.close();
                    } else {
                        scientificNames[i] = "Data not available";
                        descriptions[i] = "Data not available";
                    }
                } else {
                    scientificNames[i] = "Data not available";
                    descriptions[i] = "Data not available";
                }
            }

            // Kirim data ke MultipleWeedDetailActivity
            Intent intent = new Intent(CameraActivity.this, ScanMultipleActivity.class);
            intent.putExtra("WEED_NAMES", weedNames);
            intent.putExtra("SCIENTIFIC_NAMES", scientificNames);
            intent.putExtra("DESCRIPTIONS", descriptions);
            intent.putExtra("HERBICIDE", herbicideRecommendation);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please select exactly 3 images.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonPressed(Button button) {
        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.custom_button_background);
        }
        button.setBackgroundResource(R.drawable.button_pressed);
        selectedButton = button;
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_help, null);
        builder.setView(dialogView);

        AlertDialog helpDialog = builder.create();

        ImageButton closeButton = dialogView.findViewById(R.id.button_close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpDialog.dismiss();
            }
        });
        helpDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    if (bitmap != null) {
                        if (isMultipleMode) {
                            // Simpan gambar ke penyimpanan sementara
                            saveTemporaryImage(bitmap);
                        } else {
                            // Identifikasi gambar secara langsung
                            identifyWeed(bitmap);
                        }
                    } else {
                        Toast.makeText(this, "Gagal memuat gambar dari galeri.", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Gagal memuat gambar dari galeri.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Gagal memuat gambar dari galeri.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String identifyWeed(Bitmap bitmap) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, true);
        float[] input = bitmapToFloatArray(resizedBitmap);

        String identifiedWeed = null;
        try {
            TFLiteHelper tfliteHelper = new TFLiteHelper(this);
            TensorBuffer inputBuffer = TensorBuffer.createFixedSize(new int[]{1, MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, 3}, DataType.FLOAT32);
            inputBuffer.loadArray(input);
            float[] predictions = tfliteHelper.predict(inputBuffer.getFloatArray());

            int maxIndex = 0;
            float maxConfidence = predictions[0];
            for (int i = 1; i < predictions.length; i++) {
                if (predictions[i] > maxConfidence) {
                    maxConfidence = predictions[i];
                    maxIndex = i;
                }
            }

            if (maxConfidence >= tfliteHelper.getThreshold()) {
                identifiedWeed = tfliteHelper.getLabel(maxIndex);
                Log.d("IdentifyWeed", "Max Confidence: " + maxConfidence);
                Log.d("IdentifyWeed", "Max Index: " + maxIndex);
                Log.d("IdentifyWeed", "Identified Weed: " + identifiedWeed);
                showWeedDetails(identifiedWeed);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        saveWeedIdentificationToDatabase(identifiedWeed);
        return identifiedWeed;
    }


    private void saveWeedIdentificationToDatabase(String identifiedWeed) {
        DatabaseGulma dbHelper = new DatabaseGulma(this);
        dbHelper.addWeedIdentification(identifiedWeed);
    }

    private float[] bitmapToFloatArray(Bitmap bitmap) {
        // Skala bitmap ke ukuran model
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, true);

        // Ubah bitmap ke array float
        float[] floatArray = new float[MODEL_INPUT_WIDTH * MODEL_INPUT_HEIGHT * 3];
        int index = 0;
        for (int y = 0; y < MODEL_INPUT_HEIGHT; y++) {
            for (int x = 0; x < MODEL_INPUT_WIDTH; x++) {
                int pixel = resizedBitmap.getPixel(x, y);
                floatArray[index++] = ((pixel >> 16) & 0xFF) / 255.0f; // R
                floatArray[index++] = ((pixel >> 8) & 0xFF) / 255.0f;  // G
                floatArray[index++] = (pixel & 0xFF) / 255.0f;         // B
            }
        }
        return floatArray;
    }

    private void showWeedDetails(String identifiedWeed) {
        Log.d("CameraActivity", "Identified weed: " + identifiedWeed);

        DatabaseGulma dbHelper = new DatabaseGulma(this);
        Cursor cursor = dbHelper.getWeedDetails(identifiedWeed);

        if (cursor != null && cursor.moveToFirst()) {
            int weedId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_ID));
            String scientificName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_SCIENTIFIC_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_DESCRIPTION));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_IMAGE_URL));

            Log.d("CameraActivity", "Weed ID: " + weedId);
            Log.d("CameraActivity", "Scientific Name: " + scientificName);
            Log.d("CameraActivity", "Description: " + description);
            Log.d("CameraActivity", "Image URL: " + imageUrl);

            Cursor herbicideCursor = dbHelper.getHerbicideRecommendations(weedId);
            ArrayList<String> herbicideRecommendations = new ArrayList<>();
            if (herbicideCursor != null && herbicideCursor.moveToFirst()) {
                do {
                    String cropType = herbicideCursor.getString(herbicideCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_CROP_TYPE));
                    String herbicideName = herbicideCursor.getString(herbicideCursor.getColumnIndexOrThrow(DatabaseGulma.COLUMN_HERBICIDE_NAME));
                    herbicideRecommendations.add(cropType + ": " + herbicideName);
                } while (herbicideCursor.moveToNext());
                herbicideCursor.close();
            }

            Intent intent = new Intent(CameraActivity.this, ScanActivity.class);
            intent.putExtra("NAMA_GULMA", identifiedWeed);
            intent.putExtra("NAMA_ILMIAH", scientificName);
            intent.putExtra("DESKRIPSI", description);
            intent.putExtra("HERBISIDA", TextUtils.join("\n", herbicideRecommendations));
            intent.putExtra("GAMBAR_GULMA", imageUrl);
            startActivity(intent);
        } else {
            Log.d("CameraActivity", "No details found for: " + identifiedWeed);
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}