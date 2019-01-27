package com.android.theflasherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class FlashlightActivity extends AppCompatActivity {
    private Button buttonSwitch;
    private boolean hasFlash;
    private static final int CAMERA_REQUEST = 50;
    private boolean cameraFlash = false;
    public static Camera cam = null;
    private Parameters params;

    private static final String LOG_TAG = FlashlightActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);

        buttonSwitch = (Button) findViewById(R.id.buttonSwitch);
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;

        buttonSwitch.setEnabled(!isEnabled);


        // ask for permission
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                && !(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))) {
            // permission not granted, so ask permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }



        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasFlash) {
                    if (cameraFlash) {
                        turnOn();
                    } else {
                        turnOff();
                    }
                }
            }
        });

    }

    public void turnOn() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            params = cam.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(params);
            cam.startPreview();
            cameraFlash = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOff() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            params = cam.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(params);
            cam.stopPreview();
            cameraFlash = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CAMERA_REQUEST :
                if (grantResults.length > 0  &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonSwitch.setEnabled(false);
                    buttonSwitch.setText(R.string.camera_enabled);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
