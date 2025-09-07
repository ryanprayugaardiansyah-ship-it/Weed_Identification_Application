package com.example.identifikasigulma;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.util.ArrayList;

public class TFLiteHelper {

    private static final String MODEL_NAME = "MobileNetV2.tflite";
    private static final int MODEL_INPUT_WIDTH = 224;
    private static final int MODEL_INPUT_HEIGHT = 224;
    private static final int MODEL_INPUT_CHANNELS = 3;
    private static final float THRESHOLD = 0.5f; // Adjust this threshold as needed
    private final Interpreter interpreter;
    private String[] labels;

    public TFLiteHelper(Context context) throws IOException {
        MappedByteBuffer modelFile = loadModelFile(context);
        Interpreter.Options options = new Interpreter.Options();
        interpreter = new Interpreter(modelFile, options);
        loadLabels(context);
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor fileDescriptor = assetManager.openFd(MODEL_NAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void loadLabels(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream labelsInput = assetManager.open("labels.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(labelsInput));
        ArrayList<String> labelList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        labels = labelList.toArray(new String[0]);
    }

    public float[] predict(float[] input) {
        TensorBuffer inputBuffer = TensorBuffer.createFixedSize(new int[]{1, MODEL_INPUT_WIDTH, MODEL_INPUT_HEIGHT, MODEL_INPUT_CHANNELS}, DataType.FLOAT32);
        inputBuffer.loadArray(input);

        TensorBuffer outputBuffer = TensorBuffer.createFixedSize(new int[]{1, getOutputSize()}, DataType.FLOAT32);
        interpreter.run(inputBuffer.getBuffer(), outputBuffer.getBuffer().rewind());
        return outputBuffer.getFloatArray();
    }

    private int getOutputSize() {
        return interpreter.getOutputTensor(0).shape()[1];
    }

    public String getLabel(int index) {
        if (index >= 0 && index < labels.length) {
            return labels[index];
        }
        return "Label tidak tersedia";
    }

    public float getThreshold() {
        return THRESHOLD;
    }
}


