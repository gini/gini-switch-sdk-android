package net.gini.tariffsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (getCallingActivity() == null) {
            //TODO
            throw new RuntimeException("WRONG!");
        }
    }
}
