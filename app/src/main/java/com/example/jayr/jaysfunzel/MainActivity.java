package com.example.jayr.jaysfunzel;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Switch s1;
    Camera cam;
    Camera.Parameters param;
    boolean bLichtAn;
    boolean bHasFlash;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        s1 = (Switch) findViewById(R.id.switch1);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Touch für Licht an");
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                        if (cam == null) {
                            cam = Camera.open();
                        }
                        param = cam.getParameters();
                        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(param);
                        cam.startPreview();
                        bLichtAn = true;
                        bHasFlash = true;
                        textView.setText("Touch für Licht aus");
                    }else{
                        Toast.makeText(getApplicationContext(), "Keine Foto-LED vorhanden...", Toast.LENGTH_SHORT).show();
                        bLichtAn = false;
                        s1.setChecked(false);
                    }
                }
                if (!isChecked) {
                    LichtAus();
                }
            }
        });
    };


    public void LichtAus() {
        if (!bLichtAn && bHasFlash) {
            cam = Camera.open();
        }
        if (bHasFlash) {
            param = cam.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(param);
            cam.stopPreview();
        }
        bLichtAn = false;
        textView.setText("Touch für Licht an");
    }

    @Override
    protected void onPause() {
        if (bLichtAn){
            LichtAus();
            cam.release();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        s1.setChecked(false);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
