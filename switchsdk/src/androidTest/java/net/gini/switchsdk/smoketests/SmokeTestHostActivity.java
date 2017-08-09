package net.gini.switchsdk.smoketests;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.gini.switchsdk.SwitchSdk;
import net.gini.switchsdk.TakePictureActivity;

//Needed since TakePictureActivity has to have a parent activity
public class SmokeTestHostActivity extends Activity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, TakePictureActivity.class);
        intent.putExtras(getIntent());
        startActivityForResult(intent, SwitchSdk.REQUEST_CODE);
    }
}
