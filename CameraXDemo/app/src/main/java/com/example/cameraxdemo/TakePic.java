package com.example.cameraxdemo;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TakePic  extends AppCompatActivity implements  ImageAnalysis.Analyzer {


    private PreviewView preView;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRE_PERMISSIONS = new String[]{"android.permission.CAMERA"
            ,"android.permission.RECORD_AUDIO"};
    ImageView imageView;
    EditText picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);

        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.editTextTextPersonName);
        preView = findViewById(R.id.previewView);
        Button button = findViewById(R.id.button);

        if(!allPermissionGranted()){
            ActivityCompat.requestPermissions(this, REQUIRE_PERMISSIONS,REQUEST_CODE_PERMISSIONS);
        }
        startCamera();
        button.setOnClickListener(view -> {
            TakePic();
        });
    }

    private void TakePic(){
        long timestamp = System.currentTimeMillis();
        ImageCapture.OutputFileOptions option1 = new ImageCapture.OutputFileOptions
                .Builder(new File(getApplicationContext().getFilesDir(),
                String.valueOf(timestamp))).build();
        imageCapture.takePicture(
                option1,
                executor,
                new ImageCapture.OnImageSavedCallback(){

                    @SuppressLint("NotConstructor")
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        runOnUiThread(() -> {
                            final Uri selectedImg = outputFileResults.getSavedUri();
                            try {
                                picture.setText(outputFileResults.getSavedUri().getPath());
                                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                                db.insertUri(outputFileResults.getSavedUri().getPath());
                                imageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImg));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                    }
                }
        );
    }
    private boolean allPermissionGranted(){
        for(String permission: REQUIRE_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission,grantResults);
        switch (requestCode){
            case REQUEST_CODE_PERMISSIONS:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(TakePic.this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(TakePic.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void startCamera(){
        final ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },ContextCompat.getMainExecutor(this));
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(preView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
        videoCapture = new VideoCapture.Builder().setVideoFrameRate(30).build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(executor,this);
        cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageCapture,videoCapture);
    }
    @Override
    public void analyze (@NonNull ImageProxy image){
        image.close();
    }
}
