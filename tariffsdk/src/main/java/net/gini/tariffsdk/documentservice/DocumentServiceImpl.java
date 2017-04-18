package net.gini.tariffsdk.documentservice;


import android.content.Context;
import android.net.Uri;

public class DocumentServiceImpl implements DocumentService {

    private static DocumentService mInstance;

    private final Context mContext;

    private DocumentServiceImpl(final Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void deleteImage(final Uri imageUri) {
        //TODO
    }

    @Override
    public void keepImage(final Uri imageUri) {
        //TODO
    }

    public static DocumentService getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new DocumentServiceImpl(context);
        }
        return mInstance;
    }
}
