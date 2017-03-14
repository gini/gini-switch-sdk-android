package net.gini.tariffsdk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class TariffSdk {

    public static int REQUEST_CODE = 666;

    //Application context is fine
    @SuppressLint("StaticFieldLeak")
    private static volatile TariffSdk mInstance = null;

    private final Context mContext;

    public static TariffSdk getSdkInstance(@NonNull final Context context) {
        if (mInstance == null) {
            synchronized (TariffSdk.class) {
                if (mInstance == null) {
                    mInstance = new TariffSdk(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    private TariffSdk(final Context context) {
        mContext = context;
    }


    @NonNull
    public Intent getTariffSdkIntent() {

        final Intent intent = new Intent(mContext, CameraActivity.class);
        return intent;
    }
}
